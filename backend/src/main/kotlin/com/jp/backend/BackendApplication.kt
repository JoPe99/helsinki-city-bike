package com.jp.backend

import com.jp.backend.databaseConn.DatabaseConn
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    // Init DB and validation helpers
    DatabaseConn
    ValidationHelpers

    runApplication<BackendApplication>(*args)
}
