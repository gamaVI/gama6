package com.example.gama6mobileapp.jsonParser

import com.example.gama6mobileapp.model.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonParser {
    private val gson = Gson()

    fun parseLocations(json: String): List<Location> {
        val listType = object : TypeToken<List<Location>>() {}.type
        return gson.fromJson(json, listType)
    }

    fun toJson(location: Location): String {
        return gson.toJson(location)
    }
}