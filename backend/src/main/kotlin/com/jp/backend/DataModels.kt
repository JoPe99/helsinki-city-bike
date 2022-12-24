package com.jp.backend

data class StationModel(
        val fid: Int,
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

data class TripModel(
        val departureTime: String, // TODO: Timestamp?
        val returnTime: String, // TODO: Timestamp?
        val departureStationId: Int,
        val departureStationName: String,
        val returnStationId: Int,
        val returnStationName: String,
        val distanceCovered: Int,
        val durationSeconds: Int

)