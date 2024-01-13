package com.example.gama6mobileapp.model

import java.util.UUID

data class Location(
    val name: String,
    val minCars: Int? = 0,
    val maxCars: Int? = 10,
    val updateFrequencySeconds: Int,
    val latitude: Double?,
    val longitude: Double?,
    var simulation: Boolean = false,
    val lastUpdated: Long = 0,
    val id: String = UUID.randomUUID().toString()
)
