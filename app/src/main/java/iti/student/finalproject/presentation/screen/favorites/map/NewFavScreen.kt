package iti.student.finalproject.presentation.screen.favorites.map

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import iti.student.finalproject.R
import iti.student.finalproject.presentation.utils.getTemperatureColor
import iti.student.finalproject.ui.theme.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun NewFavScreen(onBackClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        StreetMapView(30.0, 31.0)
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
                    text = "Select Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WeatherPrimaryDark
                )
            }
        }
        SearchBar(Modifier.padding(10.dp))
        SelectCityDialog(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 70.dp)
            .height(55.dp).background(Color.Transparent),
        shape = RoundedCornerShape(34.dp),
        shadowElevation = 50.dp,
    ) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search by name") },
            leadingIcon = { Icon(painterResource(R.drawable.ic_search), null) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = WeatherDivider,
                unfocusedContainerColor = WeatherDivider,
                focusedContainerColor = WeatherSurfaceCard
            ),
            shape = RoundedCornerShape(34.dp)
        )

    }
}

@Composable
fun StreetMapView(
    lat: Double,
    lon: Double
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            val mapView = MapView(context)

            mapView.setMultiTouchControls(true)

            val mapController = mapView.controller
            mapController.setZoom(14.0)
            mapView.zoomController.setVisibility(
                org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
            )
            val startPoint = GeoPoint(lat, lon)
            mapController.setCenter(startPoint)

            val marker = Marker(mapView)
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Selected Location"

            mapView.overlays.add(marker)

            mapView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun SelectCityDialog(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(34.dp),
        shadowElevation = 50.dp,
        color = WeatherSurfaceCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(WeatherSurfaceCard),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(WeatherAccentBlue.copy(0.1f))
                ) {
                    Icon(
                        painterResource(R.drawable.ic_building),
                        null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        tint = WeatherAccentBlue
                    )
                }
                Column {
                    Text("Egypt", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Benha, kafr Saad,43215", fontSize = 12.sp, color = WeatherSecondaryText)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val color = getTemperatureColor(13f)
                Box(
                    Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(color.copy(0.1f))
                        .padding(4.dp)
                ) {
                    Text(
                        "Clear Sky", fontSize = 12.sp, color = color,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("Currently ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        ) {
                            append("30°C")
                        }
                    }
                )
            }
            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {

                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WeatherAccentBlue
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_fav),
                            null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("Add to Favourite")
                    }
                }
            }
        }
    }
}