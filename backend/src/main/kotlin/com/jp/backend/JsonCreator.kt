package com.jp.backend

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonCreator {
    private val gson = Gson()

    //TODO: Tests
    fun tripsToJSON(): String {
        return gson.toJson(DatabaseConn.getTripsData())
    }

    // TODO: Tests
    fun stationsToJSON(): String {
        return gson.toJson(DatabaseConn.getStationsData())
    }
}