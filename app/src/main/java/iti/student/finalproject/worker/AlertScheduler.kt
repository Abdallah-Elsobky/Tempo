package iti.student.finalproject.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import iti.student.finalproject.domain.model.AlertModel
import iti.student.finalproject.presentation.screen.notification.AlertType
import java.util.concurrent.TimeUnit

class AlertScheduler(private val context: Context) {

    private val workManager = WorkManager.getInstance(context.applicationContext)

    fun scheduleAlert(alert: AlertModel) {
        val now = System.currentTimeMillis()
        when {
            now > alert.endDate -> {
                cancelAlert(alert.id)
                return
            }
            now in alert.startDate..alert.endDate -> {
                enqueueNotificationWork(alert, delayMs = 0)
                scheduleCleanupAtEndDate(alert)
                return
            }
            else -> {
                val delayMs = alert.startDate - now
                enqueueNotificationWork(alert, delayMs = delayMs)
                scheduleCleanupAtEndDate(alert)
            }
        }
    }

    private fun enqueueNotificationWork(alert: AlertModel, delayMs: Long) {
        val title = getTitleForType(alert.alertType)
        val body = getBodyForType(alert.alertType)

        val inputData = Data.Builder()
            .putInt(AlertNotificationWorker.KEY_ALERT_ID, alert.id)
            .putString(AlertNotificationWorker.KEY_ALERT_TYPE, alert.alertType)
            .putString(AlertNotificationWorker.KEY_TITLE, title)
            .putString(AlertNotificationWorker.KEY_BODY, body)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertNotificationWorker>()
            .setInitialDelay(maxOf(0, delayMs), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(TAG_ALERT + alert.id)
            .build()

        workManager.enqueueUniqueWork(
            AlertNotificationWorker.WORK_NAME_PREFIX + alert.id,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun scheduleCleanupAtEndDate(alert: AlertModel) {
        val delayMs = alert.endDate - System.currentTimeMillis()
        if (delayMs <= 0) return

        val inputData = Data.Builder()
            .putInt(AlertCleanupWorker.KEY_ALERT_ID, alert.id)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertCleanupWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(
            AlertCleanupWorker.WORK_NAME_PREFIX + alert.id,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelAlert(alertId: Int) {
        workManager.cancelUniqueWork(AlertNotificationWorker.WORK_NAME_PREFIX + alertId)
        workManager.cancelUniqueWork(AlertCleanupWorker.WORK_NAME_PREFIX + alertId)
    }

    private fun getTitleForType(type: String): String = when (type) {
        AlertType.RAIN.name -> "Rain Alert"
        AlertType.STORM.name -> "Storm Alert"
        AlertType.TEMPERATURE.name -> "Temperature Alert"
        else -> "Weather Alert"
    }

    private fun getBodyForType(type: String): String = when (type) {
        AlertType.RAIN.name -> "Your rain alert time has started. Check the weather."
        AlertType.STORM.name -> "Your storm alert time has started. Stay safe."
        AlertType.TEMPERATURE.name -> "Your temperature alert time has started."
        else -> "Your scheduled weather alert is now active."
    }

    companion object {
        private const val TAG_ALERT = "alert_"
    }
}
