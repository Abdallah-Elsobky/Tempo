package iti.student.finalproject.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.student.finalproject.data.local.database.AppDatabase
import iti.student.finalproject.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var alertDao: AlertDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        alertDao = database.alertDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAlert_and_getAllAlert_returnsInserted() = runBlocking {
        val alert = AlertEntity(id = 1, startDate = 0L, endDate = 0L, alertType = "RAIN")

        alertDao.insertAlert(alert)
        val result = alertDao.getAllAlert().first()

        assertEquals(1, result.size)
        assertEquals("RAIN", result[0].alertType)
    }

    @Test
    fun deleteAlert_removesFromDatabase() = runBlocking {
        val alert = AlertEntity(id = 1, startDate = 0L, endDate = 0L, alertType = "RAIN")
        alertDao.insertAlert(alert)

        alertDao.deleteAlert(alert)
        val result = alertDao.getAllAlert().first()

        assertEquals(0, result.size)
    }

    @Test
    fun deleteById_removesMatchingAlert() = runBlocking {
        val alert1 = AlertEntity(id = 1, startDate = 0L, endDate = 0L, alertType = "RAIN")
        val alert2 = AlertEntity(id = 2, startDate = 0L, endDate = 0L, alertType = "STORM")
        alertDao.insertAlert(alert1)
        alertDao.insertAlert(alert2)

        alertDao.deleteById(1)
        val result = alertDao.getAllAlert().first()

        assertEquals(1, result.size)
        assertEquals(2, result[0].id)
    }
}

