package iti.student.finalproject.presentation.screen.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.NotificationPrefs
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.AppLanguage
import iti.student.finalproject.domain.model.AppTheme
import iti.student.finalproject.domain.model.LocationMode
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.domain.model.WindSpeedUnit
import iti.student.finalproject.ui.theme.*

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settings by settingsViewModel.settings.collectAsState()

    var notificationsEnabled by rememberSaveable {
        mutableStateOf(NotificationPrefs.areNotificationsEnabled(context))
    }
    var soundAlertsEnabled by rememberSaveable {
        mutableStateOf(NotificationPrefs.isSoundAlertsEnabled(context))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WeatherGradientTop, WeatherGradientBottom)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = WeatherPrimaryDark
            )

            SettingsSection(title = "Localization") {
                SettingCardItem(
                    title = "Language",
                    subtitle = "Choose your language"
                ) {
                    SegmentedControl(
                        options = listOf("en", "ar"),
                        selectedIndex = if (settings.language == AppLanguage.ENGLISH) 0 else 1,
                        onOptionSelected = { index ->
                            settingsViewModel.setLanguage(
                                if (index == 0) AppLanguage.ENGLISH else AppLanguage.ARABIC
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                SettingCardItem(
                    title = "Theme",
                    subtitle = "Choose your Theme"
                ) {
                    SegmentedControl(
                        options = listOf("light", "dark"),
                        selectedIndex = if (settings.theme == AppTheme.LIGHT) 0 else 1,
                        onOptionSelected = { index ->
                            settingsViewModel.setTheme(
                                if (index == 0) AppTheme.LIGHT else AppTheme.DARK
                            )
                        }
                    )
                }
            }

            SettingsSection(title = "UNITS") {
                SettingCardItem(
                    title = "Temperature",
                    subtitle = "Choose your unit"
                ) {
                    SegmentedControl(
                        options = listOf("°C", "°F"),
                        selectedIndex = if (settings.temperatureUnit == TemperatureUnit.CELSIUS) 0 else 1,
                        onOptionSelected = { index ->
                            settingsViewModel.setTemperatureUnit(
                                if (index == 0) TemperatureUnit.CELSIUS else TemperatureUnit.FAHRENHEIT
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SettingCardItem(
                    title = "Wind Speed",
                    subtitle = "Display speed in unit"
                ) {
                    SegmentedControl(
                        options = listOf("km/h", "mph"),
                        selectedIndex = if (settings.windSpeedUnit == WindSpeedUnit.KMH) 0 else 1,
                        onOptionSelected = { index ->
                            settingsViewModel.setWindSpeedUnit(
                                if (index == 0) WindSpeedUnit.KMH else WindSpeedUnit.MPH
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SettingCardItem(
                    title = "Location",
                    subtitle = "Get location method"
                ) {
                    SegmentedControl(
                        options = listOf("GPS", "MAP"),
                        selectedIndex = if (settings.locationMode == LocationMode.GPS) 0 else 1,
                        onOptionSelected = { index ->
                            settingsViewModel.setLocationMode(
                                if (index == 0) LocationMode.GPS else LocationMode.MAP
                            )
                        }
                    )
                }
            }

            SettingsSection(title = "NOTIFICATIONS") {
                ToggleSettingRow(
                    title = "Enable Notifications",
                    subtitle = "Stay informed about weather",
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        NotificationPrefs.setNotificationsEnabled(context, it)
                    },
                    icon = R.drawable.ic_notification,
                    color = WeatherOrange
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = WeatherDivider
                )

                ToggleSettingRow(
                    title = "Alert Sound",
                    subtitle = "Play sound for alert notifications",
                    checked = soundAlertsEnabled && notificationsEnabled,
                    enabled = notificationsEnabled,
                    onCheckedChange = {
                        soundAlertsEnabled = it
                        NotificationPrefs.setSoundAlertsEnabled(context, it)
                    },
                    icon = R.drawable.ic_notification,
                    color = WeatherPurple
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = WeatherSecondaryText,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(WeatherSurfaceCard)
                .border(
                    width = 1.dp,
                    color = WeatherCardBorder,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 14.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingCardItem(
    title: String,
    subtitle: String,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = WeatherPrimaryDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = WeatherSecondaryText
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            control()
        }
    }
}

@Composable
private fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(WeatherDivider.copy(alpha = 0.4f))
            .border(
                width = 1.dp,
                color = WeatherCardBorder,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) WeatherAccentBlue else Color.Transparent
                    )
                    .clickable { onOptionSelected(index) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else WeatherPrimaryDark
                )
            }
        }
    }
}

@Composable
private fun ToggleSettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    icon: Int,
    color: Color
) {
    val effectiveAlpha by remember(enabled) { mutableStateOf(if (enabled) 1f else 0.4f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(.1f))
            ) {
                Icon(
                    painterResource(icon),
                    null,
                    tint = color,
                    modifier = Modifier
                        .size(19.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WeatherPrimaryDark.copy(alpha = effectiveAlpha)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = WeatherSecondaryText.copy(alpha = effectiveAlpha)
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = { if (enabled) onCheckedChange(it) },
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = WeatherAccentBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = WeatherDivider,
                uncheckedBorderColor = WeatherCardBorder
            )
        )
    }
}


