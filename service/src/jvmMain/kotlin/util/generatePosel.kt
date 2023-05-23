package util

import Gps
import Sparkasse
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.random.Random

val random = Random.Default

fun generatePosel(): Sparkasse {
    val componentTypeDisplay = generateRandomLocation()
    val address = generateRandomAddress()
    val transactionAmount = random.nextDouble(1247.1655328798186, 2357.1428571428573)
    val estimatedAmountM2 = transactionAmount
    val isEstimatedAmount = false
    val gps = generateRandomLngAndLat(45.9, 46.1, 14.4, 14.6)
    val ltransactionItemList = generateRandomTransactionItems()
    val trasnsactionSumParcelSizes = random.nextInt(1, 5)
    val transactionDate = generateRandomDate()
    val transactionAmountGross = random.nextInt(35_000, 350_000)
    val transactiontax: Double? = null
    val buildingYearBuilt = random.nextInt(1965, 2023)
    val unitRoomCount = random.nextInt(1, 4)
    val unitRoomsSumSize = roundToDecimals(random.nextDouble(26.0, 97.0), 1)
    val unitRooms = generateRandomUnitRooms()

    return Sparkasse(
        UUID.randomUUID().toString(),
        componentTypeDisplay,
        address,
        transactionAmount,
        estimatedAmountM2,
        isEstimatedAmount,
        gps,
        ltransactionItemList,
        trasnsactionSumParcelSizes,
        transactionDate,
        transactionAmountGross,
        transactiontax,
        buildingYearBuilt,
        unitRoomCount,
        unitRoomsSumSize,
        unitRooms
    )
}

//TODO: lahko se uporabi ce bi rabli vec lokacij
fun generateRandomLocation(): String {
    val locations = listOf("Hiša", "Stanovanje", "Poslovni prostor", "Zemljišče")
    return locations.random()
}

fun generateRandomUnitRooms(): String {
    val unitRoomTypes = listOf("Klet", "Shramba", "Odprt balkon", "Zaprta loža", "Tehnični ali pomožni prostori")

    val numberOfUnitRooms = random.nextInt(0, unitRoomTypes.size + 1)
    val randomUnitRooms = unitRoomTypes.shuffled().take(numberOfUnitRooms)

    return randomUnitRooms.joinToString(", ")

}


fun generateRandomTransactionItems(): List<String> {
    val transactionItems = listOf(
        "Pisarniški prostori",
        "Stanovanje",
        "Parkirni prostor",
        "Tehnični ali pomožni prostori",
        "Odprt balkon",
        "Zaprta loža",
        "Klet",
        "Shramba"
    )

    val randomItemCount = random.nextInt(1, transactionItems.size + 1)
    return transactionItems.shuffled().subList(0, randomItemCount)
}

fun generateRandomLngAndLat(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): Gps {
    val latitude = random.nextDouble(minLat, maxLat)
    val longitude = random.nextDouble(minLng, maxLng)
    return Gps(latitude, longitude)
}

fun generateRandomAddress(): String {
    val streetNames = listOf(
        "Main Street",
        "Park Avenue",
        "Garden Road",
        "Sunset Boulevard",
        "Oak Street",
        "Maple Avenue"
    )
    val cities = listOf(
        "Ljubljana",
        "Maribor",
        "Celje",
        "Kranj",
        "Velenje",
        "Novo Mesto"
    )
    val randomStreet = streetNames.random()
    val randomCity = cities.random()
    val randomHouseNumber = random.nextInt(1, 100)

    return "$randomStreet $randomHouseNumber, $randomCity"
}


fun generateRandomDate(): String {
    val year = random.nextInt(2013, 2024)
    val month = random.nextInt(1, 13)
    val day = random.nextInt(1, 30)
    val dayString = if (day < 10) "0$day" else "$day"
    val monthString = if (month < 10) "0$month" else "$month"
    return "$dayString.$monthString.$year"
}

fun generateRandomTipPosla(): String {
    val tipPoslaOptions = listOf(
        "Prodaja na prostem trgu",
        "Prodaja na javni dražbi",
        "Prodaja med družinskimi člani"
    )
    return tipPoslaOptions.random()
}

fun generateRandomTipNepremicnine(): String {
    val tipNepremicnineOptions = listOf("Hiša", "Stanovanje")
    return tipNepremicnineOptions.random()
}

fun roundToDecimals(value: Double, decimalPlaces: Int): Double {
    require(decimalPlaces >= 0) { "Decimal places must be a non-negative value." }

    val bigDecimal = BigDecimal(value)
    return bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP).toDouble()
}
