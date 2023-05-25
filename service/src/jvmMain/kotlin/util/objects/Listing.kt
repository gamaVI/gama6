package util.objects

import com.google.gson.annotations.SerializedName

data class Listing(
    val title: String,
    val link: String,
    val photoUrl: String,
    val price: Int,
    val size: Double?,
    val year: Int?,
    val seller: String,
    val location: String,
)

data class ListingDB(
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Int,
    @SerializedName("url") val link: String,
    @SerializedName("location") val location: String,
    @SerializedName("seller") val seller: String,
    @SerializedName("size") val size: Double?,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("type") val type: String,
)