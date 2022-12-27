package com.jp.backend

import com.google.gson.Gson

object JsonCreator {
    private val gson = Gson()

    //TODO: Tests
    fun tripsToJSON(trips: ArrayList<TripModelWithStationData>): String {
        return gson.toJson(trips)
    }

    // TODO: Tests
    fun stationsToJSON(stations: ArrayList<StationModelWithDetails>): String {
        return gson.toJson(stations)
    }
}