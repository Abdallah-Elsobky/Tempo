package iti.student.finalproject.data.repository

import iti.student.finalproject.data.local.datasource.alert.AlertLocalDataSource
import iti.student.finalproject.data.local.entity.AlertEntity
import iti.student.finalproject.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow

class AlertRepositoryImpl(private val localDataSource: AlertLocalDataSource) : AlertRepository {
    override fun getAlerts(): Flow<List<AlertEntity>> {
        return localDataSource.getAllAlert()
    }

    override suspend fun insertAlert(alert: AlertEntity): Long {
        return localDataSource.insertNewAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertEntity) {
        localDataSource.deleteAlert(alert)
    }

    override suspend fun deleteAlertById(id: Int) {
        localDataSource.deleteAlertById(id)
    }
}