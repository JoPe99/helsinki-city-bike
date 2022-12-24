package com.jp.backend

import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.jp.backend.*
import com.jp.backend.CSVParser

// TODO: Correct responses to queries
@CrossOrigin(maxAge = 3600)
@RestController
class RESTController(
) {
    @GetMapping("/", produces = [MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun helloWorld(): ResponseEntity<Any?> {
        return ResponseEntity("Hello World", HttpStatus.OK)
    }

    @GetMapping("/stations")
    @ResponseBody
    fun getStations(): ResponseEntity<Any?> {
        return ResponseEntity(JsonCreator.stationsToJSON(), HttpStatus.OK)
    }

    @GetMapping("/trips")
    @ResponseBody
    fun getTrips(): ResponseEntity<Any?> {
        return ResponseEntity(JsonCreator.tripsToJSON(), HttpStatus.OK)
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

    @DeleteMapping("/delete")
    fun deleteAll(): ResponseEntity<Any?> {
        DatabaseConn.deleteData()
        return ResponseEntity("Tables emptied", HttpStatus.OK)
    }
}