package iti.student.finalproject.domain.model

data class FavLocationModel(
    val name: String,
    val country: String,
    val temp: Float,
    val iconUrl: String,
    val lat: Double,
    val lon: Double,
)