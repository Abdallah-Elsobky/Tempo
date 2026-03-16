package iti.student.finalproject.presentation.screen.favorites.map

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.presentation.screen.FavViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.favorites.map.components.Header
import iti.student.finalproject.presentation.screen.favorites.map.components.SearchBar
import iti.student.finalproject.presentation.screen.favorites.map.components.SelectCityBottomSheet
import iti.student.finalproject.presentation.screen.favorites.map.components.StreetMapView
import iti.student.finalproject.presentation.utils.getTemperatureColor
import iti.student.finalproject.ui.theme.*
import iti.student.finalproject.utils.DrawableHelper
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewFavScreen(
    weatherModel: WeatherViewModel,
    favViewModel: FavViewModel,
    onBackClick: () -> Unit = {}
) {
    var showSheet by remember { mutableStateOf(true) }
    var selectedLat by remember { mutableStateOf(30.0) }
    var selectedLon by remember { mutableStateOf(31.0) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        weatherModel.loadWeather(selectedLat, selectedLon)
        weatherModel.loadPossibleCities("cairo")
    }

    val weatherState by weatherModel.weatherState.collectAsState()
    val cityState by weatherModel.possibleCitiesState.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.length < 3) return@LaunchedEffect
        delay(500)
        weatherModel.loadPossibleCities(searchQuery)
    }

    LaunchedEffect(cityState) {
        val state = cityState
        if (state is ResultState.Success && state.data.isNotEmpty()) {
            val city = state.data.first()
            selectedLat = city.lat
            selectedLon = city.lon
            weatherModel.loadWeather(selectedLat, selectedLon)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        StreetMapView(selectedLat, selectedLon) { lat, lon ->
            selectedLat = lat
            selectedLon = lon
            weatherModel.loadWeather(lat, lon)
            showSheet = true
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(0.3f), WeatherGradientBottom.copy(0.1f))
                    )
                )
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Header(onBackClick)
            SearchBar{
                searchQuery = it
            }
            if (showSheet) {
                SelectCityBottomSheet(
                    weatherState = weatherState,
                    onDismiss = { showSheet = false },
                    onAddFavourite = {
                        favViewModel.insertFavorite(it)
                        showSheet = false
                    }
                )
            }
        }
    }
}