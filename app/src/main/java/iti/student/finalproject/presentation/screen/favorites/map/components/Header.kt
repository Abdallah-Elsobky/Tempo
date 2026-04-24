package iti.student.finalproject.presentation.screen.favorites.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.ui.theme.WeatherDivider
import iti.student.finalproject.ui.theme.WeatherPrimaryDark
import kotlin.invoke

@Composable
fun Header(onBackClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(80.dp)
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(WeatherDivider)
                .clickable(true) {
                    onBackClick.invoke()
                }
        ) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = WeatherPrimaryDark,
                modifier = Modifier
                    .size(19.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            modifier = Modifier.weight(2f),
            text = stringResource(R.string.select_location),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = WeatherPrimaryDark
        )
    }
}