package com.jp.backend.databaseConn

import com.jp.backend.JourneyAPIResult
import com.jp.backend.JourneyModelWithStationData
import com.jp.backend.databaseConn.QueryHelpers.formOrderBy
import com.jp.backend.databaseConn.QueryHelpers.validateDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object JourneyQueries {
    fun getPaginationJourneysData(
        pageSize: Int,
        offset: Long,
        sortBy: String? = "",
        sortDesc: Boolean?,
        search: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        minDistance: Int? = null,
        maxDistance: Int? = null,
        minDuration: Int? = null,
        maxDuration: Int? = null
    ): JourneyAPIResult {
        val ret = JourneyAPIResult(0, arrayListOf())
        val orderBy = formOrderBy(sortBy, sortDesc)
        val departureStationsTable = Stations.alias("st1")
        val returnStationsTable = Stations.alias("st2")
        val timestampStartDate = validateDate(startDate, toEndOfDay = false)
        val timestampEndDate = validateDate(endDate, toEndOfDay = true)


        transaction(DatabaseConn.db) {
            var journeys = Journeys
                .innerJoin(
                    departureStationsTable,
                    { departure_station_id },
                    { departureStationsTable[Stations.station_id] })
                .innerJoin(
                    returnStationsTable,
                    { Journeys.return_station_id },
                    { returnStationsTable[Stations.station_id] })
                .selectAll()
            search?.let {
                journeys.andWhere { (Journeys.departure_station_name like ("$search%")) or (Journeys.return_station_name like ("$search%")) }
            }
            timestampStartDate?.let {
                journeys.andWhere { Journeys.departure_time greaterEq timestampStartDate }
            }
            timestampEndDate?.let {
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
                ret.journeys.add(
                    JourneyModelWithStationData(
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
                    )
                )
            }
        }
        return ret
    }

    fun getJourneyCount(): Long {
        var ret: Long = 0
        transaction(DatabaseConn.db) {
            ret = Journeys.selectAll().count()
        }

        return ret
    }
}