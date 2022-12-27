package com.jp.backend

import com.google.gson.Gson

object JsonCreator {
    private val gson = Gson()

    //TODO: Tests
    fun tripsToJSON(trips: ArrayList<TripModelWithStationData>): String {
        return gson.toJson(trips)
    }

    // TODO: Tests
    fun stationDetailsToJSON(station: StationModelWithDetails): String {
        return gson.toJson(station)
    }

    fun stationsToJSON(stations: ArrayList<StationModel>): String {
        return gson.toJson(stations)
    }
}