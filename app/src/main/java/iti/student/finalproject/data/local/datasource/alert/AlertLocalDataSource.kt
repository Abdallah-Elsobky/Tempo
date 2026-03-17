package iti.student.finalproject.data.local.datasource.alert

import iti.student.finalproject.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

interface AlertLocalDataSource {
    fun getAllAlert(): Flow<List<AlertEntity>>

    suspend fun deleteAlert(alert: AlertEntity)

    suspend fun deleteAlertById(id: Int)

    suspend fun insertNewAlert(alert: AlertEntity): Long
}