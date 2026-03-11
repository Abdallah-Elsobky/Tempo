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
import iti.student.finalproject.ui.theme.WeatherAccentBlue
import iti.student.finalproject.ui.theme.WeatherOrange
import iti.student.finalproject.ui.theme.WeatherPrimaryDark
import iti.student.finalproject.ui.theme.WeatherSecondaryText

@Composable
fun WeatherDetails() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherDetailItem(
            value = "11km/hr",
            iconRes = R.drawable.ic_wind,
            iconTint = WeatherAccentBlue
        )
        WeatherDetailItem(
            value = "02%",
            iconRes = R.drawable.ic_drop_water,
            iconTint = WeatherAccentBlue
        )
        WeatherDetailItem(
            value = "8hr",
            iconRes = R.drawable.ic_sun,
            iconTint = WeatherOrange
        )
    }
}

@Composable
private fun WeatherDetailItem(
    value: String,
    iconRes: Int,
    iconTint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
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
            color = WeatherPrimaryDark
        )
    }
}
