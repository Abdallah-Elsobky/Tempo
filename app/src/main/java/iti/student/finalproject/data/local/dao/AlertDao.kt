package iti.student.finalproject.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import iti.student.finalproject.data.local.entity.AlertEntity
import iti.student.finalproject.data.local.entity.FavLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity): Long

    @Delete
    suspend fun deleteAlert(alert: AlertEntity)

    @Query("DELETE FROM alert WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM alert")
    fun getAllAlert(): Flow<List<AlertEntity>>
}