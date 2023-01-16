package com.jp.backend

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParsingTests {

    /**
     * Starting from empty database.
     */
    @BeforeAll
    fun init() {
        if (DatabaseConn.tablesExist()) {
            DatabaseConn.deleteTables()
        }

        DatabaseConn.createTables()

        val pathToStationTestCSV = "src/test/kotlin/resources/valid_stations.tcsv"
        val stationArray = CSVParser.parseStationCSV(pathToStationTestCSV)
        if (stationArray.size == 4) {
            DatabaseConn.insertIntoStations(stationArray)
            ValidationHelpers.updateStationIDs()
            assertTrue(true)
        } else {
            assertTrue(false)
        }
    }

    @AfterAll
    fun clean() {
        DatabaseConn.deleteTables()
    }


    /**
     * Tests parsing and row validation with CSV file
     * of 5 valid journeys.
     */
    @Test
    @DisplayName("Parses valid journeys correctly")
    fun parseAndValidateJourneys() {
        val pathToJourneyTestCSV = "src/test/kotlin/resources/valid_journeys.tcsv"
        val journeyArray = CSVParser.parseJourneyCSV(pathToJourneyTestCSV)

        // 5 journeys in the resulting array
        assertTrue(journeyArray.size == 5)
    }

    /**
     * Tests parsing and row validation with CSV file
     * of 7 invalid journeys.
     * Reasons for invalidation:
     * Departure timestamp not in correct format
     * Return timestamp not in correct format
     * Duration under 10
     * Distance under 10
     * Couldn't convert IDs, distance or duration to Int
     * Departure station ID not in valid stations
     * Return station ID not in valid stations
     */
    @Test
    @DisplayName("Rejects invalid parsed journeys")
    fun parseAndValidateInvalidJourneys() {
        val pathToJourneyTestCSV = "src/test/kotlin/resources/invalid_journeys.tcsv"
        val journeyArray = CSVParser.parseJourneyCSV(pathToJourneyTestCSV)

        // 0 journeys passed validation
        assertTrue(journeyArray.size == 0)
    }
}