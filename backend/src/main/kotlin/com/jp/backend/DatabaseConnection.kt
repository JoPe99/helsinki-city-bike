/**
 * This file includes a database connection Singleton,
 * and functions for interacting with the database.
 */

package com.jp.backend

import com.jp.backend.ValidationHelpers.updateStationIDs
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq

object DatabaseConn {
    private val dbUrl: String = System.getenv("DB_URL") ?: "postgresql://localhost:5432/postgres"
    private val dbUsername: String = System.getenv("DB_USERNAME") ?: "postgres"
    private val dbPassword: String = System.getenv("DB_PASSWORD") ?: "password"


    // Initialize database connection.
	private val db: Database =
			Database.connect(
                url = "jdbc:$dbUrl?reWriteBatchedInserts=true",
				driver = "org.postgresql.Driver",
				user = dbUsername,
				password = dbPassword)

	init {
        if (!tablesExist()) {
            // If tables don't exist, create them
            transaction(db) {
                SchemaUtils.create(Journeys)
                SchemaUtils.create(Stations)
            }
            // Send command to parse the CSV files and populate database
            CSVParser.parseStationData()
            CSVParser.parseJourneyData()
        }
	}

// Function for getting all stations
fun getStationsData(): ArrayList<StationModel> {
    var ret: ArrayList<StationModel> = arrayListOf()
    transaction(db) {
        for (station in Stations.selectAll()) {
            val stationId = station[Stations.station_id].toInt()
            ret.add(StationModel(
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
            ))
        }
    }
    return ret
}


fun getAllStationIDs(): ArrayList<Int> {
    var ret: ArrayList<Int> = arrayListOf()
    if (tablesExist()) {
        transaction(db) {
            for (station in Stations.selectAll()) {
                ret.add(station[Stations.station_id])
            }
        }
    }
    return ret
}

/** Function for getting paginated station data.
 * Not used for anything, but left here for possible
 * future needs. Sorting and searching not implemented.
 */
fun getPaginationStationsData(pageSize: Int, offset: Long, sortBy: String? = "", sortDesc: Boolean?, search: String? = ""): ArrayList<StationModel> {
    var ret: ArrayList<StationModel> = arrayListOf()
    transaction(db) {
        for (station in Stations.selectAll().limit(pageSize, offset)) {
            val stationId = station[Stations.station_id].toInt()
            ret.add(StationModel(
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
            ))
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

    transaction(db) {
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

fun getPaginationJourneysData(pageSize: Int,
                              offset: Long,
                              sortBy: String? = "",
                              sortDesc: Boolean?,
                              search: String? = null,
                              startDate: String? = null,
                              endDate: String? = null,
                              minDistance: Int? = null,
                              maxDistance: Int?  = null,
                              minDuration: Int? = null,
                              maxDuration: Int? = null): JourneyAPIResult {
    val ret = JourneyAPIResult(0, arrayListOf() )
    val orderBy = formOrderBy(sortBy, sortDesc)
    val departureStationsTable = Stations.alias("st1")
    val returnStationsTable = Stations.alias("st2")
    val timestampStartDate = validateDate(startDate, toEndOfDay = false)
    val timestampEndDate = validateDate(endDate, toEndOfDay = true)


    transaction(db) {
        var journeys = Journeys
            .innerJoin(departureStationsTable, { Journeys.departure_station_id}, {departureStationsTable[Stations.station_id]})
            .innerJoin(returnStationsTable, { Journeys.return_station_id }, {returnStationsTable[Stations.station_id]})
            .selectAll()
            search?.let {
                journeys.andWhere { (Journeys.departure_station_name like("$search%")) or (Journeys.return_station_name like ("$search%")) }
            }
            timestampStartDate?.let{
                journeys.andWhere { Journeys.departure_time greaterEq timestampStartDate }
            }
            timestampEndDate?.let{
                journeys.andWhere { Journeys.departure_time lessEq timestampEndDate }
            }
            minDistance?.let {
                journeys.andWhere { Journeys.distance greaterEq minDistance }
            }
            maxDistance?.let {
                journeys.andWhere { Journeys.distance lessEq maxDistance }
            }
            minDuration?.let {
                journeys.andWhere { Journeys.duration greaterEq minDuration }
            }
            maxDuration?.let {
                journeys.andWhere { Journeys.duration lessEq maxDuration }
            }

            // Getting length before paginating
            ret.length = journeys.count().toInt()

            journeys.orderBy(orderBy).limit(pageSize, offset)

        for (journey in journeys) {
            ret.journeys.add(JourneyModelWithStationData(
                journey[Journeys.id].toInt(),
                journey[Journeys.departure_time].toString(),
                journey[Journeys.return_time].toString(),

                journey[Journeys.departure_station_id],
                journey[Journeys.departure_station_name],
                journey[departureStationsTable[Stations.longitude]],
                journey[departureStationsTable[Stations.latitude]],

                journey[Journeys.return_station_id],
                journey[Journeys.return_station_name],
                journey[returnStationsTable[Stations.longitude]],
                journey[returnStationsTable[Stations.latitude]],

                journey[Journeys.distance],
                journey[Journeys.duration]
            ))
        }
    }
    return ret
}

private fun formOrderBy(sortBy: String?, sortDesc: Boolean?): Pair<Column<*>, SortOrder> {
    var sort: SortOrder = SortOrder.ASC;
    if (sortDesc == true) { sort = SortOrder.DESC }
    return when (sortBy) {
        "departureDateTime" -> Pair(Journeys.departure_time, sort)
        "departureStationName" -> Pair(Journeys.departure_station_name, sort)
        "returnDateTime" -> Pair(Journeys.return_time, sort)
        "returnStationName" -> Pair(Journeys.return_station_name, sort)
        "distance" -> Pair(Journeys.distance, sort)
        "duration" -> Pair(Journeys.duration, sort)
        else -> Pair(Journeys.departure_time, sort)
    }
}


fun insertIntoStations(stations: ArrayList<StationModel>) {
    println("Inserting ${stations.size} station(s)")
    transaction(db) {
        Stations.batchInsert(stations) { it ->
            this[Stations.station_id] = it.id
            this[Stations.name_fi] = it.nameFi
            this[Stations.name_se] = it.nameSe
            this[Stations.name_en] = it.nameEn
            this[Stations.address_fi] = it.addressFi
            this[Stations.address_se] = it.addressSe
            this[Stations.city_fi] = it.cityFi
            this[Stations.city_se] = it.citySe
            this[Stations.operator] = it.operator
            this[Stations.capacity] = it.capacity
            this[Stations.longitude] = it.longitude
            this[Stations.latitude] = it.latitude
        }
    }

    // After insert of station, update allowed IDs
    // to ValidationHelpers
    updateStationIDs()
}

fun insertIntoJourneys(journeys: ArrayList<JourneyModel>) {
    println("Inserting ${journeys.size} Journey(s)")
    transaction(db) {
            // Times are saved as timestamps in the database,
            // check DataModels.kt for more information
            Journeys.batchInsert(journeys) {
                this[Journeys.departure_time] = it.departureTime.toLocalDateTime()
                this[Journeys.return_time] = it.returnTime.toLocalDateTime()
                this[Journeys.departure_station_id] = it.departureStationId
                this[Journeys.departure_station_name] = it.departureStationName
                this[Journeys.return_station_id] = it.returnStationId
                this[Journeys.return_station_name] = it.returnStationName
                this[Journeys.distance] = it.distanceCovered
                this[Journeys.duration] = it.durationSeconds
            }
    }
}

// Function for deleting all data
fun deleteTables() {
    println("Destroying tables")
    if (tablesExist()) {
        transaction(db) {
            SchemaUtils.drop(Journeys)
            SchemaUtils.drop(Stations)
        }
    }
}
fun createTables() {
    println("Creating tables")
    if (!tablesExist()) {
        transaction(db) {
            SchemaUtils.create(Journeys)
            SchemaUtils.create(Stations)
        }
    }
}

fun getJourneyCount(): Long {
    var ret: Long = 0;
    transaction(db) {
        ret = Journeys.selectAll().count()
    }

    return ret
}

private fun validateDate(date: String?, toEndOfDay: Boolean): LocalDateTime? {
        val validatedDate: LocalDateTime?
        if (date.isNullOrBlank()) {
            return null
        } else {
            if (toEndOfDay) {
                try {
                    val ISODate = date + "T23:59:59"
                    validatedDate = ISODate.toLocalDateTime()
                } catch (e: Exception) {
                    println(e)
                    return null
                }
            } else {
                try {
                    val ISODate = date + "T00:00:00"
                    validatedDate = ISODate.toLocalDateTime()
                } catch (e: Exception) {
                    println(e)
                    return null
                }
            }
        }
        return validatedDate
    }

/**
 * tablesExist tries to get a count from both tables, and returns false
 * if the query results in an exception. This happens when the tables
 * are not yet created.
 */
fun tablesExist(): Boolean {
    var ret = true
    transaction(db) {
        try { Journeys.selectAll().count() } catch (e: Exception) {ret = false}
        try { Stations.selectAll().count() } catch (e: Exception) {ret = false}
    }
    return ret
}

private fun getTopDepartureStationsForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): ArrayList<TopStationModel> {
    var ret: ArrayList<TopStationModel> = arrayListOf()
    val departureStationsTable = Stations.alias("departure")
    transaction(db) {
        var table = departureStationsTable
            .join(Journeys, JoinType.INNER, additionalConstraint = {(departureStationsTable[Stations.station_id] eq Journeys.departure_station_id) and (Journeys.departure_station_id eq id)})
            .join(Stations, JoinType.INNER, additionalConstraint = {Stations.station_id eq Journeys.return_station_id})
            .slice(departureStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
            .selectAll()
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
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

private fun getTopReturnStationsForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): ArrayList<TopStationModel> {
    var ret: ArrayList<TopStationModel> = arrayListOf()
    val returnStationsTable = Stations.alias("return")
    transaction(db) {
        var table = returnStationsTable
            .join(Journeys, JoinType.INNER, additionalConstraint = {(returnStationsTable[Stations.station_id] eq Journeys.return_station_id) and (Journeys.return_station_id eq id)})
            .join(Stations, JoinType.INNER, additionalConstraint = {Stations.station_id eq Journeys.departure_station_id})
            .slice(returnStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
            .selectAll().groupBy(returnStationsTable[Stations.station_id], Stations.station_id)
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
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
    transaction(db) {
        var table = Journeys.select {Journeys.departure_station_id eq id}
                startDate?.let {
                    table.andWhere { Journeys.departure_time greaterEq startDate }
                }
                endDate?.let {
                    table.andWhere { Journeys.departure_time lessEq  endDate }
                }

            totalDeparts = table.count().toInt()
    }
    return totalDeparts
}

private fun getTotalJourneysToStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Int {
    var totalReturns = 0
    transaction(db) {
        val table = Journeys.select {Journeys.return_station_id eq id}
        startDate?.let {
            table.andWhere { Journeys.departure_time greaterEq startDate }
        }
        endDate?.let {
            table.andWhere { Journeys.departure_time lessEq  endDate }
        }

        totalReturns = table.count().toInt()
    }
    return totalReturns
}

private fun getAverageDepartDistanceForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
    var averageDepartDistance = 0.0F
    transaction(db) {
        val table = Journeys.slice(Journeys.distance.avg(1))
            .select {Journeys.departure_station_id eq id}
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
            }

            table.map {
                averageDepartDistance = it[Journeys.distance.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return averageDepartDistance
}

private fun getAverageReturnDistanceForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
    var averageReturnDistance = 0.0F
    transaction(db) {
        val table = Journeys.slice(Journeys.distance.avg(1))
            .select {Journeys.return_station_id eq id}
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
            }

            table.map {
                averageReturnDistance = it[Journeys.distance.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return averageReturnDistance
}
private fun getAverageDepartDurationForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
    var averageDepartDuration = 0.0F
    transaction(db) {
        val table = Journeys.slice(Journeys.duration.avg(1))
            .select {Journeys.departure_station_id eq id}
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
            }

            table.map {
                averageDepartDuration = it[Journeys.duration.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return averageDepartDuration
}

private fun getAverageReturnDurationForStation(id: Int, startDate: LocalDateTime?, endDate: LocalDateTime?): Float {
    var averageReturnDuration = 0.0F
    transaction(db) {
        val table = Journeys.slice(Journeys.duration.avg(1))
            .select {Journeys.return_station_id eq id}
            startDate?.let {
                table.andWhere { Journeys.departure_time greaterEq startDate }
            }
            endDate?.let {
                table.andWhere { Journeys.departure_time lessEq  endDate }
            }

            table.map {
                averageReturnDuration = it[Journeys.duration.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return averageReturnDuration
}

}

object Stations: Table() {
    val station_id: Column<Int> = integer("station_id").index()
    val name_fi: Column<String> = varchar("name_fi", 50).index()
    val name_se: Column<String> = varchar("name_se", 50)
    val name_en: Column<String> = varchar("name_en", 50)
    val address_fi: Column<String> = varchar("address_fi", 50).index()
    val address_se: Column<String> = varchar("address_se", 50)
    val city_fi: Column<String> = varchar("city_fi", 50)
    val city_se: Column<String> = varchar("city_se", 50)
    val operator: Column<String> = varchar("operator", 50)
    val capacity: Column<Int> = integer("capacity").index()
    val longitude: Column<String> = varchar("longitude", 50) // PostGIS?
    val latitude: Column<String> = varchar("latitude", 50) // PostGIS?

    override val primaryKey = PrimaryKey(station_id, name = "station_id")
}

object Journeys: Table() {
    val id = integer("id").autoIncrement()
    val departure_time = datetime("departure_time").index()
    val return_time = datetime("return_time").index()

    val departure_station_id = (integer("departure_station_id") references Stations.station_id).index()
    val departure_station_name = varchar("departure_station_name", 50).index()

    val return_station_id = (integer("return_station_id") references Stations.station_id).index()
    val return_station_name = varchar("return_station_name", 50).index()

    val distance = integer("distance").default(0).index()
    val duration = integer("duration").default(0).index()
}