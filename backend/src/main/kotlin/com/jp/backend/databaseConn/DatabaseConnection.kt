/**
 * This file includes a database connection Singleton,
 * and functions for interacting with the database.
 */

package com.jp.backend.databaseConn

import com.jp.backend.JourneyModel
import com.jp.backend.StationModel
import com.jp.backend.ValidationHelpers.updateStationIDs
import com.jp.backend.databaseConn.QueryHelpers.tablesExist
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConn {
    private val dbUrl: String = System.getenv("DB_URL") ?: "postgresql://localhost:5432/postgres"
    private val dbUsername: String = System.getenv("DB_USERNAME") ?: "postgres"
    private val dbPassword: String = System.getenv("DB_PASSWORD") ?: "password"


    // Initialize database connection.
    val db: Database =
        Database.connect(
            url = "jdbc:$dbUrl?reWriteBatchedInserts=true",
            driver = "org.postgresql.Driver",
            user = dbUsername,
            password = dbPassword
        )

    init {
        if (!tablesExist()) {
            // If tables don't exist, create them
            transaction(db) {
                SchemaUtils.create(Journeys)
                SchemaUtils.create(Stations)
            }
        }
    }

    // Destroy tables
    fun deleteTables() {
        println("Destroying tables")
        if (tablesExist()) {
            transaction(db) {
                SchemaUtils.drop(Journeys)
                SchemaUtils.drop(Stations)
            }
        }
    }

    // Create tables
    fun createTables() {
        println("Creating tables")
        if (!tablesExist()) {
            transaction(db) {
                SchemaUtils.create(Journeys)
                SchemaUtils.create(Stations)
            }
        }
    }


    fun insertIntoStations(stations: ArrayList<StationModel>) {
        println("Inserting ${stations.size} station(s)")
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

        // After insert of station, update allowed IDs
        // to ValidationHelpers
        updateStationIDs()
    }

    fun insertIntoJourneys(journeys: ArrayList<JourneyModel>) {
        println("Inserting ${journeys.size} Journey(s)")
        transaction(db) {
            // Times are saved as timestamps in the database,
            // check DataModels.kt for more information
            Journeys.batchInsert(journeys) {
                this[Journeys.departure_time] = it.departureTime.toLocalDateTime()
                this[Journeys.return_time] = it.returnTime.toLocalDateTime()
                this[Journeys.departure_station_id] = it.departureStationId
                this[Journeys.departure_station_name] = it.departureStationName
                this[Journeys.return_station_id] = it.returnStationId
                this[Journeys.return_station_name] = it.returnStationName
                this[Journeys.distance] = it.distanceCovered
                this[Journeys.duration] = it.durationSeconds
            }
        }
    }

}

object Stations : Table() {
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

object Journeys : Table() {
    val id = integer("id").autoIncrement()
    val departure_time = datetime("departure_time").index()
    val return_time = datetime("return_time").index()

    val departure_station_id = (integer("departure_station_id") references Stations.station_id).index()
    val departure_station_name = varchar("departure_station_name", 50).index()

    val return_station_id = (integer("return_station_id") references Stations.station_id).index()
    val return_station_name = varchar("return_station_name", 50).index()

    val distance = integer("distance").default(0).index()
    val duration = integer("duration").default(0).index()
}