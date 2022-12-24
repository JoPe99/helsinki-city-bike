package com.jp.backend

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonCreator {
    private val gson = Gson()

    //TODO: Tests
    fun tripsToJSON(trips: ArrayList<TripModel>): String {
        return gson.toJson(trips)
    }

    // TODO: Tests
    fun stationsToJSON(stations: ArrayList<StationModel>): String {
        return gson.toJson(stations)
    }
}