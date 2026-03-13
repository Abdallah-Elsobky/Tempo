package iti.student.finalproject.data.local.datasource

import android.util.Log
import iti.student.finalproject.data.local.dao.FavDao
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.api.WeatherApiService
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(private val favDao: FavDao) :
    WeatherLocalDataSource {
    override fun getAllFav(): Flow<List<FavLocationEntity>> {
        return favDao.getAllFav()
    }

    override suspend fun deleteFavLocation(location: FavLocationEntity) {
        favDao.deleteFav(location)
    }

    override suspend fun insertNewFav(location: FavLocationEntity) {
        favDao.insertFav(location)
    }
}