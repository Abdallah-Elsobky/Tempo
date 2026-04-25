package iti.student.finalproject.presentation.alarm

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import iti.student.finalproject.NotificationPrefs
import iti.student.finalproject.R
import iti.student.finalproject.ui.theme.FinalProjectTheme
import iti.student.finalproject.ui.theme.WeatherAccentBlue
import iti.student.finalproject.ui.theme.WeatherAccentRed
import iti.student.finalproject.ui.theme.WeatherGradientTop
import iti.student.finalproject.ui.theme.WeatherPrimaryDark
import iti.student.finalproject.ui.theme.WeatherSecondaryText
import iti.student.finalproject.worker.AlertCleanupWorker
import iti.student.finalproject.worker.AlertNotificationWorker
import iti.student.finalproject.worker.AlertScheduler
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

class AlertRingingActivity : ComponentActivity() {

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show over lock screen + turn on screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val alertId = intent.getIntExtra(EXTRA_ALERT_ID, -1)
        val alertType = intent.getStringExtra(EXTRA_ALERT_TYPE).orEmpty()
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val body = intent.getStringExtra(EXTRA_BODY).orEmpty()
        val action = intent.getStringExtra(EXTRA_ACTION)

        // Handle actions when launched from notification buttons
        if (action == ACTION_DISMISS) {
            dismissAlert(alertId)
            finish()
            return
        }
        if (action == ACTION_SNOOZE) {
            snoozeAlert(alertId, alertType, title, body)
            finish()
            return
        }

        // If alerts are disabled, close immediately
        if (!NotificationPrefs.areNotificationsEnabled(this) || !NotificationPrefs.isSoundAlertsEnabled(this)) {
            finish()
            return
        }

        startSound()

        setContent {
            FinalProjectTheme(dynamicColor = false) {
                AlarmScreen(
                    title = if (title.isNotBlank()) title else getString(R.string.weather_alert),
                    body = if (body.isNotBlank()) body else getString(R.string.alert_time_active),
                    onSnooze = {
                        stopSound()
                        snoozeAlert(alertId, alertType, title, body)
                        finish()
                    },
                    onDismiss = {
                        stopSound()
                        dismissAlert(alertId)
                        finish()
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val alertId = intent.getIntExtra(EXTRA_ALERT_ID, -1)
        val alertType = intent.getStringExtra(EXTRA_ALERT_TYPE).orEmpty()
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val body = intent.getStringExtra(EXTRA_BODY).orEmpty()
        when (intent.getStringExtra(EXTRA_ACTION)) {
            ACTION_DISMISS -> {
                stopSound()
                dismissAlert(alertId)
                finish()
            }
            ACTION_SNOOZE -> {
                stopSound()
                snoozeAlert(alertId, alertType, title, body)
                finish()
            }
        }
    }

    override fun onDestroy() {
        stopSound()
        super.onDestroy()
    }

    private fun startSound() {
        if (player != null) return
        player = MediaPlayer.create(this, R.raw.sound)?.apply {
            isLooping = true
            setVolume(1f, 1f)
            start()
        }
    }

    private fun stopSound() {
        player?.stop()
        player?.release()
        player = null
    }

    private fun dismissAlert(alertId: Int) {
        if (alertId < 0) return
        AlertNotificationWorker.cancelNotification(applicationContext, alertId)
        AlertScheduler(applicationContext).cancelAlert(alertId)
        val app = application as? iti.student.finalproject.WeatherApp ?: return
        lifecycleScope.launch {
            app.database.alertDao().deleteById(alertId)
        }
    }

    private fun snoozeAlert(alertId: Int, alertType: String, title: String, body: String) {
        if (alertId < 0) return
        AlertNotificationWorker.cancelNotification(applicationContext, alertId)
        // Schedule a one-time notification again after 5 minutes (does not change DB range)
        val delayMs = TimeUnit.MINUTES.toMillis(DEFAULT_SNOOZE_MINUTES)
        val data = Data.Builder()
            .putInt(AlertNotificationWorker.KEY_ALERT_ID, alertId)
            .putString(AlertNotificationWorker.KEY_ALERT_TYPE, alertType)
            .putString(AlertNotificationWorker.KEY_TITLE, title)
            .putString(AlertNotificationWorker.KEY_BODY, body)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertNotificationWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            WORK_NAME_SNOOZE_PREFIX + alertId,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val EXTRA_ALERT_ID = "alert_id"
        const val EXTRA_ALERT_TYPE = "alert_type"
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
        const val EXTRA_ACTION = "action"

        const val ACTION_SNOOZE = "action_snooze"
        const val ACTION_DISMISS = "action_dismiss"

        private const val WORK_NAME_SNOOZE_PREFIX = "alert_snooze_"
        private const val DEFAULT_SNOOZE_MINUTES = 5L
    }
}

@androidx.compose.runtime.Composable
private fun AlarmScreen(
    title: String,
    body: String,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WeatherGradientTop
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = WeatherPrimaryDark
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = body,
                fontSize = 14.sp,
                color = WeatherSecondaryText
            )
            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onSnooze,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WeatherAccentBlue),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.snooze), color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WeatherAccentRed),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.dismiss), color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

