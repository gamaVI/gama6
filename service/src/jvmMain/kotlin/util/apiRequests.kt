package util

import Sparkasse
import SparkasseDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import util.objects.Listing
import util.objects.ListingsResponse
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiRequests {

    private val client = OkHttpClient.Builder()
        .callTimeout(25, TimeUnit.SECONDS) //Timeout after 25 seconds
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private var NEPREMICNINE_API_ENDPOINT = "http://51.136.39.46:3000/api/scraping/nepremicnine"
    private var SPARKASSE_API_ENDPOINT = "http://51.136.39.46:3000/api/scraping/posli"
    private var SPARKASSE_DB_ENDPOINT = "http://51.136.39.46:3000/api/transactions/new"

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
                .url(NEPREMICNINE_API_ENDPOINT)
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

    @JvmStatic
    fun getPosli(dateInterval: Int, selectedUnitTypeIndex: Int, selectedSubTypeIndex: Int): List<Sparkasse> {
        try {
            val json = """
            {
                "unit_type":  ${selectedUnitTypeIndex},
                "unit_subtype": ${selectedSubTypeIndex},
                "date_interval_months": $dateInterval
            }
            """.trimIndent()

            val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(SPARKASSE_API_ENDPOINT)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val responseData = response.body?.string()
                responseData?.let {
                    val gson = Gson()
                    val type = object : TypeToken<List<Sparkasse>>() {}.type
                    return gson.fromJson(it, type)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return emptyList()
    }

    /*
    Save sparkasse data to database one by one
     */
    @JvmStatic
    fun savePosli(sparkasse: MutableList<Sparkasse>){
        val sparkasseDB = convertSparkasseToSparkasseDB(sparkasse)
        val gson = GsonBuilder().serializeNulls().create()
        for (item in sparkasseDB) {
            try {
                val json = gson.toJson(item)
                val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url(SPARKASSE_DB_ENDPOINT)
                    .post(body)
                    .build()
                println("Sending $json")  // print JSON instead of body for readability
                val response = client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val responseData = response.body?.string()
                    responseData?.let {
                        println("Received response: $it")  // print the received response
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }


    @JvmStatic
    fun convertSparkasseToSparkasseDB(sparkasse: MutableList<Sparkasse>) : List<SparkasseDB>{
        val sparkasseDBList = sparkasse.map { sparkasse ->
            SparkasseDB(
                sparkasse.id,
                sparkasse.componentTypeDisplay,
                sparkasse.address,
                sparkasse.transactionAmountM2,
                sparkasse.estimatedAmountM2,
                sparkasse.isEstimatedAmount,
                sparkasse.gps,
                sparkasse.transactionItemsList,
                sparkasse.transactionSumParcelSizes,
                sparkasse.transactionDate,
                sparkasse.transactionAmountGross,
                sparkasse.transaction_tax,
                sparkasse.buildingYearBuilt,
                sparkasse.unitRoomCount,
                sparkasse.unitRoomsSumSize,
                sparkasse.unitRooms
            )
        }
        return sparkasseDBList
    }
}


