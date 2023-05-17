package util.objects

data class Listing(
    val title: String,
    val link: String,
    val photoUrl: String,
    val price: Int,
    val size: Double,
    val year: Int,
    val seller: String,
    val location: String
)