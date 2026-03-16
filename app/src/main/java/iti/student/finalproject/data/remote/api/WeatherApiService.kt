package iti.student.finalproject.data.remote.api

import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherResponseDto

    @GET("data/2.5/forecast")
    suspend fun getHourlyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 40
    ): HourlyForecastResponseDto

    @GET("geo/1.0/direct")
    suspend fun getPossibleCities(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1
    ): List<CityDto>

    @GET("geo/1.0/reverse")
    suspend fun getCityNamesLocalized(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1
    ): List<CityDto>
}