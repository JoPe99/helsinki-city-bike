package com.jp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.jp.backend.DatabaseConn

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {

	DatabaseConn.testFunc();
	// Check if the tables are already created

	// If not, create them and parse CSVs to database, else continue
	
	// Start RESTController
	

	runApplication<BackendApplication>(*args)
}
