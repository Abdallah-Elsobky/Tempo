package iti.student.finalproject.domain.model

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

enum class WindSpeedUnit {
    KMH,
    MPH
}

enum class LocationMode {
    GPS,
    MAP
}

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    ARABIC("ar")
}

enum class AppTheme {
    LIGHT,
    DARK
}

data class AppSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.KMH,
    val locationMode: LocationMode = LocationMode.GPS,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
    val mapLatitude: Double = 30.0444,
    val mapLongitude: Double = 31.2357
) {
    val apiUnits: String
        get() = if (temperatureUnit == TemperatureUnit.FAHRENHEIT) "imperial" else "metric"
}
