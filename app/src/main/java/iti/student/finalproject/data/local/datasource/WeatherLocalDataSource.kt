package iti.student.finalproject.data.local.datasource

import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import kotlinx.coroutines.flow.Flow


interface WeatherLocalDataSource {
    fun getAllFav(): Flow<List<FavLocationEntity>>

    suspend fun deleteFavLocation(location: FavLocationEntity)

    suspend fun insertNewFav(location: FavLocationEntity)
}