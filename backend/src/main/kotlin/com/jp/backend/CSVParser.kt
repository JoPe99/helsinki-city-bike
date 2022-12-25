/**
 * This file includes helper functions for parsing the CSV files,
 * and populating the database with the functions
 */

package com.jp.backend

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.jp.backend.DatabaseConn.insertIntoStations
import com.jp.backend.DatabaseConn.insertIntoTrips
import kotlinx.datetime.toLocalDateTime


object CSVParser {
    fun parseStationData() {
        var stationArray: ArrayList<StationModel> = arrayListOf()
        csvReader().open("src/main/resources/data/stations.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                if (validateStationData(row)) { stationArray.add(stationRowToModel(row)) }
            }
        }

        insertIntoStations(stationArray)
    }

    fun parseTripData() {
        var tripArray: ArrayList<TripModel> = arrayListOf()

        csvReader().open("src/main/resources/data/05_2021.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                if (validateTripData(row)) { tripArray.add(tripRowToModel(row))}
            }
        }

        insertIntoTrips(tripArray)
    }

    private fun validateStationData(row: Map<String, String>): Boolean {
        return true
    }

    fun validateTripData(row: Map<String, String>): Boolean {
        // Simplify testing by grouping values by test types
        val timestampTests: Array<String> = arrayOf("Departure", "Return")
        val stringTests: Array<String> = arrayOf("Departure station name", "Return station name")
        val intTests = arrayOf("Departure station id", "Return station id", "Covered distance (m)", "Duration (sec.)")

        // Timestamp should be "YYYY-MM-DD'T'HH:MM:SS" e.g. "2021-05-31T23:50:19"
        for (key in timestampTests) {
            var timestamp = row.getValue(key)
            // Use RegExp to check for correct format
            val regex = Regex(pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]{1,3})?")
            if (!regex.matches(timestamp)) { return false }
        }

        // Database can store strings up the length of 50, don't allow higher
        // TODO: Nosta lukua?
        for (key in stringTests) {
            if (row.getValue(key).length > 50) { return false }
        }

        for (key in intTests) {
            var value = row.getValue(key)

            // Check that number can be converted to int, else return false
            try {value.toInt()} catch (e: Exception) { return false }

            // If covered distance under 10 metres, trip is not included.
            if (key == "Covered distance (m)" && value.toInt() < 10) { return false }

            // If trip length is under 10 seconds, trip is not included.
            if (key == "Duration (sec.)" && value.toInt() < 10) { return false }

        }

        // Return true if passes all tests
        return true
    }

    // TODO: Tests
    private fun stationRowToModel(row: Map<String, String>): StationModel {
        return StationModel(
            row.getValue("ID").toInt(),
            row.getValue("Nimi"),
            row.getValue("Namn"),
            row.getValue("Name"),
            row.getValue("Osoite"),
            row.getValue("Adress"),
            row.getValue("Kaupunki"),
            row.getValue("Stad"),
            row.getValue("Operaattor"),
            row.getValue("Kapasiteet").toInt(),
            row.getValue("x"),
            row.getValue("y")
        )
    }

    // TODO: Tests
    private fun tripRowToModel(row: Map<String, String>): TripModel {
        return TripModel(
                row.getValue("Departure"),
                row.getValue("Return"),
                row.getValue("Departure station id").toInt(),
                row.getValue("Departure station name"),
                row.getValue("Return station id").toInt(),
                row.getValue("Return station name"),
                row.getValue("Covered distance (m)").toInt(),
                row.getValue("Duration (sec.)").toInt()
        )
    }

}