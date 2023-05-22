import com.google.gson.annotations.SerializedName

data class Sparkasse(
    val id: String,
    @SerializedName("get_component_type_display") val componentTypeDisplay: String,
    val address: String,
    @SerializedName("transaction_amount_m2") val transactionAmountM2: Double?,
    @SerializedName("estimated_amount_m2") val estimatedAmountM2: Double?,
    @SerializedName("is_estimated_amount") val isEstimatedAmount: Boolean,
    val gps: Gps,
    @SerializedName("transaction_items_list") val transactionItemsList: List<String>,
    @SerializedName("transaction_sum_parcel_sizes") val transactionSumParcelSizes: Int,
    @SerializedName("transaction_date") val transactionDate: String,
    @SerializedName("transaction_amount_gross") val transactionAmountGross: Int,
    @SerializedName("transaction_tax") val transactionTax: Double?,
    @SerializedName("building_year_built") val buildingYearBuilt: Int,
    @SerializedName("unit_room_count") val unitRoomCount: Int?,
    @SerializedName("unit_rooms_sum_size") val unitRoomsSumSize: Double,
    @SerializedName("unit_rooms") val unitRooms: String
)

data class Gps(
    val lat: Double,
    val lng: Double
)
