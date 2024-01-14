package com.example.gama6mobileapp.appClass

import android.app.Application
import android.widget.Toast
import com.example.gama6mobileapp.apiService.ApiService
import com.example.gama6mobileapp.model.Location
import com.google.gson.Gson
import java.io.File

const val FILE_NAME = "locations.json"

class Gama6Application : Application() {
    private lateinit var file: File
    var locations: MutableList<Location> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        instance = this
        file = File(filesDir, FILE_NAME)
        initData()
    }

    private fun initData() {
        if (file.exists()) {
            val json = file.readText()
            locations = Gson().fromJson(json, Array<Location>::class.java).toMutableList()
        }
    }

    fun saveData() {
        val json = Gson().toJson(locations)
        file.writeText(json)
    }

    companion object {
        lateinit var instance: Gama6Application
            private set
    }

    fun fetchLocationsFromServer(callback: (MutableList<Location>) -> Unit) {
        ApiService.getAllLocations { fetchedLocations ->
            locations.clear()
            locations.addAll(fetchedLocations)
            saveData()
            callback(fetchedLocations.toMutableList())
        }
    }

    fun upsertLocation(location: Location): Boolean {
        var return_value = true
        ApiService.upsertLocation(location) { success ->
            if (success) {
                // Update or add the location in the local list
                val index = locations.indexOfFirst { it.name == location.name }
                if (index != -1) {
                    locations[index] = location
                } else {
                    locations.add(location)
                }
                return_value = true
                saveData() // Save the updated list to file
                // Notify UI about the change
            } else {
                return_value = false
            }
        }
        return return_value
    }
}