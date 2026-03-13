package iti.student.finalproject.presentation.screen.home

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import iti.student.finalproject.R
import iti.student.finalproject.data.remote.api.RetrofitInstance
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSourceImpl
import iti.student.finalproject.data.repository.WeatherRepositoryImpl
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.presentation.components.HourlyForecast
import iti.student.finalproject.presentation.components.MainWeather
import iti.student.finalproject.presentation.components.WeatherDetails
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModelFactory
import iti.student.finalproject.ui.theme.*
import iti.student.finalproject.utils.ResultState


@Composable
fun HomeScreen(viewModel: WeatherViewModel, onNavigateToForecast: () -> Unit = {}) {

    val weatherState by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()

    when {
        weatherState is ResultState.Loading || forecastState is ResultState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        weatherState is ResultState.Error -> {
            Box(Modifier.fillMaxSize()) {
                Text((weatherState as ResultState.Error).message)
            }
        }

        forecastState is ResultState.Error -> {
            Box(Modifier.fillMaxSize()) {
                Text((forecastState as ResultState.Error).message)
            }
        }

        weatherState is ResultState.Success && forecastState is ResultState.Success -> {

            val weather = (weatherState as ResultState.Success).data
            val forecast = (forecastState as ResultState.Success).data

            HomeContent(
                weather,
                forecast,
                onNavigateToForecast
            )
        }
    }
}

@Composable
fun HomeContent(
    weather: WeatherModel,
    forecast: List<ForecastModel>,
    onNavigateToForecast: () -> Unit = {}
) {
    Log.d("loco", forecast.toString())
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
                .padding(top = 24.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = WeatherAccentBlue
                )
                Text(
                    text = " ${weather.city}, ${weather.country}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WeatherPrimaryDark,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            MainWeather(weather)

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                color = WeatherDivider,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeatherDetails(weather)

            Spacer(modifier = Modifier.height(32.dp))

            ForecastItem(onNavigateToForecast)

            Spacer(modifier = Modifier.height(20.dp))

            HourlyForecast(forecast)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun ForecastItem(onNavigateToForecast: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_cloud),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = WeatherSecondaryText
            )
            Text(
                text = "  Forecast",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = WeatherPrimaryDark,
                letterSpacing = 1.sp
            )
        }
        Text(
            text = "5 DAYS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = WeatherAccentBlue,
            letterSpacing = 0.5.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = WeatherAccentBlue,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onNavigateToForecast() }
                .padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}