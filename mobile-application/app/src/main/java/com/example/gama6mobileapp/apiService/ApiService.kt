package com.example.gama6mobileapp.apiService

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gama6mobileapp.jsonParser.JsonParser
import com.example.gama6mobileapp.model.Location
import com.example.gama6mobileapp.network.NetworkClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

//const val IP_ADDRESS = "192.168.56.1" // Laptop
const val IP_ADDRESS = "192.168.1.2" // PC

object ApiService {
    fun getAllLocations(callback: (List<Location>) -> Unit) {
        val url = "http://${IP_ADDRESS}:3000/api/locations/getAll"
        NetworkClient.run(url, "", "GET", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.d("ApiService", "Failed to get locations")
                //print error message
                Log.d("ApiService", e.toString())
                callback(listOf())
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback(listOf())
                    return
                }

                val responseBody = response.body?.string() ?: ""
                val locations = JsonParser.parseLocations(responseBody)
                callback(locations)
            }
        })
    }

    fun upsertLocation(location: Location, callback: (Boolean) -> Unit) {
        val url = "http://${IP_ADDRESS}:3000/api/locations/upsert"
        val json = JsonParser.toJson(location)
        NetworkClient.run(url, json, "POST", object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                // You might want to check the response code here to determine if the upsert was successful
                callback(response.isSuccessful)
            }
        })
    }

    fun updateLocation(locationName: String, numCars: Int, callback: (Boolean) -> Unit) {
        val url = "http://${IP_ADDRESS}:3000/api/locations/updateLocation"

        // Creating the JSON payload
        val jsonPayload = JSONObject().apply {
            put("name", locationName)
            put("numCars", numCars)
        }

        // Convert the JSON object to a String
        val jsonString = jsonPayload.toString()

        NetworkClient.run(url, jsonString, "PUT", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }


    fun deleteLocation(location: Location, callback: (Boolean) -> Unit) {
        val url = "http://${IP_ADDRESS}:3000/api/locations/delete"
        val json = JsonParser.toJson(location)
        NetworkClient.run(url, json, "DELETE", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    callback(false)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Handler(Looper.getMainLooper()).post {
                    callback(response.isSuccessful)
                }
            }
        })
    }

}