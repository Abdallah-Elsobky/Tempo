package iti.student.finalproject.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.AppSettings
import iti.student.finalproject.domain.model.LocationMode
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.presentation.components.HourlyForecast
import iti.student.finalproject.presentation.components.MainWeather
import iti.student.finalproject.presentation.components.WeatherDetails
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.ui.theme.*
import iti.student.finalproject.utils.ResultState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    settings: AppSettings,
    onNavigateToForecast: (lon: Float, lat: Float) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var showPermissionSettingsDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestCurrentLocationAndLoadWeather(viewModel, context, settings)
        } else {
            showPermissionSettingsDialog = true
        }
    }

    LaunchedEffect(settings.locationMode, settings.language, settings.apiUnits) {
        if (settings.locationMode == LocationMode.MAP) {
            viewModel.loadWeather(
                settings.mapLatitude,
                settings.mapLongitude,
                settings.language.code,
                settings.apiUnits
            )
            viewModel.loadForecast(
                settings.mapLatitude,
                settings.mapLongitude,
                settings.language.code,
                settings.apiUnits
            )
        } else {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                requestCurrentLocationAndLoadWeather(viewModel, context, settings)
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

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
                settings,
                { onNavigateToForecast(weather.lon, weather.lat) }
            )
        }
    }

    if (showPermissionSettingsDialog && activity != null) {
        AlertDialog(
            onDismissRequest = { showPermissionSettingsDialog = false },
            title = { Text(stringResource(R.string.location_permission_needed)) },
            text = {
                Text(
                    stringResource(R.string.location_permission_message)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionSettingsDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionSettingsDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun HomeContent(
    weather: WeatherModel,
    forecast: List<ForecastModel>,
    settings: AppSettings,
    onNavigateToForecast: () -> Unit = {}
) {
    Log.d("loco", forecast.toString())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            MainWeather(weather, settings.temperatureUnit)

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeatherDetails(weather, settings.windSpeedUnit)

            Spacer(modifier = Modifier.height(32.dp))

            ForecastItem(onNavigateToForecast)

            Spacer(modifier = Modifier.height(20.dp))

            HourlyForecast(forecast, settings.temperatureUnit)

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
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = "  ${stringResource(R.string.forecast)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 1.sp
            )
        }
        Text(
            text = stringResource(R.string.five_day_forecast),
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

@RequiresApi(Build.VERSION_CODES.O)
private fun requestCurrentLocationAndLoadWeather(
    viewModel: WeatherViewModel,
    context: Context,
    settings: AppSettings
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    viewModel.loadWeather(lat, lon, settings.language.code, settings.apiUnits)
                    viewModel.loadForecast(lat, lon, settings.language.code, settings.apiUnits)
                } else {
                    viewModel.loadWeather(35.0, 39.0, settings.language.code, settings.apiUnits)
                    viewModel.loadForecast(35.0, 39.0, settings.language.code, settings.apiUnits)
                }
            }
            .addOnFailureListener {
                viewModel.loadWeather(35.0, 39.0, settings.language.code, settings.apiUnits)
                viewModel.loadForecast(35.0, 39.0, settings.language.code, settings.apiUnits)
            }
    } catch (e: SecurityException) {

    }
}