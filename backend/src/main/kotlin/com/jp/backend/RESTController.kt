package com.jp.backend

import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.jp.backend.*
import com.jp.backend.CSVParser

// TODO: Add correct responses to queries, e.g errors etc
@CrossOrigin(maxAge = 3600)
@RestController
class RESTController() {
    @GetMapping("/", produces = [MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun helloWorld(): ResponseEntity<Any?> {
        return ResponseEntity("Hello World", HttpStatus.OK)
    }

    @GetMapping("/stations/all")
    @ResponseBody
    fun getStations(): ResponseEntity<Any?> {
        return ResponseEntity(JsonCreator.stationsToJSON(DatabaseConn.getStationsData()), HttpStatus.OK)
    }

    @GetMapping("/stations")
    @ResponseBody
    fun getPaginatedStations(@RequestParam pageSize: Int, @RequestParam offset: Long, @RequestParam sortBy: String?): ResponseEntity<Any?> {
        val stationData = DatabaseConn.getPaginationStationsData(pageSize, offset)
        return ResponseEntity(JsonCreator.stationsToJSON(stationData), HttpStatus.OK)
    }

    @GetMapping("/trips/all")
    @ResponseBody
    fun getTrips(): ResponseEntity<Any?> {
        return ResponseEntity(JsonCreator.tripsToJSON(DatabaseConn.getTripsData()), HttpStatus.OK)
    }

    @GetMapping("/trips")
    @ResponseBody
    fun getPaginatedTrips(@RequestParam pageSize: Int, @RequestParam offset: Long, @RequestParam sortBy: String?): ResponseEntity<Any?> {
        val tripData = DatabaseConn.getPaginationTripsData(pageSize, offset)
        return ResponseEntity(JsonCreator.tripsToJSON(tripData), HttpStatus.OK)
    }

    @PostMapping("/parse/stations")
    fun parseStations(): ResponseEntity<String> {
        CSVParser.parseStationData()
        return ResponseEntity("Stations parsed", HttpStatus.OK)
    }

    @PostMapping("/parse/trips")
    fun parseTrips(): ResponseEntity<String> {
        CSVParser.parseTripData()
        return ResponseEntity("Trips parsed", HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createTables(): ResponseEntity<Any?> {
        DatabaseConn.createTables()
        return ResponseEntity("Tables created", HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteTables(): ResponseEntity<Any?> {
        DatabaseConn.deleteTables()
        return ResponseEntity("Tables destroyed", HttpStatus.OK)
    }
}