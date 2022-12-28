package com.jp.backend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.jp.backend.*

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
                             @RequestParam search: String?): ResponseEntity<Any?> {
        val stationData = DatabaseConn.getPaginationStationsData(pageSize, offset, sortBy, search)
        return ResponseEntity(JsonCreator.stationsToJSON(stationData), HttpStatus.OK)
    }

    @GetMapping("/stations/{id}")
    @ResponseBody
    fun getPaginatedStations(@PathVariable id: Int): ResponseEntity<Any?> {
        val stationData = DatabaseConn.getSingleStationData(id)
        return if (stationData == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(JsonCreator.stationDetailsToJSON(stationData), HttpStatus.OK)
        }
    }


    @GetMapping("/trips")
    @ResponseBody
    fun getPaginatedTrips(@RequestParam pageSize: Int, @RequestParam offset: Long, @RequestParam sortBy: String?): ResponseEntity<Any?> {
        val tripData = DatabaseConn.getPaginationTripsData(pageSize, offset)
        return ResponseEntity(JsonCreator.tripsToJSON(tripData), HttpStatus.OK)
    }

    @GetMapping("/trips/count")
    @ResponseBody
    fun getTripsCount(): ResponseEntity<Any?> {
        val tripsCount = DatabaseConn.getTripsCount()
        return ResponseEntity(tripsCount, HttpStatus.OK)
    }

    // TODO: Add inserts for stations and trips

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