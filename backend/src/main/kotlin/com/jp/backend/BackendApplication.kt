package com.jp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	DatabaseConn
	ValidationHelpers
	runApplication<BackendApplication>(*args)
}
