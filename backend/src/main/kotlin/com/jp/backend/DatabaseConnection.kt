/**
 * This file includes a database connection Singleton,
 * and functions for interacting with the database.
 */

package com.jp.backend

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.kotlin.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp


object DatabaseConn {
	private val db: Database;

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

		// Check if the tables are already created
		// If not, create them and parse CSVs to database, else continue
		// TODO: Make work correctly
		if (tablesExist()) {
			transaction(db) {
				SchemaUtils.drop(TripsTable)
				SchemaUtils.drop(StationsTable)
			}
		}
		transaction(db) {
			SchemaUtils.create(TripsTable)
			SchemaUtils.create(StationsTable)
		}
	}

	// Function for returning data from tables
	// TODO: Add pagination and searching
	fun getStationsData(): ArrayList<StationModel> {
		var ret: ArrayList<StationModel> = arrayListOf()
		transaction(db) {
			for (station in StationsTable.selectAll()) {
				ret.add(StationModel(
					station[StationsTable.fid],
					station[StationsTable.id],
					station[StationsTable.name_fi],
					station[StationsTable.name_se],
					station[StationsTable.name_en],
					station[StationsTable.address_fi],
					station[StationsTable.address_se],
					station[StationsTable.city_fi],
					station[StationsTable.city_se],
					station[StationsTable.operator],
					station[StationsTable.capacity],
					station[StationsTable.longitude],
					station[StationsTable.latitude]
				))
			}
		}
		return ret
	}

	fun getPaginationStationsData(pageSize: Int, offset: Long, sortBy: String? = ""): ArrayList<StationModel> {
		var ret: ArrayList<StationModel> = arrayListOf()
		transaction(db) {
			for (station in StationsTable.selectAll().limit(pageSize, offset)) {
				ret.add(StationModel(
					station[StationsTable.fid],
					station[StationsTable.id],
					station[StationsTable.name_fi],
					station[StationsTable.name_se],
					station[StationsTable.name_en],
					station[StationsTable.address_fi],
					station[StationsTable.address_se],
					station[StationsTable.city_fi],
					station[StationsTable.city_se],
					station[StationsTable.operator],
					station[StationsTable.capacity],
					station[StationsTable.longitude],
					station[StationsTable.latitude]
				))
			}
		}
		return ret
	}

	// Function gets all trips from database.
	// TODO: Add pagination and searching
	fun getTripsData(): ArrayList<TripModel> {
		var ret: ArrayList<TripModel> = arrayListOf()
		transaction(db) {
			for (trip in TripsTable.selectAll()) {

				ret.add(TripModel(
						trip[TripsTable.departure_time].toString(),
						trip[TripsTable.return_time].toString(),
						trip[TripsTable.departure_station_id],
						trip[TripsTable.departure_station_name],
						trip[TripsTable.return_station_id],
						trip[TripsTable.return_station_name],
						trip[TripsTable.distance],
						trip[TripsTable.duration]
				))
			}
		}
		return ret
	}

	fun getPaginationTripsData(pageSize: Int, offset: Long): ArrayList<TripModel> {
		var ret: ArrayList<TripModel> = arrayListOf()

		transaction(db) {
			for (trip in TripsTable.selectAll().limit(pageSize, offset)) {
				ret.add(TripModel(
					trip[TripsTable.departure_time].toString(),
					trip[TripsTable.return_time].toString(),
					trip[TripsTable.departure_station_id],
					trip[TripsTable.departure_station_name],
					trip[TripsTable.return_station_id],
					trip[TripsTable.return_station_name],
					trip[TripsTable.distance],
					trip[TripsTable.duration]
				))
			}
		}
		return ret
	}
	// Function for checking if tables already exist.
	fun tablesExist(): Boolean {
		var ret = true
		transaction(db) {
			if (TripsTable.selectAll().count() > 0) {ret = false}
		}
		return ret
	}

	// Functions for inserting data into database
	fun insertIntoStations(station: StationModel) {
		transaction(db) {
			StationsTable.insert {
				it[fid] = station.fid
				it[id] = station.id
				it[name_fi] = station.nameFi
				it[name_se] = station.nameSe
				it[name_en] = station.nameEn
				it[address_fi] = station.addressFi
				it[address_se] = station.addressSe
				it[city_fi] = station.cityFi
				it[city_se] = station.citySe
				it[operator] = station.operator
				it[capacity] = station.capacity
				it[longitude] = station.longitude
				it[latitude] = station.latitude
			}
		}
	}

	fun insertIntoTrips(trip: TripModel) {
		/**
		 *  As documented in the DataModels.kt TripModel declaration,
		 *  datetime-strings are converted to LocalDateTimes before
		 *  insertion into database
		 */
		val depTime = trip.departureTime.toLocalDateTime()
		val retTime = trip.returnTime.toLocalDateTime()

		transaction(db) {
			TripsTable.insert {
				it[departure_time] = depTime
				it[return_time] = retTime
				it[departure_station_id] = trip.departureStationId
				it[departure_station_name] = trip.departureStationName
				it[return_station_id] = trip.returnStationId
				it[return_station_name] = trip.returnStationName
				it[distance] = trip.distanceCovered
				it[duration] = trip.durationSeconds
			}
		}
	}

	// Function for deleting all data
	fun deleteData() {
		transaction(db) {
			TripsTable.deleteAll()
			StationsTable.deleteAll()
		}
	}

	// Function for modifying data


}

object StationsTable : Table() {
	val fid: Column<Int> = integer("fid")
	val id: Column<Int> = integer("id")
	val name_fi: Column<String> = varchar("name_fi", 50)
	val name_se: Column<String> = varchar("name_se", 50)
	val name_en: Column<String> = varchar("name_en", 50)
	val address_fi: Column<String> = varchar("address_fi", 50)
	val address_se: Column<String> = varchar("address_se", 50)
	val city_fi: Column<String> = varchar("city_fi", 50)
	val city_se: Column<String> = varchar("city_se", 50)
	val operator: Column<String> = varchar("operator", 50)
	val capacity: Column<Int> = integer("capacity")
	val longitude: Column<String> = varchar("longitude", 50) // PostGIS?
	val latitude: Column<String> = varchar("latitude", 50) // PostGIS?

	override val primaryKey = PrimaryKey(fid, name = "STATIONS_ID")
}

object TripsTable : Table() {
	val departure_time = datetime("departure_time")
	val return_time = datetime("return_time")
	val departure_station_id = integer("departure_station_id")
	val departure_station_name = varchar("departure_station_name", 50)
	val return_station_id = integer("return_station_id")
	val return_station_name = varchar("return_station_name", 50)
	val distance = integer("distance")
	val duration = integer("duration")
}