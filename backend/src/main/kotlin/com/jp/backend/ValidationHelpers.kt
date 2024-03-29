/**
 * This file includes functions for validating CSV data and
 * data from frontend insert feature
 */

package com.jp.backend

import com.jp.backend.databaseConn.QueryHelpers.tablesExist
import com.jp.backend.databaseConn.StationsQueries.getAllStationIDs
import kotlin.reflect.full.memberProperties

object ValidationHelpers {

    // Contains all station ids in the Stations CSV/Database.
    // Is used to reject journeys with departure/return id outside
    // declared stations.
    private var usedStationIDs: ArrayList<Int> = arrayListOf()

    init {
        updateStationIDs()
    }

    /**
     * Updates station IDs of ValidationHelpers
     */
    fun updateStationIDs() {
        if (tablesExist()) {
            println("Updating allowed station IDs")
            usedStationIDs = getAllStationIDs()
            println(usedStationIDs)
        } else {
            usedStationIDs = arrayListOf()
        }
    }

    fun validateJourneyInsertFromApp(journey: JourneyModel): Boolean {

        // Timestamp should be "YYYY-MM-DD'T'HH:MM:SS" e.g. "2021-05-31T23:50:19"
        for (dateTime in arrayListOf(journey.departureTime, journey.returnTime)) {
            // Use RegExp to check for correct format
            val regex = Regex(pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]{1,3})?")
            if (!regex.matches(dateTime)) {
                return false
            }
        }

        // If covered distance under 10 metres, journey is not included.
        if (journey.distanceCovered < 10) {
            return false
        }

        // If journey length is under 10 seconds, journey is not included.
        if (journey.durationSeconds < 10) {
            return false
        }

        // Reject if departure or return station id is not in the allowed list of station ids.
        if (!usedStationIDs.contains(journey.departureStationId) ||
            !usedStationIDs.contains(journey.returnStationId)
        ) {
            return false
        }

        // If passes all tests
        return true
    }

    fun validateStationInsertFromApp(station: StationModel): Boolean {
        var valid = true
        val stringKeysToCheck = arrayListOf(
            "nameFi", "nameSe", "nameEn", "addressFi",
            "addressSe", "cityFi", "citySe",
            "operator", "longitude", "latitude"
        )

        if (usedStationIDs.contains(station.id)) {
            return false
        }

        // Check station string properties to be under 50 chars
        for (key in StationModel::class.memberProperties) {
            if (stringKeysToCheck.contains(key.name) && key.get(station).toString().length > 50) {
                valid = false
                break
            }
        }
        return valid
    }

    fun validateJourneyCSVRow(row: Map<String, String>): Boolean {
        // Simplify testing by grouping values by test types
        val timestampTests: Array<String> = arrayOf("Departure", "Return")
        val intTests = arrayOf("Departure station id", "Return station id", "Covered distance (m)", "Duration (sec.)")

        // Timestamp should be "YYYY-MM-DD'T'HH:MM:SS" e.g. "2021-05-31T23:50:19"
        for (key in timestampTests) {
            var timestamp = row.getValue(key)
            // Use RegExp to check for correct format
            val regex = Regex(pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]{1,3})?")
            if (!regex.matches(timestamp)) {
                return false
            }
        }

        for (key in intTests) {
            var value = row.getValue(key)

            // Check that number can be converted to int, else return false
            try {
                value.toInt()
            } catch (e: Exception) {
                return false
            }

            // If covered distance under 10 metres, journey is not included.
            if (key == "Covered distance (m)" && value.toInt() < 10) {
                //println("Rejected ${value.toInt()}")
                return false
            }

            // If journey length is under 10 seconds, journey is not included.
            if (key == "Duration (sec.)" && value.toInt() < 10) {
                return false
            }

            // Reject if departure or return station id is not in the allowed list of station ids.
            if (key == "Departure station id" || key == "Return station id") {
                var id = row.getValue(key).toInt()
                if (!usedStationIDs.contains(id)) {
                    return false
                }
            }
        }

        // If passes all tests
        return true
    }
}