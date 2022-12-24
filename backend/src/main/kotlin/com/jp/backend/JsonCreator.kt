package com.jp.backend

import com.google.gson.Gson

object JsonCreator {
    private val gson = Gson()
    fun tripsToJSON(): String {
        return gson.toJson(DatabaseConn.getTripsData())
    }

    fun stationsToJSON(): String {
        return gson.toJson(DatabaseConn.getStationsData())
    }
}