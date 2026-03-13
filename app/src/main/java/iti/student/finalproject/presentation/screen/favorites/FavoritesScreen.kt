package iti.student.finalproject.presentation.screen.favorites

import androidx.compose.foundation.background
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.presentation.utils.getTemperatureColor

import iti.student.finalproject.ui.theme.*

@Composable
fun FavoritesScreen(onAddNewCity: () -> Unit = {}) {
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
            LocationItemCard(24.0f)
            LocationItemCard(13f)
            LocationItemCard(-1f)
            LocationItemCard(32f)
            LocationItemCard(50f)
            LocationItemCard(80f)
            LocationItemCard(9f)
            LocationItemCard(44f)
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = {
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
fun LocationItemCard(temp: Float) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(WeatherSurfaceCard)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(getTemperatureColor(temp))
            )
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(getTemperatureColor(temp).copy(0.1f))
            ) {
                Icon(
                    painterResource(R.drawable.ic_sun),
                    contentDescription = null,
                    tint = getTemperatureColor(temp),
                    modifier = Modifier
                        .size(19.dp)
                        .align(Alignment.Center)
                )
            }
            Column {
                Text("London", fontWeight = FontWeight.Bold)
                Text("United Kingdom", fontSize = 12.sp, color = WeatherSecondaryText)
            }
            Spacer(Modifier.width(40.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text("${temp}°", fontWeight = FontWeight.Bold)
            }
        }
    }
}