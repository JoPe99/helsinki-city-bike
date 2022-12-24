/**
 * This file includes helper functions for parsing the CSV files,
 * and populating the database with the functions
 */

package com.jp.backend

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.jp.backend.DatabaseConn.insertIntoStations
import com.jp.backend.DatabaseConn.insertIntoTrips


object CSVParser {
    fun parseStationData() {
        csvReader().open("src/main/resources/data/stations.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                // TODO: Validate station data
                insertIntoStations(stationRowToModel(row))
            }
        }
    }

    fun parseTripData() {
        csvReader().open("src/main/resources/data/trips_1.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                // TODO: Validate trip data
                insertIntoTrips(tripRowToModel(row))
            }
        }
    }

    // TODO: Tests
    private fun stationRowToModel(row: Map<String, String>): StationModel {
        return StationModel(
            row.getValue("FID").toInt(),
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