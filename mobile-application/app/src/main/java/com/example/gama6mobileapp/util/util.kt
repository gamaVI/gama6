package com.example.gama6mobileapp.util

import android.util.Log
import com.example.gama6mobileapp.apiService.IP_ADDRESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

// Change the function signature to be a suspend function
suspend fun uploadImage(imageBytes: ByteArray): Int = withContext(Dispatchers.IO) {
    // The network request is now inside a coroutine block
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file", "image.jpg",
            imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, imageBytes.size)
        )
        .build()

    val request = Request.Builder()
        .url("http://${IP_ADDRESS}:7000/process-image/") // Replace with your server URL
        .post(requestBody)
        .build()

    val client = OkHttpClient()

    try {
        val response = client.newCall(request).execute() // Synchronous call inside the IO Dispatcher

        if (response.isSuccessful) {
            val responseBody = response.body?.string() // Get the response body as a string
            response.close()
            if (responseBody != null) {
                val jsonObject = JSONObject(responseBody)
                jsonObject.optInt("count", 0)
            } else {
                0
            }
        } else {
            0
        }
    } catch (e: Exception) {
        Log.e("Upload", "Error during image upload", e)
        0
    }
}


