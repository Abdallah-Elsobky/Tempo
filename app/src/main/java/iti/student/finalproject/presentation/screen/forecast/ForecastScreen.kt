package iti.student.finalproject.presentation.screen.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.ui.theme.*

private data class DailyForecast(
    val dayName: String,
    val date: String,
    val condition: String,
    val iconRes: Int,
    val lowTemp: Int,
    val highTemp: Int,
    val isToday: Boolean = false
)

private data class WeatherInfoCard(
    val label: String,
    val value: String,
    val description: String,
    val iconRes: Int,
    val iconTint: Color
)

@Composable
fun ForecastScreen() {
    val dailyForecasts = listOf(
        DailyForecast("Today", "Mar 3", "Partly Cloudy", R.drawable.ic_cloud, 65, 75, isToday = true),
        DailyForecast("Wed", "Mar 4", "Sunny", R.drawable.ic_sun, 64, 72),
        DailyForecast("Thu", "Mar 5", "Rainy", R.drawable.ic_drop_water, 62, 70),
        DailyForecast("Fri", "Mar 6", "Stormy", R.drawable.ic_wind, 62, 69),
        DailyForecast("Sat", "Mar 7", "Sunny", R.drawable.ic_sun, 64, 71),
    )

    val infoCards = listOf(
        WeatherInfoCard("VISIBILITY", "10 mi", "Good visibility", R.drawable.ic_sun, WeatherOrange),
        WeatherInfoCard("PRESSURE", "1015 hPa", "Falling slightly", R.drawable.ic_wind, WeatherPurple),
        WeatherInfoCard("UV INDEX", "4", "Moderate", R.drawable.ic_sun, WeatherYellow),
        WeatherInfoCard("DEW POINT", "56°", "Comfortable", R.drawable.ic_drop_water, WeatherTeal),
    )

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
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 100.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7-Day Forecast",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = WeatherPrimaryDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    dailyForecasts.forEachIndexed { index, forecast ->
                        DailyForecastRow(forecast)
                        if (index < dailyForecasts.lastIndex) {
                            HorizontalDivider(
                                color = WeatherDivider,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in infoCards.indices step 2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoCard(
                            card = infoCards[i],
                            modifier = Modifier.weight(1f)
                        )
                        if (i + 1 < infoCards.size) {
                            InfoCard(
                                card = infoCards[i + 1],
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyForecastRow(forecast: DailyForecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(56.dp)) {
            Text(
                text = forecast.dayName,
                fontSize = 14.sp,
                fontWeight = if (forecast.isToday) FontWeight.Bold else FontWeight.SemiBold,
                color = WeatherPrimaryDark
            )
            Text(
                text = forecast.date,
                fontSize = 11.sp,
                color = WeatherSecondaryText
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(forecast.iconRes),
            contentDescription = forecast.condition,
            modifier = Modifier.size(28.dp),
            tint = when (forecast.iconRes) {
                R.drawable.ic_sun -> WeatherYellow
                R.drawable.ic_drop_water -> WeatherAccentBlue
                R.drawable.ic_wind -> WeatherAccentBlue
                else -> WeatherSecondaryText
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = forecast.condition,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = WeatherSecondaryText,
            modifier = Modifier.width(64.dp)
        )

        Text(
            text = "${forecast.lowTemp}°",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = WeatherSecondaryText
        )

        Spacer(modifier = Modifier.width(8.dp))

        TemperatureBar(
            low = forecast.lowTemp,
            high = forecast.highTemp,
            modifier = Modifier
                .weight(1f)
                .height(5.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${forecast.highTemp}°",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = WeatherPrimaryDark
        )
    }
}

@Composable
private fun TemperatureBar(
    low: Int,
    high: Int,
    modifier: Modifier = Modifier
) {

    // TODO Fix this Formula
    val overallMin = 55
    val overallMax = 80
    val range = (overallMax - overallMin).toFloat()
    val startFraction = ((low - overallMin) / range).coerceIn(0f, 1f)
    val endFraction = ((high - overallMin) / range).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(WeatherDivider)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(endFraction)
                .padding(start = (startFraction * 100).dp.coerceAtMost(50.dp))
                .fillMaxSize()
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            WeatherAccentBlue.copy(alpha = 0.6f),
                            WeatherAccentBlue
                        )
                    )
                )
        )
    }
}

@Composable
private fun InfoCard(
    card: WeatherInfoCard,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .border(
                width = 0.dp,
                color = WeatherCardBorder,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = WeatherSurfaceCard
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = card.iconTint.copy(alpha = 0.1f)
                ) {
                    Icon(
                        painter = painterResource(card.iconRes),
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                        tint = card.iconTint
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = card.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WeatherSecondaryText,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = card.value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = WeatherPrimaryDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = card.description,
                fontSize = 12.sp,
                color = WeatherSecondaryText
            )
        }
    }
}
