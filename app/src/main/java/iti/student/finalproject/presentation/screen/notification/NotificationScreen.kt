package iti.student.finalproject.presentation.screen.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.AlertModel
import iti.student.finalproject.presentation.screen.AlertViewModel
import iti.student.finalproject.presentation.screen.notification.components.AddAlertBottomSheet
import iti.student.finalproject.ui.theme.WeatherAccentBlue
import iti.student.finalproject.ui.theme.WeatherAccentRed
import iti.student.finalproject.ui.theme.WeatherCardBorder
import iti.student.finalproject.ui.theme.WeatherTeal
import iti.student.finalproject.ui.theme.WeatherYellow
import iti.student.finalproject.utils.TimeUtils.dateFormatTime
import iti.student.finalproject.utils.TimeUtils.formatTime

data class NotificationDisplayItem(
    val alert: AlertModel,
    val title: String,
    val description: String,
    val iconResId: Int,
    val iconTint: Color,
    val iconBackgroundTint: Color,
)

@Composable
fun AlertModel.toNotificationDisplayItem(): NotificationDisplayItem {
    val (title, description, iconResId, iconTint, iconBg) = when (alertType) {
        AlertType.RAIN.name -> NotificationDisplayData(
            stringResource(R.string.rain_alert),
            stringResource(R.string.rain_alert_desc),
            R.drawable.ic_drop_water,
            WeatherTeal,
            WeatherTeal.copy(alpha = 0.12f)
        )

        AlertType.STORM.name -> NotificationDisplayData(
            stringResource(R.string.storm_alert),
            stringResource(R.string.storm_alert_desc),
            R.drawable.ic_wind,
            WeatherAccentRed,
            WeatherAccentRed.copy(alpha = 0.12f)
        )

        AlertType.TEMPERATURE.name -> NotificationDisplayData(
            stringResource(R.string.temperature_alert),
            stringResource(R.string.temperature_alert_desc),
            R.drawable.ic_sun,
            WeatherYellow,
            WeatherYellow.copy(alpha = 0.15f)
        )

        else -> NotificationDisplayData(
            stringResource(R.string.weather_alert),
            stringResource(R.string.weather_alert_desc),
            R.drawable.ic_cloud,
            WeatherAccentBlue,
            WeatherAccentBlue.copy(alpha = 0.12f)
        )
    }
    return NotificationDisplayItem(
        alert = this,
        title = title,
        description = description,
        iconResId = iconResId,
        iconTint = iconTint,
        iconBackgroundTint = iconBg
    )
}

private data class NotificationDisplayData(
    val title: String,
    val description: String,
    val iconResId: Int,
    val iconTint: Color,
    val iconBackgroundTint: Color
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(alertViewModel: AlertViewModel) {
    val alertState by alertViewModel.alerts.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    var alertToDelete by remember { mutableStateOf<AlertModel?>(null) }

    val displayItems = alertState.map { it.toNotificationDisplayItem() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 100.dp)
        ) {
            Text(
                text = stringResource(R.string.notifications),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.tap_alert_remove),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (displayItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_alerts_yet),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = displayItems,
                        key = { it.alert.id }
                    ) { item ->
                        NotificationItem(
                            item = item,
                            onClick = { alertToDelete = item.alert }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { showSheet = true },
            containerColor = WeatherAccentBlue,
        ) {
            Icon(
                painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = Color.White
            )
        }

        if (showSheet) {
            AddAlertBottomSheet(
                onDismiss = { showSheet = false },
                onSave = { str, end, type ->
                    val alertItem =
                        AlertModel(startDate = str, endDate = end, alertType = type.name)
                    alertViewModel.insertAlert(alertItem)
                    showSheet = false
                }
            )
        }

        alertToDelete?.let { alert ->
            DeleteAlertDialog(
                alertDisplayTitle = displayItems.find { it.alert.id == alert.id }?.title
                    ?: stringResource(R.string.this_alert),
                onConfirm = {
                    alertViewModel.deleteAlert(alert)
                    alertToDelete = null
                },
                onDismiss = { alertToDelete = null }
            )
        }
    }
}

@Composable
private fun DeleteAlertDialog(
    alertDisplayTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.delete_alert),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_alert_message, alertDisplayTitle),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    contentColor = WeatherAccentRed
                )
            ) {
                Text(stringResource(R.string.delete), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurface)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationItem(item: NotificationDisplayItem, onClick: () -> Unit = {}) {
    val startTime = formatTime(item.alert.startDate)
    val endTime = formatTime(item.alert.endDate)
    val date = dateFormatTime(item.alert.startDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, WeatherCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                color = item.iconBackgroundTint
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(item.iconResId),
                        contentDescription = null,
                        tint = item.iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$startTime – $endTime | $date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.delete_icon_desc),
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
