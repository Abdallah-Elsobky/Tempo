package iti.student.finalproject.data.local.datasource.alert

import iti.student.finalproject.data.local.dao.AlertDao
import iti.student.finalproject.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AlertLocalDataSourceImplTest {

    private class FakeAlertDao : AlertDao {
        val alertsFlow = MutableStateFlow<List<AlertEntity>>(emptyList())

        var insertCalledWith: AlertEntity? = null
        var deleteCalledWith: AlertEntity? = null
        var deleteByIdCalledWith: Int? = null

        override suspend fun insertAlert(alert: AlertEntity): Long {
            insertCalledWith = alert
            alertsFlow.value = alertsFlow.value + alert
            return 1L
        }

        override suspend fun deleteAlert(alert: AlertEntity) {
            deleteCalledWith = alert
            alertsFlow.value = alertsFlow.value.filterNot { it.id == alert.id }
        }

        override suspend fun deleteById(id: Int) {
            deleteByIdCalledWith = id
            alertsFlow.value = alertsFlow.value.filterNot { it.id == id }
        }

        override fun getAllAlert(): Flow<List<AlertEntity>> = alertsFlow
    }

    @Test
    fun `getAllAlert delegates to dao`() {
        val fakeDao = FakeAlertDao()
        val dataSource = AlertLocalDataSourceImpl(fakeDao)

        val flow = dataSource.getAllAlert()

        assertEquals(fakeDao.alertsFlow, flow)
    }

    @Test
    fun `insertNewAlert calls dao insert`() = runTest {
        val fakeDao = FakeAlertDao()
        val dataSource = AlertLocalDataSourceImpl(fakeDao)
        val alert = AlertEntity(id = 1, startDate = 0L, endDate = 0L, alertType = "RAIN")

        dataSource.insertNewAlert(alert)

        assertEquals(alert, fakeDao.insertCalledWith)
    }

    @Test
    fun `deleteAlertById calls dao deleteById`() = runTest {
        val fakeDao = FakeAlertDao()
        val dataSource = AlertLocalDataSourceImpl(fakeDao)

        dataSource.deleteAlertById(5)

        assertEquals(5, fakeDao.deleteByIdCalledWith)
    }
}

