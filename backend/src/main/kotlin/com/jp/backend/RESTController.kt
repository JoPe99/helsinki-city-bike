package com.jp.backend

import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.jp.backend.DatabaseConn

@CrossOrigin(maxAge = 3600)
@RestController
class RESTController(
) {
    @GetMapping("/", produces = [MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun helloWorld(): ResponseEntity<Any?> {
        return ResponseEntity("Hello World", HttpStatus.OK)
    }

    @GetMapping("/testi")
    @ResponseBody
    fun helloWorldTesti(): ResponseEntity<Any?> {
        return ResponseEntity(DatabaseConn.getData(), HttpStatus.OK)
    }
}