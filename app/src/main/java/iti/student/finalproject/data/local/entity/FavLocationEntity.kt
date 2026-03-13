package iti.student.finalproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorites")
data class FavLocationEntity(
    @PrimaryKey
    val name: String,
    val country: String,
    val temp: Float,
    val iconUrl: String,
    val lat: Double,
    val lon: Double,
)