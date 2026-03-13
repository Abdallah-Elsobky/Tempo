package iti.student.finalproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import iti.student.finalproject.data.local.dao.FavDao
import iti.student.finalproject.data.local.entity.FavLocationEntity

@Database(entities = [FavLocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favDao(): FavDao
}