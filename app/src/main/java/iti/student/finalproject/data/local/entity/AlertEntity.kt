package iti.student.finalproject.data.local.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import iti.student.finalproject.presentation.screen.notification.AlertType

@Entity("alert")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val alertType: String,
)
