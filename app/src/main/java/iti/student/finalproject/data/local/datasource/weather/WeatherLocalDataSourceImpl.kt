package iti.student.finalproject.data.local.datasource.weather

import iti.student.finalproject.data.local.dao.FavDao
import iti.student.finalproject.data.local.entity.FavLocationEntity
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