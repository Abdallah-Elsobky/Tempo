package iti.student.finalproject.domain.model

data class ForecastModel(
    /** API slot timestamp, e.g. `2026-01-01 00:00:00` — used for daily rows, not for display. */
    val dtTxt: String,
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
