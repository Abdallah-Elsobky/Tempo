package iti.student.finalproject.domain.repository

import iti.student.finalproject.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAlerts(): Flow<List<AlertEntity>>
    suspend fun insertAlert(alert: AlertEntity): Long
    suspend fun deleteAlert(alert: AlertEntity)
    suspend fun deleteAlertById(id: Int)
}