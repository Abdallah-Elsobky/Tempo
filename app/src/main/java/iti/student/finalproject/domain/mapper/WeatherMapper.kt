package iti.student.finalproject.domain.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.utils.NumberUtils.roundTo
import iti.student.finalproject.utils.TimeUtils.getDayMonth
import iti.student.finalproject.utils.TimeUtils.getTime
import iti.student.finalproject.utils.TimeUtils.getDayName


object WeatherMapper {
    fun weatherToDomain(dto: WeatherResponseDto): WeatherModel {
        return WeatherModel(
            city = dto.name,
            country = dto.sys.country,
            temp = roundTo(dto.main.temp, 1),
            description = dto.weather[0].description,
            windSpeed = dto.wind.speed,
            humidity = dto.main.humidity,
            pressure = dto.main.pressure,
            clouds = dto.clouds.all,
            iconUrl = mapOpenWeatherIconToGoogleCondition(dto.weather[0].icon),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun forecastToDomain(dto: HourlyForecastResponseDto): List<ForecastModel> {
        val list = mutableListOf<ForecastModel>()
        dto.list.forEach {
            val forecast = ForecastModel(
                dayDate = getDayMonth(it.dtTxt),
                dayName = getDayName(it.dtTxt),
                dayTime = getTime(it.dtTxt),
                temperature = roundTo(it.main.temp, 1),
                minTemperature = roundTo(it.main.tempMin, 1),
                maxTemperature = roundTo(it.main.tempMax, 1),
                description = it.weather[0].description,
                windSpeed = it.wind.speed,
                humidity = it.main.humidity,
                pressure = it.main.pressure,
                clouds = it.clouds.all,
                iconUrl = mapOpenWeatherIconToGoogleCondition(it.weather[0].icon),
            )
            list.add(forecast)
        }
        return list
    }
}


private const val BASE_URL = "https://maps.gstatic.com/weather/v1/"
private const val EXT = ".svg"
fun mapOpenWeatherIconToGoogleCondition(icon: String): String {
    val condition = when (icon.take(2)) {
        "01" -> "sunny"
        "02" -> "mostly_clear"
        "03" -> "partly_cloudy"
        "04" -> "mostly_cloudy"
        "09" -> "scattered_showers"
        "10" -> "showers"
        "11" -> "strong_tstorms"
        "13" -> "snow_showers"
        "50" -> "cloudy"
        else -> "clear"
    }
    return "$BASE_URL$condition$EXT"
}