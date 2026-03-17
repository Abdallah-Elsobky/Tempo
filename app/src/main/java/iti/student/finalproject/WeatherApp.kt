package iti.student.finalproject

import androidx.room.Room
import iti.student.finalproject.data.local.database.AppDatabase

class WeatherApp : android.app.Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
