package com.jp.backend

import com.google.gson.Gson

object JsonCreator {
    private val gson = Gson()

    //TODO: Tests
    fun journeysToJSON(journeys: ArrayList<JourneyModelWithStationData>): String {
        return gson.toJson(journeys)
    }

    // TODO: Tests
    fun stationDetailsToJSON(station: StationModelWithDetails): String {
        return gson.toJson(station)
    }

    fun stationsToJSON(stations: ArrayList<StationModel>): String {
        return gson.toJson(stations)
    }
}