package util

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.JsonParser
import util.objects.Listing
import java.util.concurrent.TimeUnit

data class ListingsResponse(val listings: List<Listing>, val pageCount: Int)

object ApiRequests {
    private val client = OkHttpClient.Builder()
        .callTimeout(25, TimeUnit.SECONDS) //Timeout after 25 seconds
        .readTimeout(25, TimeUnit.SECONDS)
        .build()
    private var BASE_URL = "http://localhost:3000/api/scraping/nepremicnine"

    @JvmStatic
    fun getNepremicnine(pageNumber: Int): ListingsResponse {
        try {
            val formBody = FormBody.Builder()
                .add("pageNumber", pageNumber.toString())
                .add("minCost", "1")
                .add("maxCost", "99999999")
                .add("typeOfBuilding", "stanovanje")
                .add("location", "ljubljana-mesto")
                .build()

            val request = Request.Builder()
                .url(BASE_URL)
                .post(formBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val responseData = response.body?.string()
                responseData?.let {
                    val jsonObject = JsonParser.parseString(it).asJsonObject
                    val listingsJsonArray = jsonObject.getAsJsonArray("listings")
                    val pageCount = jsonObject.getAsJsonPrimitive("pageCount").asInt

                    val gson = Gson()
                    val listings = gson.fromJson(listingsJsonArray, Array<Listing>::class.java)

                    return ListingsResponse(listings.toList(), pageCount)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return ListingsResponse(emptyList(), 1)
    }

}

