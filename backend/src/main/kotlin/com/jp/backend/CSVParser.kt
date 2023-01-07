/**
 * This file includes helper functions for parsing the CSV files,
 * and populating the database with the functions
 */

package com.jp.backend

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.jp.backend.DatabaseConn.insertIntoStations
import com.jp.backend.DatabaseConn.insertIntoJourneys
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object CSVParser {

    // Contains all station ids found in the stations CSV.
    // Is used to reject journeys with departure/return id outside
    // declared stations.
    private var allowedStationIDs: ArrayList<Int> = arrayListOf()

    fun parseStationData() {
        var stationArray: ArrayList<StationModel> = arrayListOf()
        csvReader().open("src/main/resources/data/stations.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                if (validateStationData(row)) { stationArray.add(stationRowToModel(row)) }
            }
        }

        insertIntoStations(stationArray)
        println("Stations parsed into database")
    }

    // TODO: Parse all files concurrently
    fun parseJourneyData() {
        var journeyArray: ArrayList<JourneyModel> = arrayListOf()
        var firstRow: Map<String, String> = mapOf()
        var rowCounter: Long = 0

        // TODO: Get file location from params
        var filenames = arrayListOf(
            "src/main/resources/data/05_2021.csv",
            "src/main/resources/data/06_2021.csv",
            "src/main/resources/data/07_2021.csv")

        for (filename in filenames) { parseCSV(filename) }

    }

    private fun parseCSV(pathToFile: String) {
        println("Starting parsing from $pathToFile")
        var journeyArray: ArrayList<JourneyModel> = arrayListOf()
        var firstRow: Map<String, String> = mapOf()
        var rowCounter: Long = 0

       /* Seems like the files from May, June & July have duplicate data.
        * The data begins repeating from start halfway of the file.
        * To not parse the duplicates, we stop the parsing when the loop
        * hits the first duplicate row. To make sure we don't stop parsing
        * in the extremely unlikely event that the first rows happen to be
        * exactly the same, checks are started doing after 5 rows.
        */
        csvReader().open(pathToFile) {
            run parsing@{
                readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                    if (firstRow.isEmpty()) {
                        firstRow = row
                    } else if (row == firstRow && rowCounter > 5) return@parsing

                    rowCounter++

                    // If journey data is valid, format to JourneyModel and add to array to be returned
                    if (validateJourneyData(row)) {
                        journeyArray.add(journeyRowToModel(row))
                    }
                }
            }
        }
        insertIntoJourneys(journeyArray)
        println("CSV from $pathToFile parsed into database")
    }

    // TODO: Tests
    private fun validateStationData(row: Map<String, String>): Boolean {
        // Add station id to allowed ids
        allowedStationIDs.add(row.getValue("ID").toInt())
        return true
    }

    // TODO: Tests
    private fun validateJourneyData(row: Map<String, String>): Boolean {
        // Simplify testing by grouping values by test types
        val timestampTests: Array<String> = arrayOf("Departure", "Return")
        val intTests = arrayOf("Departure station id", "Return station id", "Covered distance (m)", "Duration (sec.)")

        // Timestamp should be "YYYY-MM-DD'T'HH:MM:SS" e.g. "2021-05-31T23:50:19"
        for (key in timestampTests) {
            var timestamp = row.getValue(key)
            // Use RegExp to check for correct format // TODO: Make better?
            val regex = Regex(pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]{1,3})?")
            if (!regex.matches(timestamp)) { return false }
        }

        for (key in intTests) {
            var value = row.getValue(key)

            // Check that number can be converted to int, else return false
            try {value.toInt()} catch (e: Exception) { return false }

            // If covered distance under 10 metres, journey is not included.
            if (key == "Covered distance (m)" && value.toInt() < 10) {
                //println("Rejected ${value.toInt()}")
                return false
            }

            // If journey length is under 10 seconds, journey is not included.
            if (key == "Duration (sec.)" && value.toInt() < 10) { return false }

            // Reject if departure or return station id is not in the allowed list of station ids.
            if (key == "Departure station id" || key == "Return station id") {
                var id = row.getValue(key).toInt()
                if (!allowedStationIDs.contains(id)) {return false}
            }
        }

        // If passes all tests
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

    // TODO: Tests & cleanup
    private fun journeyRowToModel(row: Map<String, String>): JourneyModel {
        return JourneyModel(
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