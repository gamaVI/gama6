package util.objects

data class Posel(
    val skupnaPovrsina: Double,
    val cena: Int,
    val cenaNaM2: Int,
    val lokacija: String,
    val datumPosla: String,
    val letoGradnje: Int,
    val tipPosla: String,
    val tipNepremicnine: String,
    val koordinateX: Double,
    val koordinateY: Double
)