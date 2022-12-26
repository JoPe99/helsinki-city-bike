package com.jp.backend

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.*

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
 *   Timestamps are stored as strings, and converted to LocalDateTimes
 *   just before insertion into the database. Gson not easily being able
 *   to parse LocalDateTimes led into this workaround.
 */
data class TripModel(
        val departureTime: String,
        val returnTime: String,
        val departureStationId: Int,
        //val departureStationName: String,
        val returnStationId: Int,
        //val returnStationName: String,
        val distanceCovered: Int,
        val durationSeconds: Int

)