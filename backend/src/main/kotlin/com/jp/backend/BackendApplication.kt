package com.jp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.jp.backend.CSVParser.*

@SpringBootApplication
class BackendApplication

object trips : Table() {

}

object stations : Table() {
	val fid: Column<String> = varchar("fid", 50)
	val id: Column<String> = varchar("id", 50)
	val name_fi: Column<String> = varchar("name_fi", 50)
	val name_se: Column<String> = varchar("name_se", 50)
	val name_en: Column<String> = varchar("name_en", 50)
	val address_fi: Column<String> = varchar("address_fi", 50)
	val address_se: Column<String> = varchar("address_se", 50)
	val city_fi: Column<String> = varchar("city_fi", 50)
	val city_se: Column<String> = varchar("city_se", 50)
	val operator: Column<String> = varchar("operator", 50)
	val capacity: Column<String> = varchar("capacity", 50)
	val longitude: Column<String> = varchar("longitude", 50)
	val latitude: Column<String> = varchar("latitude", 50)

	override val primaryKey = PrimaryKey(fid, name = "STATIONS_ID")
}

object testi : Table() {
	val name: Column<String> = varchar("name", 40)
	val age: Column<Int> = integer("age")
}

fun main(args: Array<String>) {


	Database.connect(
		System.getenv("db_url"),
		driver = "org.postgresql.Driver",
		user = "postgres",
		password = "password")

	transaction {
		//addLogger(StdOutSqlLogger)

		SchemaUtils.create(testi)

		var x: Int = 1

		while (x < 1000) {
			testi.insert {
				it[name] = "Jarkko"
				it[age] = 45
			}
			x++
		}

		println("All users: ")

		for (user in testi.selectAll()) {
			println("${user[testi.name]}: ${user[testi.age]}")
		}
	}

	runApplication<BackendApplication>(*args)
}
