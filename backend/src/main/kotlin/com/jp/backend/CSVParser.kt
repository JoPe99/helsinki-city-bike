/**
 * This file includes functions for parsing the CSV files
 */

package com.jp.backend

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.jp.backend.DatabaseConn.insertIntoStations
import com.jp.backend.DatabaseConn.insertIntoJourneys
import com.jp.backend.ValidationHelpers.validateJourneyCSVRow


object CSVParser {

    fun parseStationData() {
        var stationArray: ArrayList<StationModel> = arrayListOf()
        csvReader().open("src/main/resources/data/stations.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                 stationArray.add(stationRowToModel(row))
            }
        }

        insertIntoStations(stationArray)
        println("Stations parsed into database")
    }

    // TODO: Parse journey files concurrently
    fun parseJourneyData() {

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
                    if (validateJourneyCSVRow(row)) {
                        journeyArray.add(journeyRowToModel(row))
                    }
                }
            }
        }
        insertIntoJourneys(journeyArray)
        println("CSV from $pathToFile parsed into database")
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