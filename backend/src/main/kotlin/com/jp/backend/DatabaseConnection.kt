/**
 * This file includes a database connection Singleton,
 * and functions for interacting with the database.
 */

package com.jp.backend

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import kotlinx.datetime.toLocalDateTime

object DatabaseConn {
	private val db: Database = if (System.getenv("DB_URL") == "null") {
			Database.connect(
				System.getenv("DB_URL"),
				driver = "org.postgresql.Driver",
				user = System.getenv("DB_USERNAME"),
				password = System.getenv("DB_PASSWORD"))
		} else { // If no system environment vars, for example running without docker
			Database.connect(
				"jdbc:postgresql://localhost:5432/postgres?reWriteBatchedInserts=true",
				driver = "org.postgresql.Driver",
				user = "postgres",
				password = "password")
		};

	// Initialize database connection. 
	init {
		// TODO: Clean up this, and why is DB_URL == NULL?
		// If Docker

		// Check if the tables are already created
		// If not, create them and parse CSVs to database, else continue
		// TODO: Make work correctly, think about this
		/* if (tablesExist()) {
			transaction(db) {
				SchemaUtils.drop(TripsTable)
				SchemaUtils.drop(StationsTable)
			}
		}*/
		/*
		transaction(db) {
			SchemaUtils.create(TripsTable)
			SchemaUtils.create(StationsTable)
		}
		*/
	}

	// Function for returning data from tables
	// TODO: Add pagination and searching
	fun getStationsData(): ArrayList<StationModel> {
		var ret: ArrayList<StationModel> = arrayListOf()
		transaction(db) {
			for (station in StationsTable.selectAll()) {
				val stationId = station[StationsTable.station_id].toInt()
				ret.add(StationModel(
					stationId,

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
			// TODO: Refactor to forEach loop?
			for (station in StationsTable.selectAll().limit(pageSize, offset)) {
				val stationId = station[StationsTable.station_id].toInt()
				ret.add(StationModel(
					stationId,

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
					station[StationsTable.latitude],
				))
			}
		}
		return ret
	}

	/**
	 * Queries single station data by id given as a parameter.
	 * Returns null if station is not found in the database.
	 * Null should never be returned, as frontend application
	 * shouldn't be able to ask for station details for non-existent
	 * station.
	 * @param id Station id
	 * @return StationModelWithDetails || null
	 */
	fun getSingleStationData(id: Int): StationModelWithDetails? {
		var ret: StationModelWithDetails? = null
		transaction(db) {
			StationsTable.select { StationsTable.station_id eq id }.forEach {
				ret = StationModelWithDetails(
					it[StationsTable.station_id],

					it[StationsTable.name_fi],
					it[StationsTable.name_se],
					it[StationsTable.name_en],

					it[StationsTable.address_fi],
					it[StationsTable.address_se],

					it[StationsTable.city_fi],
					it[StationsTable.city_se],

					it[StationsTable.operator],
					it[StationsTable.capacity],

					it[StationsTable.longitude],
					it[StationsTable.latitude],

					getTotalJourneysFromStation(id),
					getTotalJourneysToStation(id),

					getAverageDepartDistanceForStation(id),
					getAverageReturnDistanceForStation(id),

					getTopDepartureStationsForStation(id),
					getTopReturnStationsForStation(id)
				)
			}
		}
		return ret
	}

	fun getPaginationTripsData(pageSize: Int, offset: Long): ArrayList<TripModelWithStationData> {
		var ret: ArrayList<TripModelWithStationData> = arrayListOf()
		val departureStationsTable = StationsTable.alias("st1")
		val returnStationsTable = StationsTable.alias("st2")

		// TODO: Refactor to forEach?
		transaction(db) {
			var trips = TripsTable
				.innerJoin(departureStationsTable, { TripsTable.departure_station_id}, {departureStationsTable[StationsTable.station_id]})
				.innerJoin(returnStationsTable, { TripsTable.return_station_id }, {returnStationsTable[StationsTable.station_id]})
				.selectAll().limit(pageSize, offset)
			for (trip in trips) {
				ret.add(TripModelWithStationData(

					trip[TripsTable.departure_time].toString(),
					trip[TripsTable.return_time].toString(),

					trip[TripsTable.departure_station_id],
					trip[departureStationsTable[StationsTable.name_fi]],
					trip[departureStationsTable[StationsTable.longitude]],
					trip[departureStationsTable[StationsTable.latitude]],

					trip[TripsTable.return_station_id],
					trip[returnStationsTable[StationsTable.name_fi]],
					trip[returnStationsTable[StationsTable.longitude]],
					trip[returnStationsTable[StationsTable.latitude]],

					trip[TripsTable.distance],
					trip[TripsTable.duration]
				))
			}
		}
		return ret
	}

	// Function for checking if tables already exist.
	private fun tablesExist(): Boolean {
		var ret = true
		transaction(db) {
			try {TripsTable.selectAll()} catch (e: Exception) {ret = false}
			try {StationsTable.selectAll()} catch (e: Exception) {ret = false}
		}
		return ret
	}



	// Functions for inserting data into database
	fun insertIntoStations(stations: ArrayList<StationModel>) {
		transaction(db) {
			StationsTable.batchInsert(stations) { it ->
				this[StationsTable.station_id] = it.id
				this[StationsTable.name_fi] = it.nameFi
				this[StationsTable.name_se] = it.nameSe
				this[StationsTable.name_en] = it.nameEn
				this[StationsTable.address_fi] = it.addressFi
				this[StationsTable.address_se] = it.addressSe
				this[StationsTable.city_fi] = it.cityFi
				this[StationsTable.city_se] = it.citySe
				this[StationsTable.operator] = it.operator
				this[StationsTable.capacity] = it.capacity
				this[StationsTable.longitude] = it.longitude
				this[StationsTable.latitude] = it.latitude
			}
			
		}
	}

	// TODO: For some reason inserts trip two times
	fun insertIntoTrips(trips: ArrayList<TripModel>) {
		println("Inserting trips")
		transaction(db) {
				// Times are saved as timestamps in the database,
				// check DataModels.kt for more information
				TripsTable.batchInsert(trips) {
					this[TripsTable.departure_time] = it.departureTime.toLocalDateTime()
					this[TripsTable.return_time] = it.returnTime.toLocalDateTime()
					this[TripsTable.departure_station_id] = it.departureStationId
					this[TripsTable.return_station_id] = it.returnStationId
					this[TripsTable.distance] = it.distanceCovered
					this[TripsTable.duration] = it.durationSeconds
				}
		}
	}

	// Function for deleting all data
	fun deleteTables() {
		transaction(db) {
			SchemaUtils.drop(TripsTable)
			SchemaUtils.drop(StationsTable)
		}
	}
	fun createTables() {
		transaction(db) {
			SchemaUtils.create(TripsTable)
			SchemaUtils.create(StationsTable)
		}
	}

	fun getTopDepartureStationsForStation(id: Int): ArrayList<Map<String, Int>> {
		var ret: ArrayList<Map<String, Int>> = arrayListOf()
		val departureStationsTable = StationsTable.alias("departure")
		transaction(db) {
			departureStationsTable
				.join(TripsTable, JoinType.INNER, additionalConstraint = {(departureStationsTable[StationsTable.station_id] eq TripsTable.departure_station_id) and (TripsTable.departure_station_id eq id)})
				.join(StationsTable, JoinType.INNER, additionalConstraint = {StationsTable.station_id eq TripsTable.return_station_id})
				.slice(departureStationsTable[StationsTable.station_id], StationsTable.name_fi, StationsTable.station_id.count())
				.selectAll().groupBy(departureStationsTable[StationsTable.station_id], StationsTable.station_id)
				.orderBy(StationsTable.station_id.count(), SortOrder.DESC).limit(5)
				.forEach {
					ret.add(mapOf(Pair(it[StationsTable.name_fi], it[StationsTable.station_id.count()].toInt())))
				}

		}
		return ret
	}

	fun getTopReturnStationsForStation(id: Int): ArrayList<Map<String, Int>> {
		var ret: ArrayList<Map<String, Int>> = arrayListOf()
		val returnStationsTable = StationsTable.alias("return")
		transaction(db) {
			returnStationsTable
				.join(TripsTable, JoinType.INNER, additionalConstraint = {(returnStationsTable[StationsTable.station_id] eq TripsTable.return_station_id) and (TripsTable.return_station_id eq id)})
				.join(StationsTable, JoinType.INNER, additionalConstraint = {StationsTable.station_id eq TripsTable.departure_station_id})
				.slice(returnStationsTable[StationsTable.station_id], StationsTable.name_fi, StationsTable.station_id.count())
				.selectAll().groupBy(returnStationsTable[StationsTable.station_id], StationsTable.station_id)
				.orderBy(StationsTable.station_id.count(), SortOrder.DESC).limit(5)
				.forEach {
					ret.add(mapOf(Pair(it[StationsTable.name_fi], it[StationsTable.station_id.count()].toInt())))
				}

		}
		return ret
	}

	fun getTotalJourneysFromStation(id: Int): Int {
		var ret = 0
		transaction(db) {
			ret = TripsTable.select {TripsTable.departure_station_id eq id}.count().toInt()
		}
		return ret
	}

	fun getTotalJourneysToStation(id: Int): Int {
		var ret = 0
		transaction(db) {
			ret = TripsTable.select {TripsTable.return_station_id eq id}.count().toInt()
		}
		return ret
	}

	fun getAverageDepartDistanceForStation(id: Int): Float {
		var ret: Float = 0.0F
		transaction(db) {
			TripsTable
				.slice(TripsTable.distance.avg(1))
				.select {TripsTable.departure_station_id eq id}
				.map {
					ret = it[TripsTable.distance.avg(1)]?.toFloat() ?: 0.0F
				}
		}
		return ret
	}

	fun getAverageReturnDistanceForStation(id: Int): Float {
		var ret: Float = 0.0F
		transaction(db) {
			TripsTable
				.slice(TripsTable.distance.avg(1))
				.select {TripsTable.return_station_id eq id}
				.map {
					ret = it[TripsTable.distance.avg(1)]?.toFloat() ?: 0.0F
				}
		}
		return ret
	}

}

//TODO: Refactor table names StationsTable -> Stations
object StationsTable : Table() {
	val station_id: Column<Int> = integer("station_id")
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

	override val primaryKey = PrimaryKey(station_id, name = "station_id")
}

object TripsTable : Table() {
	val departure_time = datetime("departure_time")
	val return_time = datetime("return_time")
	val departure_station_id = (integer("departure_station_id") references StationsTable.station_id)
	val return_station_id = (integer("return_station_id") references StationsTable.station_id)
	val distance = integer("distance").default(0)
	val duration = integer("duration").default(0)
}