package com.jp.backend


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
 *   TripModel is used when parsing from the CSV files and inserting to the database.
 *
 *   Timestamps are stored as strings, and converted to LocalDateTimes
 *   just before insertion into the database. Gson not easily being able
 *   to parse LocalDateTimes in this format led into this workaround.
 */
data class TripModel(
        val departureTime: String,
        val returnTime: String,
        val departureStationId: Int,
        val returnStationId: Int,
        val distanceCovered: Int,
        val durationSeconds: Int
)

/**
 * Trip model comes from the database with data from the stations table,
 * including station names and coordinates.
 *
 * This model is used when returning data from database to the frontend.
 */
data class TripModelWithStationData(
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