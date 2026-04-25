package iti.student.finalproject.domain.repository

import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        language: String,
        units: String
    ): Flow<ResultState<WeatherResponseDto>>

    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double,
        language: String,
        units: String
    ): Flow<ResultState<HourlyForecastResponseDto>>
    suspend fun getPossibleCities(cityName: String): Flow<ResultState<List<CityDto>>>
    suspend fun getCityNamesLocalized(lat: Double, lon: Double): Flow<ResultState<List<String>>>

    fun getFavorites(): Flow<List<FavLocationEntity>>
    suspend fun insertFavorite(location: FavLocationEntity)
    suspend fun deleteFavorite(location: FavLocationEntity)
}