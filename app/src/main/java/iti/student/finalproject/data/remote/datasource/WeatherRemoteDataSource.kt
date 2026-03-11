package iti.student.finalproject.data.remote.datasource

import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import retrofit2.Response


interface WeatherRemoteDataSource {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponseDto

    suspend fun getHourlyForecast(lat: Double, lon: Double): HourlyForecastResponseDto

    suspend fun getPossibleCities(cityName: String): List<CityDto>

    suspend fun getCityNamesLocalized(lat: Double, lon: Double): List<CityDto>
}