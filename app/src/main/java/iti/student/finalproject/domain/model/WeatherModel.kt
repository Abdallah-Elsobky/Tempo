package iti.student.finalproject.domain.model

data class WeatherModel(
    val city: String,
    val country: String,
    val temp: Float,
    val description: String,
    val windSpeed: Float,
    val humidity: Int,
    val pressure: Int,
    val clouds: Int,
    val iconUrl: String,
    val lon: Float,
    val lat:Float
)
