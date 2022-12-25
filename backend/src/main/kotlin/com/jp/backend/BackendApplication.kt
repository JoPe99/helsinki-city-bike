package com.jp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.jp.backend.DatabaseConn

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	DatabaseConn
	runApplication<BackendApplication>(*args)
}
