package iti.student.finalproject.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import iti.student.finalproject.R
import iti.student.finalproject.presentation.screen.notification.AlertType

class AlertNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(KEY_ALERT_ID, -1)
        val alertType = inputData.getString(KEY_ALERT_TYPE) ?: return Result.failure()
        val title = inputData.getString(KEY_TITLE) ?: getDefaultTitle(alertType)
        val body = inputData.getString(KEY_BODY) ?: getDefaultBody(alertType)

        createNotificationChannelIfNeeded()
        showNotification(alertId, title, body)
        return Result.success()
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            applicationContext.getString(R.string.notification_channel_alerts_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = applicationContext.getString(R.string.notification_channel_alerts_desc)
            enableVibration(true)
        }
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun showNotification(alertId: Int, title: String, body: String) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(applicationContext)
                .notify(NOTIFICATION_TAG, alertId.coerceAtLeast(0), notification)
        } catch (_: SecurityException) {
        }
    }

    private fun getDefaultTitle(type: String): String = when (type) {
        AlertType.RAIN.name -> "Rain Alert"
        AlertType.STORM.name -> "Storm Alert"
        AlertType.TEMPERATURE.name -> "Temperature Alert"
        else -> "Weather Alert"
    }

    private fun getDefaultBody(type: String): String = when (type) {
        AlertType.RAIN.name ->
            "Rain is expected during your selected time. Consider carrying an umbrella."

        AlertType.STORM.name ->
            "Severe weather conditions may occur. Stay safe and avoid unnecessary travel."

        AlertType.TEMPERATURE.name ->
            "Significant temperature changes are expected. Please take precautions."

        else ->
            "Conditions may change during your selected time. Stay updated."
    }

    companion object {
        const val KEY_ALERT_ID = "alert_id"
        const val KEY_ALERT_TYPE = "alert_type"
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val CHANNEL_ID = "weather_alerts"
        private const val NOTIFICATION_TAG = "AlertNotification"
        const val WORK_NAME_PREFIX = "alert_notification_"
    }
}
