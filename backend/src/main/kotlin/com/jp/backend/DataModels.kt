package com.jp.backend

/**
 *   StationModel is used when parsing from the CSV files and inserting to the database.
 */
data class StationModel(
        val id: Int,
        val nameFi: String,
        val nameSe: String,
        val nameEn: String,
        val addressFi: String,
        val addressSe: String,
        val cityFi: String,
        val citySe: String,
        val operator: String,
        val capacity: Int,
        val longitude: String,
        val latitude: String
)

/**
 * StationModelWithDetails is used when returning data from API calls.
 *
 * It includes multiple extra details about the station, including
 * average distances of journeys starting/ending at the stations,
 * and most popular stations to go to/come from.
 */
data class StationModelWithDetails(
        val id: Int,

        val nameFi: String,
        val nameSe: String,
        val nameEn: String,

        val addressFi: String,
        val addressSe: String,

        val cityFi: String,
        val citySe: String,

        val operator: String,
        val capacity: Int,

        val longitude: String,
        val latitude: String,

        val totalDepartJourneys: Int,
        val totalReturnJourneys: Int,

        val averageDepartDistance: Float,
        val averageReturnDistance: Float,

        val averageDepartDuration: Float,
        val averageReturnDuration: Float,

        val topDepartStations: ArrayList<TopStationModel>,
        val topReturnStations: ArrayList<TopStationModel>
)

/**
 * Exclusively used for easier gathering of top return/depart stations for
 * StationModelWithDetails.
 */
data class TopStationModel(
        val name: String,
        val count: Int
)


/**
 *   JourneyModel is used when parsing from the CSV files and inserting to the database.
 *
 *   Timestamps are stored as strings, and converted to LocalDateTimes
 *   just before insertion into the database. Gson not easily being able
 *   to parse LocalDateTimes in this format led into this workaround.
 */
data class JourneyModel(
        val departureTime: String,
        val returnTime: String,
        val departureStationId: Int,
        val departureStationName: String,
        val returnStationId: Int,
        val returnStationName: String,
        val distanceCovered: Int,
        val durationSeconds: Int
)

/**
 * Journeys model comes from the database with data from the stations table,
 * including station names and coordinates.
 *
 * This model is used when returning data from database to the frontend.
 */
data class JourneyModelWithStationData(
        val id: Int,

        val departureTime: String,
        val returnTime: String,

        val departureStationId: Int,
        val departureStationName: String,
        val departureStationX: String,
        val departureStationY: String,

        val returnStationId: Int,
        val returnStationName: String,
        val returnStationX: String,
        val returnStationY: String,

        val distanceCovered: Int,
        val durationSeconds: Int
)