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
	// Initialize database connection.
	private val db: Database =
		// If Docker
		// TODO: Clean up this
		if (System.getenv("DB_URL") != "null") {
			Database.connect(
				System.getenv("DB_URL") + "?reWriteBatchedInserts=true",
				driver = "org.postgresql.Driver",
				user = System.getenv("DB_USERNAME"),
				password = System.getenv("DB_PASSWORD"))
		} else { // If no system environment vars, for example running without docker
			Database.connect(
				"jdbc:postgresql://localhost:5432/postgres?reWriteBatchedInserts=true",
				driver = "org.postgresql.Driver",
				user = "postgres",
				password = "password")
		}

	// TODO: Make work
	/*init {
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

	}*/

// Function for all stations
fun getStationsData(): ArrayList<StationModel> {
    var ret: ArrayList<StationModel> = arrayListOf()
    transaction(db) {
        for (station in Stations.selectAll()) {
            val stationId = station[Stations.station_id].toInt()
            ret.add(StationModel(
                stationId,

                station[Stations.name_fi],
                station[Stations.name_se],
                station[Stations.name_en],

                station[Stations.address_fi],
                station[Stations.address_se],

                station[Stations.city_fi],
                station[Stations.city_se],

                station[Stations.operator],
                station[Stations.capacity],

                station[Stations.longitude],
                station[Stations.latitude]
            ))
        }
    }
    return ret
}

// Function for getting paginated/sorted/searched data
fun getPaginationStationsData(pageSize: Int, offset: Long, sortBy: String? = "", search: String? = ""): ArrayList<StationModel> {
    var ret: ArrayList<StationModel> = arrayListOf()
    transaction(db) {
        // TODO: Refactor to forEach loop?
        for (station in Stations.selectAll().limit(pageSize, offset)) {
            val stationId = station[Stations.station_id].toInt()
            ret.add(StationModel(
                stationId,

                station[Stations.name_fi],
                station[Stations.name_se],
                station[Stations.name_en],

                station[Stations.address_fi],
                station[Stations.address_se],

                station[Stations.city_fi],
                station[Stations.city_se],

                station[Stations.operator],
                station[Stations.capacity],

                station[Stations.longitude],
                station[Stations.latitude],
            ))
        }
    }
    return ret
}

/**
 * Form detailed single station data by id given as a parameter.
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
        Stations.select { Stations.station_id eq id }.forEach {
            ret = StationModelWithDetails(
                it[Stations.station_id],

                it[Stations.name_fi],
                it[Stations.name_se],
                it[Stations.name_en],

                it[Stations.address_fi],
                it[Stations.address_se],

                it[Stations.city_fi],
                it[Stations.city_se],

                it[Stations.operator],
                it[Stations.capacity],

                it[Stations.longitude],
                it[Stations.latitude],

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

fun getPaginationTripsData(pageSize: Int, offset: Long, sortBy: String? = "", search: String? = ""): ArrayList<TripModelWithStationData> {
    val ret: ArrayList<TripModelWithStationData> = arrayListOf()
    val orderBy = formOrderBy()
    val departureStationsTable = Stations.alias("st1")
    val returnStationsTable = Stations.alias("st2")

    // TODO: Refactor to forEach?
    transaction(db) {
        var trips = Trips
            .innerJoin(departureStationsTable, { Trips.departure_station_id}, {departureStationsTable[Stations.station_id]})
            .innerJoin(returnStationsTable, { Trips.return_station_id }, {returnStationsTable[Stations.station_id]})
            .selectAll().orderBy(orderBy).limit(pageSize, offset)
        for (trip in trips) {
            ret.add(TripModelWithStationData(

                trip[Trips.departure_time].toString(),
                trip[Trips.return_time].toString(),

                trip[Trips.departure_station_id],
                trip[departureStationsTable[Stations.name_fi]],
                trip[departureStationsTable[Stations.longitude]],
                trip[departureStationsTable[Stations.latitude]],

                trip[Trips.return_station_id],
                trip[returnStationsTable[Stations.name_fi]],
                trip[returnStationsTable[Stations.longitude]],
                trip[returnStationsTable[Stations.latitude]],

                trip[Trips.distance],
                trip[Trips.duration]
            ))
        }
    }
    return ret
}

private fun formOrderBy(): Pair<Column<*>, SortOrder> {
    return Pair(Trips.departure_time, SortOrder.ASC)
}

// Function for checking if tables already exist.
private fun tablesExist(): Boolean {
    var ret = true
    transaction(db) {
        try {Trips.selectAll()} catch (e: Exception) {ret = false}
        try {Stations.selectAll()} catch (e: Exception) {ret = false}
    }
    return ret
}



// Functions for inserting data into database
fun insertIntoStations(stations: ArrayList<StationModel>) {
    transaction(db) {
        Stations.batchInsert(stations) { it ->
            this[Stations.station_id] = it.id
            this[Stations.name_fi] = it.nameFi
            this[Stations.name_se] = it.nameSe
            this[Stations.name_en] = it.nameEn
            this[Stations.address_fi] = it.addressFi
            this[Stations.address_se] = it.addressSe
            this[Stations.city_fi] = it.cityFi
            this[Stations.city_se] = it.citySe
            this[Stations.operator] = it.operator
            this[Stations.capacity] = it.capacity
            this[Stations.longitude] = it.longitude
            this[Stations.latitude] = it.latitude
        }

    }
}

// TODO: For some reason inserts trip two times
fun insertIntoTrips(trips: ArrayList<TripModel>) {
    println("Inserting trips")
    transaction(db) {
            // Times are saved as timestamps in the database,
            // check DataModels.kt for more information
            Trips.batchInsert(trips) {
                this[Trips.departure_time] = it.departureTime.toLocalDateTime()
                this[Trips.return_time] = it.returnTime.toLocalDateTime()
                this[Trips.departure_station_id] = it.departureStationId
                this[Trips.return_station_id] = it.returnStationId
                this[Trips.distance] = it.distanceCovered
                this[Trips.duration] = it.durationSeconds
            }
    }
}

// Function for deleting all data
fun deleteTables() {
    transaction(db) {
        SchemaUtils.drop(Trips)
        SchemaUtils.drop(Stations)
    }
}
fun createTables() {
    transaction(db) {
        SchemaUtils.create(Trips)
        SchemaUtils.create(Stations)
    }
}

fun getTopDepartureStationsForStation(id: Int): ArrayList<Map<String, Int>> {
    var ret: ArrayList<Map<String, Int>> = arrayListOf()
    val departureStationsTable = Stations.alias("departure")
    transaction(db) {
        departureStationsTable
            .join(Trips, JoinType.INNER, additionalConstraint = {(departureStationsTable[Stations.station_id] eq Trips.departure_station_id) and (Trips.departure_station_id eq id)})
            .join(Stations, JoinType.INNER, additionalConstraint = {Stations.station_id eq Trips.return_station_id})
            .slice(departureStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
            .selectAll().groupBy(departureStationsTable[Stations.station_id], Stations.station_id)
            .orderBy(Stations.station_id.count(), SortOrder.DESC).limit(5)
            .forEach {
                ret.add(mapOf(Pair(it[Stations.name_fi], it[Stations.station_id.count()].toInt())))
            }

    }
    return ret
}

private fun getTopReturnStationsForStation(id: Int): ArrayList<Map<String, Int>> {
    var ret: ArrayList<Map<String, Int>> = arrayListOf()
    val returnStationsTable = Stations.alias("return")
    transaction(db) {
        returnStationsTable
            .join(Trips, JoinType.INNER, additionalConstraint = {(returnStationsTable[Stations.station_id] eq Trips.return_station_id) and (Trips.return_station_id eq id)})
            .join(Stations, JoinType.INNER, additionalConstraint = {Stations.station_id eq Trips.departure_station_id})
            .slice(returnStationsTable[Stations.station_id], Stations.name_fi, Stations.station_id.count())
            .selectAll().groupBy(returnStationsTable[Stations.station_id], Stations.station_id)
            .orderBy(Stations.station_id.count(), SortOrder.DESC).limit(5)
            .forEach {
                ret.add(mapOf(Pair(it[Stations.name_fi], it[Stations.station_id.count()].toInt())))
            }

    }
    return ret
}

private fun getTotalJourneysFromStation(id: Int): Int {
    var ret = 0
    transaction(db) {
        ret = Trips.select {Trips.departure_station_id eq id}.count().toInt()
    }
    return ret
}

private fun getTotalJourneysToStation(id: Int): Int {
    var ret = 0
    transaction(db) {
        ret = Trips.select {Trips.return_station_id eq id}.count().toInt()
    }
    return ret
}

private fun getAverageDepartDistanceForStation(id: Int): Float {
    var ret: Float = 0.0F
    transaction(db) {
        Trips
            .slice(Trips.distance.avg(1))
            .select {Trips.departure_station_id eq id}
            .map {
                ret = it[Trips.distance.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return ret
}

private fun getAverageReturnDistanceForStation(id: Int): Float {
    var ret: Float = 0.0F
    transaction(db) {
        Trips
            .slice(Trips.distance.avg(1))
            .select {Trips.return_station_id eq id}
            .map {
                ret = it[Trips.distance.avg(1)]?.toFloat() ?: 0.0F
            }
    }
    return ret
}

}

// TODO: Refactor "station_id" to "id"
object Stations: Table() {
val station_id: Column<Int> = integer("station_id").index()
val name_fi: Column<String> = varchar("name_fi", 50).index()
val name_se: Column<String> = varchar("name_se", 50)
val name_en: Column<String> = varchar("name_en", 50)
val address_fi: Column<String> = varchar("address_fi", 50).index()
val address_se: Column<String> = varchar("address_se", 50)
val city_fi: Column<String> = varchar("city_fi", 50)
val city_se: Column<String> = varchar("city_se", 50)
val operator: Column<String> = varchar("operator", 50)
val capacity: Column<Int> = integer("capacity").index()
val longitude: Column<String> = varchar("longitude", 50) // PostGIS?
val latitude: Column<String> = varchar("latitude", 50) // PostGIS?

override val primaryKey = PrimaryKey(station_id, name = "station_id")
}

// TODO: Refactor Trips -> Journeys everywhere
object Trips: Table() {
val departure_time = datetime("departure_time").index()
val return_time = datetime("return_time").index()
val departure_station_id = (integer("departure_station_id") references Stations.station_id).index()
val return_station_id = (integer("return_station_id") references Stations.station_id).index()
val distance = integer("distance").default(0).index()
val duration = integer("duration").default(0).index()
}