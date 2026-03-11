package iti.student.finalproject.data.remote.datasource

import android.util.Log
import iti.student.finalproject.data.remote.api.WeatherApiService
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import retrofit2.Response

class WeatherRemoteDataSourceImpl(private val apiService: WeatherApiService) :
    WeatherRemoteDataSource {
    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): WeatherResponseDto {
        Log.d("loco repo", "call getWeather...")
        return apiService.getWeather(lat, lon)
    }

    override suspend fun getHourlyForecast(
        lat: Double,
        lon: Double
    ): HourlyForecastResponseDto {
        Log.d("loco repo", "call Forecast...")
        return apiService.getHourlyForecast(lat, lon)
    }

    override suspend fun getPossibleCities(cityName: String): List<CityDto> {
        return apiService.getPossibleCities(cityName)
    }

    override suspend fun getCityNamesLocalized(
        lat: Double,
        lon: Double
    ): List<CityDto> {
        return apiService.getCityNamesLocalized(lat, lon)
    }
}