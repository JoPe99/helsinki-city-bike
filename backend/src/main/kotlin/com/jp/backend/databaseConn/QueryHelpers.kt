package com.jp.backend.databaseConn

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object QueryHelpers {
    /**
     * tablesExist tries to get a count from both tables, and returns false
     * if the query results in an exception. This happens when the tables
     * are not yet created.
     */
    fun tablesExist(): Boolean {
        var ret = true
        transaction(DatabaseConn.db) {
            try {
                Journeys.selectAll().count()
            } catch (e: Exception) {
                ret = false
            }
            try {
                Stations.selectAll().count()
            } catch (e: Exception) {
                ret = false
            }
        }
        return ret
    }


    /**
     * Validates date string, and parses it to the ISO 8601 format.
     */
    fun validateDate(date: String?, toEndOfDay: Boolean): LocalDateTime? {
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

    fun formOrderBy(sortBy: String?, sortDesc: Boolean?): Pair<Column<*>, SortOrder> {
        var sort: SortOrder = SortOrder.ASC
        if (sortDesc == true) {
            sort = SortOrder.DESC
        }
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

}