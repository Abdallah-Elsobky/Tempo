package iti.student.finalproject.presentation.screen.home.forecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.AppSettings
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.domain.model.WindSpeedUnit
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.ui.theme.*
import iti.student.finalproject.utils.NumberUtils.roundTo
import iti.student.finalproject.utils.ResultState

data class WeatherInfoCard(
    val label: String,
    var value: String,
    val description: String,
    val iconRes: Int,
    val iconTint: Color
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    lon: Float,
    lat: Float,
    viewModel: WeatherViewModel,
    settings: AppSettings,
    onBackClick: () -> Unit
) {
    val infoCards = listOf(
        WeatherInfoCard("WIND", "10 mi", "Good visibility", R.drawable.ic_wind, WeatherAccentBlue),
        WeatherInfoCard("HUMIDITY", "4", "Moderate", R.drawable.ic_drop_water, WeatherTeal),
        WeatherInfoCard("CLOUD", "56°", "Comfortable", R.drawable.ic_cloud, WeatherPrimaryMedium),
        WeatherInfoCard(
            "PRESSURE",
            "1015 hPa",
            "Falling slightly",
            R.drawable.ic_pressure,
            WeatherPurple
        ),
    )

    LaunchedEffect(lat, lon, settings.language, settings.apiUnits) {
        viewModel.loadForecast(lat.toDouble(), lon.toDouble(), settings.language.code, settings.apiUnits)
    }

    val forecastState by viewModel.forecastState.collectAsState()

    when (forecastState) {
        is ResultState.Error -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            Box(Modifier.fillMaxSize()) {
                Text((forecastState as ResultState.Error).message)
            }
        }

        is ResultState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is ResultState.Success<*> -> {
            ForecastContent(
                forecastState as ResultState.Success<List<ForecastModel>>,
                infoCards,
                settings,
                onBackClick
            )
        }
    }
}


@Composable
fun ForecastContent(
    forecastState: ResultState.Success<List<ForecastModel>>,
    infoCards: List<WeatherInfoCard>,
    settings: AppSettings,
    onBackClick: () -> Unit
) {

    val forecasts = forecastState.data.filter { it.dayTime == "12 AM" }
    val windValue = if (settings.windSpeedUnit == WindSpeedUnit.KMH) {
        "${roundTo(forecasts.get(0).windSpeed * 3.6f, 1)} km/h"
    } else {
        "${roundTo(forecasts.get(0).windSpeed, 1)} mph"
    }
    val temperatureSymbol = if (settings.temperatureUnit == TemperatureUnit.FAHRENHEIT) "°F" else "°C"
    infoCards.get(0).value = windValue
    infoCards.get(1).value = "${forecasts.get(0).humidity} %"
    infoCards.get(2).value = "${forecasts.get(0).clouds} %"
    infoCards.get(3).value = "${forecasts.get(0).pressure} hPa"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 100.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(80.dp)
            ) {
                Box(
                    Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable(true) {
                            onBackClick.invoke()
                        }
                ) {
                    Icon(
                        painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(19.dp)
                            .align(Alignment.Center)
                    )
                }
                Text(
                    modifier = Modifier.weight(2f),
                    text = stringResource(R.string.five_day_forecast),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    forecasts.forEachIndexed { index, forecast ->
                        DailyForecastRow(index, forecast, temperatureSymbol)
                        if (index < forecasts.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
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
private fun DailyForecastRow(index: Int, forecast: ForecastModel, temperatureSymbol: String) {
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
                fontWeight = if (index == 0) FontWeight.Bold else FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = forecast.dayDate,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = forecast.iconUrl,
            contentDescription = forecast.description,
            modifier = Modifier.size(28.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = forecast.description,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.width(64.dp)
        )

        Text(
            text = "${forecast.minTemperature}$temperatureSymbol",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        TemperatureBar(
            modifier = Modifier
                .weight(1f)
                .height(5.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${forecast.maxTemperature}$temperatureSymbol",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TemperatureBar(
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            WeatherAccentBlue.copy(alpha = 0.2f),
                            WeatherAccentBlue.copy(alpha = 0.6f),
                            WeatherAccentBlue.copy(alpha = 0.6f),
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
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = card.value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
