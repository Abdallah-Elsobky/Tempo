package iti.student.finalproject.data.local.datasource.weather

import iti.student.finalproject.data.local.entity.FavLocationEntity
import kotlinx.coroutines.flow.Flow


interface WeatherLocalDataSource {
    fun getAllFav(): Flow<List<FavLocationEntity>>

    suspend fun deleteFavLocation(location: FavLocationEntity)

    suspend fun insertNewFav(location: FavLocationEntity)
}