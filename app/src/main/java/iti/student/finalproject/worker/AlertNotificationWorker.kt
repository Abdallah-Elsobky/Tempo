package iti.student.finalproject.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import iti.student.finalproject.NotificationPrefs
import iti.student.finalproject.R
import iti.student.finalproject.presentation.alarm.AlertRingingActivity
import iti.student.finalproject.presentation.screen.notification.AlertType

class AlertNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!NotificationPrefs.areNotificationsEnabled(applicationContext)) {
            return Result.success()
        }

        val alertId = inputData.getInt(KEY_ALERT_ID, -1)
        val alertType = inputData.getString(KEY_ALERT_TYPE) ?: return Result.failure()
        val title = inputData.getString(KEY_TITLE) ?: getDefaultTitle(alertType)
        val body = inputData.getString(KEY_BODY) ?: getDefaultBody(alertType)

        val soundEnabled = NotificationPrefs.isSoundAlertsEnabled(applicationContext)
        createNotificationChannelsIfNeeded()
        if (soundEnabled) {
            showAlarmNotification(alertId, alertType, title, body)
        } else {
            showNotification(alertId, title, body, false)
        }
        return Result.success()
    }

    private fun createNotificationChannelsIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val silentChannel = NotificationChannel(
            CHANNEL_ID_SILENT,
            applicationContext.getString(R.string.notification_channel_alerts_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = applicationContext.getString(R.string.notification_channel_alerts_desc)
            enableVibration(true)
            setSound(null, null)
        }
        manager.createNotificationChannel(silentChannel)

        val soundChannel = NotificationChannel(
            CHANNEL_ID_SOUND,
            applicationContext.getString(R.string.notification_channel_alerts_sound_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = applicationContext.getString(R.string.notification_channel_alerts_sound_desc)
            enableVibration(true)
            val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            setSound(soundUri(), attrs)
        }
        manager.createNotificationChannel(soundChannel)
    }

    private fun showNotification(alertId: Int, title: String, body: String, soundEnabled: Boolean) {
        val channelId = if (soundEnabled) CHANNEL_ID_SOUND else CHANNEL_ID_SILENT
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(if (soundEnabled) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(if (soundEnabled) Notification.DEFAULT_VIBRATE else 0)
            .setAutoCancel(true)
        if (soundEnabled && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(soundUri())
        }
        val notification = builder.build()
        try {
            NotificationManagerCompat.from(applicationContext)
                .notify(NOTIFICATION_TAG, alertId.coerceAtLeast(0), notification)
        } catch (_: SecurityException) {
        }
    }

    private fun showAlarmNotification(alertId: Int, alertType: String, title: String, body: String) {
        val openIntent = Intent(applicationContext, AlertRingingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlertRingingActivity.EXTRA_ALERT_ID, alertId)
            putExtra(AlertRingingActivity.EXTRA_ALERT_TYPE, alertType)
            putExtra(AlertRingingActivity.EXTRA_TITLE, title)
            putExtra(AlertRingingActivity.EXTRA_BODY, body)
        }

        val snoozeIntent = Intent(applicationContext, AlertRingingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlertRingingActivity.EXTRA_ALERT_ID, alertId)
            putExtra(AlertRingingActivity.EXTRA_ALERT_TYPE, alertType)
            putExtra(AlertRingingActivity.EXTRA_TITLE, title)
            putExtra(AlertRingingActivity.EXTRA_BODY, body)
            putExtra(AlertRingingActivity.EXTRA_ACTION, AlertRingingActivity.ACTION_SNOOZE)
        }

        val dismissIntent = Intent(applicationContext, AlertRingingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlertRingingActivity.EXTRA_ALERT_ID, alertId)
            putExtra(AlertRingingActivity.EXTRA_ALERT_TYPE, alertType)
            putExtra(AlertRingingActivity.EXTRA_TITLE, title)
            putExtra(AlertRingingActivity.EXTRA_BODY, body)
            putExtra(AlertRingingActivity.EXTRA_ACTION, AlertRingingActivity.ACTION_DISMISS)
        }

        val contentPending = android.app.PendingIntent.getActivity(
            applicationContext,
            alertId,
            openIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        val snoozePending = android.app.PendingIntent.getActivity(
            applicationContext,
            alertId + 10_000,
            snoozeIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        val dismissPending = android.app.PendingIntent.getActivity(
            applicationContext,
            alertId + 20_000,
            dismissIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_SOUND)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(contentPending)
            .setFullScreenIntent(contentPending, true)
            .addAction(0, "Snooze", snoozePending)
            .addAction(0, "Dismiss", dismissPending)
            .build()

        try {
            NotificationManagerCompat.from(applicationContext)
                .notify(NOTIFICATION_TAG, alertId.coerceAtLeast(0), notification)
        } catch (_: SecurityException) {
        }
    }

    private fun soundUri(): Uri {
        return Uri.parse("android.resource://${applicationContext.packageName}/${R.raw.sound}")
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
        const val CHANNEL_ID_SILENT = "weather_alerts_silent"
        const val CHANNEL_ID_SOUND = "weather_alerts_sound"
        const val NOTIFICATION_TAG = "AlertNotification"
        const val WORK_NAME_PREFIX = "alert_notification_"

        fun cancelNotification(context: Context, alertId: Int) {
            try {
                NotificationManagerCompat.from(context)
                    .cancel(NOTIFICATION_TAG, alertId.coerceAtLeast(0))
            } catch (_: SecurityException) {
            }
        }
    }
}
