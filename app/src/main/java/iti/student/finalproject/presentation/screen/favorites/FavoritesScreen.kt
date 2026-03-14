package iti.student.finalproject.presentation.screen.favorites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import iti.student.finalproject.R
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.domain.mapper.FavLocationMapper.modelToEntity
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.presentation.screen.FavViewModel
import iti.student.finalproject.presentation.utils.getTemperatureColor

import iti.student.finalproject.ui.theme.*

@Composable
fun FavoritesScreen(
    favViewModel: FavViewModel,
    onAddNewCity: () -> Unit = {},
    onFavClick: (favLocation: FavLocationModel) -> Unit = {}
) {
    val favState by favViewModel.favorites.collectAsState()

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
                text = "Favorites",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = WeatherPrimaryDark
            )
            favState.forEach { favLocation ->
                LocationItemCard(
                    favLocation,
                    onDeleted = {
                        favViewModel.deleteFavorite(modelToEntity(it))
                    },
                    onNavigateToForecast = {
                        onFavClick.invoke(favLocation)
                    })
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = {
                //TODO dummy data

                favViewModel.insertFavorite(
                    FavLocationEntity(
                        "cairo1",
                        "egypt",
                        30f,
                        "https://maps.gstatic.com/weather/v1/snow_showers.svg",
                        34.0,
                        35.0
                    )
                )
                favViewModel.insertFavorite(
                    FavLocationEntity(
                        "cairo2",
                        "egypt",
                        20f,
                        "https://maps.gstatic.com/weather/v1/cloudy.svg",
                        32.0,
                        21.0
                    )
                )
                favViewModel.insertFavorite(
                    FavLocationEntity(
                        "cairo3",
                        "egypt",
                        40f,
                        "https://maps.gstatic.com/weather/v1/clear.svg",
                        50.0,
                        21.0
                    )
                )
                favViewModel.insertFavorite(
                    FavLocationEntity(
                        "cairo4",
                        "egypt",
                        10f,
                        "https://maps.gstatic.com/weather/v1/showers.svg",
                        40.0,
                        35.0
                    )
                )
                onAddNewCity.invoke()
            },
            containerColor = WeatherAccentBlue,
        ) {
            Icon(
                painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}


@Composable
fun LocationItemCard(
    favLocation: FavLocationModel,
    onDeleted: (FavLocationModel) -> Unit = {},
    onNavigateToForecast: (favLocation: FavLocationModel) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable(true) {
                onNavigateToForecast.invoke(favLocation)
            }
            .fillMaxWidth()
            .height(95.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WeatherSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(getTemperatureColor(favLocation.temp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            AsyncImage(
                model = favLocation.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(favLocation.country, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(favLocation.name, fontSize = 13.sp, color = WeatherSecondaryText)
            }

            Text("${favLocation.temp}°", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "delete",
                    tint = Color.Red
                )
            }
        }
    }

    if (showDialog) {
        DeleteFavoriteDialog(favLocation, {
            showDialog = false
            onDeleted.invoke(favLocation)
        }, {
            showDialog = false
        })
    }
}

@Composable
fun DeleteFavoriteDialog(
    favLocation: FavLocationModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WeatherSurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Delete Favorite?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WeatherPrimaryDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Are you sure you want to remove ${favLocation.name} from favorites?",
                    fontSize = 14.sp,
                    color = WeatherSecondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onConfirm() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }

                    OutlinedButton(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = WeatherPrimaryDark
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}