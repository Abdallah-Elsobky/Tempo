package iti.student.finalproject.domain.model

import androidx.compose.ui.graphics.Color
import iti.student.finalproject.presentation.screen.notification.AlertType

data class AlertModel(
    val id: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val alertType: String,
)