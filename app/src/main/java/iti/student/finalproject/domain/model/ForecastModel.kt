package iti.student.finalproject.domain.model

data class ForecastModel(
    val dayDate: String,
    val dayName: String,
    val dayTime: String,
    val temperature: Float,
    val minTemperature: Float,
    val maxTemperature: Float,
    val description: String,
    val windSpeed: Float,
    val humidity: Int,
    val pressure: Int,
    val clouds: Int,
    val iconUrl: String,
)
