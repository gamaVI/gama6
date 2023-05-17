package util

import util.objects.Posel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

val random = Random.Default

fun generatePosel(): Posel {
    val skupnaPovrsina = roundToDecimals(random.nextDouble(100.0, 120.0), 1)
    val cenaNaM2 = random.nextInt(1000, 5000)
    val cena = random.nextInt(50_000, 555_000)
    val lokacija = "Ljubljana"
    val datumPosla = generateRandomDate()
    val letoGradnje = random.nextInt(1950, 2023)
    val tipPosla = generateRandomTipPosla()
    val tipNepremicnine = generateRandomTipNepremicnine()
    val koordinateX = roundToDecimals(random.nextDouble(46.0, 56.0),3)
    val koordinateY = roundToDecimals(random.nextDouble(14.0, 24.0),3)

    return Posel(
        skupnaPovrsina,
        cena,
        cenaNaM2,
        lokacija,
        datumPosla,
        letoGradnje,
        tipPosla,
        tipNepremicnine,
        koordinateX,
        koordinateY
    )
}

//TODO: lahko se uporabi ce bi rabli vec lokacij
//fun generateRandomLocation(): String {
//    val locations = listOf("Ljubljana - Bežigrad", "Ljubljana - Center", "Ljubljana - Moste-Polje", "Ljubljana - Vič-Rudnik", "Ljubljana - Šiška")
//    return locations.random()
//}

fun generateRandomDate(): String {
    val year = random.nextInt(2021, 2024)
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
