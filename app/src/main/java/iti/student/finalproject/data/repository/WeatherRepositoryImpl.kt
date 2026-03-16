package iti.student.finalproject.data.repository

import android.util.Log
import iti.student.finalproject.data.local.datasource.WeatherLocalDataSource
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSource
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): Flow<ResultState<WeatherResponseDto>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getWeather(lat, lon)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getHourlyForecast(
        lat: Double,
        lon: Double
    ): Flow<ResultState<HourlyForecastResponseDto>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getHourlyForecast(lat, lon)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPossibleCities(cityName: String): Flow<ResultState<List<CityDto>>> =
        flow {
            emit(ResultState.Loading)
            try {
                val response = remoteDataSource.getPossibleCities(cityName)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getCityNamesLocalized(
        lat: Double,
        lon: Double
    ): Flow<ResultState<List<String>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getCityNamesLocalized(lat, lon)
            val result: List<String> = response.map { it.name ?: "Unknown" }
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFavorites(): Flow<List<FavLocationEntity>> {
        return localDataSource.getAllFav()
    }

    override suspend fun insertFavorite(location: FavLocationEntity) {
        localDataSource.insertNewFav(location)
    }

    override suspend fun deleteFavorite(location: FavLocationEntity) {
        localDataSource.deleteFavLocation(location)
    }
}