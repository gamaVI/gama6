package com.example.gama6mobileapp.appClass

import android.app.Application
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
        if(file.exists()){
            val json = file.readText()
            locations = Gson().fromJson(json, Array<Location>::class.java).toMutableList()
        }
    }

    fun saveData() {
        val json = Gson().toJson(locations)
        file.writeText(json)
    }

    companion object {
        lateinit var instance : Gama6Application
            private set
    }
}