package com.example.gama6mobileapp.network

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object NetworkClient {
    private val client = OkHttpClient()

    fun run(url: String, json: String, method: String, callback: Callback) {
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val builder = Request.Builder().url(url)

        val request = when (method) {
            "GET" -> builder.get()
            "POST" -> builder.post(body)
            "DELETE" -> builder.delete(body)
            else -> builder.method(method, body)
        }.build()

        client.newCall(request).enqueue(callback)
    }
}
