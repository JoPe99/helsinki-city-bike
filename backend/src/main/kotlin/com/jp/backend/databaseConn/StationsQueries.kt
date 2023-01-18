package com.jp.backend.databaseConn

import com.jp.backend.StationModel
import com.jp.backend.StationModelWithDetails
import com.jp.backend.TopStationModel
import com.jp.backend.databaseConn.QueryHelpers.tablesExist
import com.jp.backend.databaseConn.QueryHelpers.validateDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object StationsQueries {
    /**
     * Returns all stations in the database.
     */
    fun getStationsData(): ArrayList<StationModel> {
        var ret: ArrayList<StationModel> = arrayListOf()
        transaction(DatabaseConn.db) {
            for (station in Stations.selectAll()) {
                val stationId = station[Stations.station_id].toInt()
                ret.add(
                    StationModel(
                        stationId,

                        station[Stations.name_fi],
                        station[Stations.name_se],
                        station[Stations.name_en],

                        station[Stations.address_fi],
                        station[Stations.address_se],

                        station[Stations.city_fi],
                        station[Stations.city_se],

                        station[Stations.operator],
                        station[Stations.capacity],

                        station[Stations.longitude],
                        station[Stations.latitude]
                    )
                )
            }
        }
        return ret
    }

    /**
     * Gets all station IDs from the database.
     */
    fun getAllStationIDs(): ArrayList<Int> {
        var ret: ArrayList<Int> = arrayListOf()
        if (tablesExist()) {
            transaction(DatabaseConn.db) {
                for (station in Stations.selectAll()) {
                    ret.add(station[Stations.station_id])
                }
            }
        }
        return ret
    }

    /**
     * Form detailed single station data by id given as a parameter.
     * Returns null if station is not found in the database.
     * Null should never be returned, as frontend application
     * shouldn't be able to ask for station details for non-existent
     * station.
     * @param id Station id
     * @return StationModelWithDetails || null
     */
    fun getSingleStationData(id: Int, startDate: String?, endDate: String?): StationModelWithDetails? {
        var ret: StationModelWithDetails? = null

        val timestampStartDate = validateDate(startDate, toEndOfDay = false)
        val timestampEndDate = validateDate(endDate, toEndOfDay = true)

        transaction(DatabaseConn.db) {
            Stations.select { Stations.station_id eq id }.forEach {
                ret = StationModelWithDetails(
                    it[Stations.station_id],

                    it[Stations.name_fi],
                    it[Stations.name_se],
                    it[Stations.name_en],

                    it[Stations.address_fi],
                    it[Stations.address_se],

                    it[Stations.city_fi],
                    it[Stations.city_se],

                    it[Stations.operator],
                    it[Stations.capacity],

                    it[Stations.longitude],
                    it[Stations.latitude],

                    getTotalJourneysFromStation(id, timestampStartDate, timestampEndDate),
                    getTotalJourneysToStation(id, timestampStartDate, timestampEndDate),

                    getAverageDepartDistanceForStation(id, timestampStartDate, timestampEndDate),
                    getAverageReturnDistanceForStation(id, timestampStartDate, timestampEndDate),

                    getAverageDepartDurationForStation(id, timestampStartDate, timestampEndDate),
                    getAverageReturnDurationForStation(id, timestampStartDate, timestampEndDate),

                    getTopDepartureStationsForStation(id, timestampStartDate, timestampEndDate),
                    getTopReturnStationsForStation(id, timestampStartDate, timestampEndDate)
                )
            }
        }
        return ret
    }

    /**
     * Returns an array of TopStationModels of the most popular
     * stations to go to.
     */
    private fun getTopDepartureStationsForStation(
        id: Int,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): ArrayList<TopStationModel> {
        var ret: ArrayList<TopStationModel> = arrayListOf()
        val departureStationsTable = Stations.alias("departure")
        transaction(DatabaseConn.db) {
            var table = departureStationsTable
                .join(
                    Journeys,
                    JoinType.INNER,
                    additionalConstraint = { (departureStationsTable[Stations.station_id] eq Journeys.departure_station_id) and (Journeys.departure_station_id eq id) })
                .join(
                    Stations,
                    JoinType.INNER,
                    additionalConstraint = { Stations.station_id eq Journeys.return_station_id })
                .slice(departureStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
                .selectAll()
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table
                .groupBy(departureStationsTable[Stations.station_id], Stations.station_id)
                .orderBy(Stations.station_id.count(), SortOrder.DESC).limit(5)
                .forEach {
                    ret.add(TopStationModel(it[Stations.name_fi], it[Stations.station_id.count()].toInt()))
                }

        }
        return ret
    }

    /**
     * Returns an array of TopStationModels of the most popular
     * stations to come from.
     */
    private fun getTopReturnStationsForStation(
        id: Int,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): ArrayList<TopStationModel> {
        var ret: ArrayList<TopStationModel> = arrayListOf()
        val returnStationsTable = Stations.alias("return")
        transaction(DatabaseConn.db) {
            var table = returnStationsTable
                .join(
                    Journeys,
                    JoinType.INNER,
                    additionalConstraint = { (returnStationsTable[Stations.station_id] eq Journeys.return_station_id) and (Journeys.return_station_id eq id) })
                .join(
                    Stations,
                    JoinType.INNER,
                    additionalConstraint = { Stations.station_id eq Journeys.departure_station_id })
                .slice(returnStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
                .selectAll().groupBy(returnStationsTable[Stations.station_id], Stations.station_id)
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table
                .orderBy(Stations.station_id.count(), SortOrder.DESC).limit(5)
                .forEach {
                    ret.add(TopStationModel(it[Stations.name_fi], it[Stations.station_id.count()].toInt()))
                }

        }
        return ret
    }

    private fun getTotalJourneysFromStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Int {
        var totalDeparts = 0
        transaction(DatabaseConn.db) {
            var table = Journeys.select { Journeys.departure_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            totalDeparts = table.count().toInt()
        }
        return totalDeparts
    }

    private fun getTotalJourneysToStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Int {
        var totalReturns = 0
        transaction(DatabaseConn.db) {
            val table = Journeys.select { Journeys.return_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            totalReturns = table.count().toInt()
        }
        return totalReturns
    }

    private fun getAverageDepartDistanceForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
        var averageDepartDistance = 0.0F
        transaction(DatabaseConn.db) {
            val table = Journeys.slice(Journeys.distance.avg(1))
                .select { Journeys.departure_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table.map {
                averageDepartDistance = it[Journeys.distance.avg(1)]?.toFloat() ?: 0.0F
            }
        }
        return averageDepartDistance
    }

    private fun getAverageReturnDistanceForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
        var averageReturnDistance = 0.0F
        transaction(DatabaseConn.db) {
            val table = Journeys.slice(Journeys.distance.avg(1))
                .select { Journeys.return_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table.map {
                averageReturnDistance = it[Journeys.distance.avg(1)]?.toFloat() ?: 0.0F
            }
        }
        return averageReturnDistance
    }

    private fun getAverageDepartDurationForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
        var averageDepartDuration = 0.0F
        transaction(DatabaseConn.db) {
            val table = Journeys.slice(Journeys.duration.avg(1))
                .select { Journeys.departure_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table.map {
                averageDepartDuration = it[Journeys.duration.avg(1)]?.toFloat() ?: 0.0F
            }
        }
        return averageDepartDuration
    }

    private fun getAverageReturnDurationForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
        var averageReturnDuration = 0.0F
        transaction(DatabaseConn.db) {
            val table = Journeys.slice(Journeys.duration.avg(1))
                .select { Journeys.return_station_id eq id }
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq endDate }
            }

            table.map {
                averageReturnDuration = it[Journeys.duration.avg(1)]?.toFloat() ?: 0.0F
            }
        }
        return averageReturnDuration
    }


    /** Function for getting paginated station data.
     * Not used for anything, but left here for possible
     * future needs. Sorting and searching not implemented.
     */
    fun getPaginationStationsData(pageSize: Int, offset: Long): ArrayList<StationModel> {
        var ret: ArrayList<StationModel> = arrayListOf()
        transaction(DatabaseConn.db) {
            for (station in Stations.selectAll().limit(pageSize, offset)) {
                val stationId = station[Stations.station_id].toInt()
                ret.add(
                    StationModel(
                        stationId,

                        station[Stations.name_fi],
                        station[Stations.name_se],
                        station[Stations.name_en],

                        station[Stations.address_fi],
                        station[Stations.address_se],

                        station[Stations.city_fi],
                        station[Stations.city_se],

                        station[Stations.operator],
                        station[Stations.capacity],

                        station[Stations.longitude],
                        station[Stations.latitude],
                    )
                )
            }
        }
        return ret
    }


}