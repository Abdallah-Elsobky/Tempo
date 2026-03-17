package iti.student.finalproject.data.local.dao

import androidx.room.*
import iti.student.finalproject.data.local.entity.FavLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(fav: FavLocationEntity)

    @Delete
    suspend fun deleteFav(fav: FavLocationEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFav(): Flow<List<FavLocationEntity>>
}