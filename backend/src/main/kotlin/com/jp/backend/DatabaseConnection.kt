package com.jp.backend

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
// This file includes a database connection Singleton,
// And 

object stationsSchema : Table() {
	val fid: Column<String> = varchar("fid", 50) // Int?
	val id: Column<String> = varchar("id", 50) // Int?
	val name_fi: Column<String> = varchar("name_fi", 50)
	val name_se: Column<String> = varchar("name_se", 50)
	val name_en: Column<String> = varchar("name_en", 50)
	val address_fi: Column<String> = varchar("address_fi", 50)
	val address_se: Column<String> = varchar("address_se", 50)
	val city_fi: Column<String> = varchar("city_fi", 50)
	val city_se: Column<String> = varchar("city_se", 50)
	val operator: Column<String> = varchar("operator", 50)
	val capacity: Column<String> = varchar("capacity", 50) // Int?
	val longitude: Column<String> = varchar("longitude", 50) // PostGIS?
	val latitude: Column<String> = varchar("latitude", 50) // PostGIS?

	override val primaryKey = PrimaryKey(fid, name = "STATIONS_ID")
}

object tripsSchema : Table() {}

object testi : Table() {
	val name: Column<String> = varchar("name", 40)
	val age: Column<Int> = integer("age")
}

object DatabaseConn {
	val db: Database;

	// Initialize database connection. 
	init {
		// If Docker
		if (System.getenv("DB_URL") == "null") {
			db = Database.connect(
				System.getenv("DB_URL"),
				driver = "org.postgresql.Driver",
				user = System.getenv("DB_USERNAME"),
				password = System.getenv("DB_PASSWORD"))
		} else { // If no system environment vars, for example running without docker
				db = Database.connect(
				"jdbc:postgresql://localhost:5432/postgres",
				driver = "org.postgresql.Driver",
				user = "postgres",
				password = "password")
		}
	}


	// Test function for development
	fun testFunc() {
		transaction(db) {
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
	}
	// Function for returning data
	fun getData(): String {
		var ret: String = "test"
		transaction(db) {

			for (user in testi.selectAll()) {
				ret = ("${user[testi.name]}: ${user[testi.age]}")
			}
		}
		return ret
	}
	// Function for checking if tables already exist

	// Function for inserting data into db

	// Function for deleting data

	// Function for modifying data


}