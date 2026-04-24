package iti.student.finalproject.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.domain.model.WindSpeedUnit
import iti.student.finalproject.ui.theme.*

@Composable
fun WeatherDetails(weather: WeatherModel, windSpeedUnit: WindSpeedUnit) {
    val windValue = when (windSpeedUnit) {
        WindSpeedUnit.KMH -> "${weather.windSpeed * 3.6f} km/h"
        WindSpeedUnit.MPH -> "${weather.windSpeed} mph"
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherDetailItem(
            value = windValue,
            iconRes = R.drawable.ic_wind,
            iconTint = WeatherAccentBlue
        )
        WeatherDetailItem(
            value = "${weather.clouds}%",
            iconRes = R.drawable.ic_cloud,
            iconTint = WeatherPrimaryMedium
        )
        WeatherDetailItem(
            value = "${weather.humidity}%",
            iconRes = R.drawable.ic_drop_water,
            iconTint = WeatherAccentBlueLight
        )
        WeatherDetailItem(
            value = "${weather.pressure} hPa",
            iconRes = R.drawable.ic_pressure,
            iconTint = WeatherPurple
        )
    }
}

@Composable
private fun WeatherDetailItem(
    value: String,
    iconRes: Int,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(10.dp),
            color = iconTint.copy(alpha = 0.1f)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = iconTint
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
