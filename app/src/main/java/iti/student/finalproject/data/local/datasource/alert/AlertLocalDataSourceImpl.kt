package iti.student.finalproject.data.local.datasource.alert

import iti.student.finalproject.data.local.dao.AlertDao
import iti.student.finalproject.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertLocalDataSourceImpl(private val alertDao: AlertDao) : AlertLocalDataSource {
    override fun getAllAlert(): Flow<List<AlertEntity>> {
        return alertDao.getAllAlert()
    }

    override suspend fun deleteAlert(alert: AlertEntity) {
        alertDao.deleteAlert(alert)
    }

    override suspend fun deleteAlertById(id: Int) {
        alertDao.deleteById(id)
    }

    override suspend fun insertNewAlert(alert: AlertEntity): Long {
        return alertDao.insertAlert(alert)
    }
}