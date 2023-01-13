package com.jp.backend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.jp.backend.*
import com.jp.backend.DatabaseConn.insertIntoJourneys
import com.jp.backend.DatabaseConn.insertIntoStations
import com.jp.backend.ValidationHelpers.updateStationIDs
import com.jp.backend.ValidationHelpers.validateJourneyInsertFromApp
import com.jp.backend.ValidationHelpers.validateStationInsertFromApp

// TODO: Add correct responses to queries, errors etc
@CrossOrigin(maxAge = 3600)
@RestController
class RESTController() {

    @GetMapping("/stations/all")
    @ResponseBody
    fun getStations(): ResponseEntity<Any?> {
        return ResponseEntity(JsonCreator.stationsToJSON(DatabaseConn.getStationsData()), HttpStatus.OK)
    }

    @GetMapping("/stations")
    @ResponseBody
    fun getPaginatedStations(@RequestParam pageSize: Int,
                             @RequestParam offset: Long,
                             @RequestParam sortBy: String?,
                             @RequestParam sortDesc: Boolean?,
                             @RequestParam search: String?): ResponseEntity<Any?> {
        val stationData = DatabaseConn.getPaginationStationsData(pageSize, offset, sortBy, sortDesc, search)
        return ResponseEntity(JsonCreator.stationsToJSON(stationData), HttpStatus.OK)
    }

    @GetMapping("/stations/{id}")
    @ResponseBody
    fun getSingleStationDetailed(@PathVariable id: Int, @RequestParam startDate: String?, @RequestParam endDate: String?): ResponseEntity<Any?> {
        val stationData = DatabaseConn.getSingleStationData(id, startDate, endDate)
        return if (stationData == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(JsonCreator.stationDetailsToJSON(stationData), HttpStatus.OK)
        }
    }


    @GetMapping("/journeys")
    @ResponseBody
    fun getPaginatedJourneys(@RequestParam pageSize: Int,
                             @RequestParam offset: Long,
                             @RequestParam sortBy: String?,
                             @RequestParam sortDesc: Boolean?,
                             @RequestParam search: String?,
                             @RequestParam startDate: String?,
                             @RequestParam endDate: String?,
                             @RequestParam minDistance: Int?,
                             @RequestParam maxDistance: Int?,
                             @RequestParam minDuration: Int?,
                             @RequestParam maxDuration: Int?): ResponseEntity<Any?> {
        val journeyData = DatabaseConn.getPaginationJourneysData(pageSize, offset, sortBy, sortDesc, search, startDate, endDate, minDistance, maxDistance, minDuration, maxDuration)
        return ResponseEntity(JsonCreator.journeysToJSON(journeyData), HttpStatus.OK)
    }

    @GetMapping("/journeys/count")
    @ResponseBody
    fun getJourneysCount(): ResponseEntity<Any?> {
        val journeyCount = DatabaseConn.getJourneyCount()
        return ResponseEntity(journeyCount, HttpStatus.OK)
    }

    @GetMapping("/journeys/longestDistance")
    @ResponseBody
    fun getLongestDistanceJourney(): ResponseEntity<Any?> {
        val journeyData = DatabaseConn.getPaginationJourneysData(1, 0, "distance", true)
        return ResponseEntity(JsonCreator.journeysToJSON(journeyData), HttpStatus.OK)
    }

    @GetMapping("/journeys/longestDuration")
    @ResponseBody
    fun getLongestDurationJourney(): ResponseEntity<Any?> {
        val journeyData = DatabaseConn.getPaginationJourneysData(1, 0, "duration", true)
        return ResponseEntity(JsonCreator.journeysToJSON(journeyData), HttpStatus.OK)
    }

    @GetMapping("/journeys/earliest")
    @ResponseBody
    fun getEarliestJourney(): ResponseEntity<Any?> {
        val journeyData = DatabaseConn.getPaginationJourneysData(1, 0, "departure_time", false)
        return ResponseEntity(JsonCreator.journeysToJSON(journeyData), HttpStatus.OK)
    }

    @GetMapping("/journeys/latest")
    @ResponseBody
    fun getLatestJourney(): ResponseEntity<Any?> {
        val journeyData = DatabaseConn.getPaginationJourneysData(1, 0, "departure_time", true)
        return ResponseEntity(JsonCreator.journeysToJSON(journeyData), HttpStatus.OK)
    }

    @PostMapping("/parse/stations")
    fun parseStations(): ResponseEntity<String> {
        CSVParser.parseStationData()
        return ResponseEntity("Stations parsed", HttpStatus.OK)
    }

    @PostMapping("/parse/journeys")
    fun parseJourneys(): ResponseEntity<String> {
        CSVParser.parseJourneyData()
        return ResponseEntity("Journeys parsed", HttpStatus.OK)
    }

    @PostMapping("/insert/journey")
    fun insertJourney(@RequestBody journey: JourneyModel): ResponseEntity<String> {
        return if (validateJourneyInsertFromApp(journey)) {
            insertIntoJourneys(arrayListOf(journey))
            ResponseEntity("Journey created", HttpStatus.OK)
        } else {
            ResponseEntity("Journey failed validation", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/insert/station")
    fun insertStation(@RequestBody station: StationModel): ResponseEntity<String> {
        return if (validateStationInsertFromApp(station)) {
            insertIntoStations(arrayListOf(station))
            ResponseEntity("Station created", HttpStatus.OK)
        } else {
            ResponseEntity("Station failed validation", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/create")
    fun createTables(): ResponseEntity<Any?> {
        DatabaseConn.createTables()
        return ResponseEntity("Tables created", HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteTables(): ResponseEntity<Any?> {
        DatabaseConn.deleteTables()
        updateStationIDs()
        return ResponseEntity("Tables destroyed", HttpStatus.OK)
    }
}