package iti.student.finalproject.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import iti.student.finalproject.ui.theme.*

data class HourlyItem(
    val time: String,
    val temp: String,
    val iconRes: Int,
    val isActive: Boolean = false
)

@Composable
fun HourlyForecast() {
    val items = listOf(
        HourlyItem("Now", "29°", R.drawable.ic_cloud, isActive = true),
        HourlyItem("5pm", "28°", R.drawable.ic_drop_water),
        HourlyItem("6pm", "28°", R.drawable.ic_cloud),
        HourlyItem("7pm", "27°", R.drawable.ic_wind),
        HourlyItem("8pm", "26°", R.drawable.ic_cloud),
        HourlyItem("9pm", "25°", R.drawable.ic_drop_water),
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(items) { _, item ->
            HourlyCard(item)
        }
    }
}

@Composable
private fun HourlyCard(item: HourlyItem) {
    val backgroundColor = if (item.isActive) WeatherActiveCard else WeatherSurfaceCard
    val contentColor = if (item.isActive) Color.White else WeatherPrimaryDark
    val secondaryColor = if (item.isActive) Color.White.copy(alpha = 0.8f) else WeatherSecondaryText

    Surface(
        modifier = Modifier.width(76.dp),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = if (item.isActive) 8.dp else 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Text(
                text = item.time,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = if (item.isActive) Color.White else WeatherActiveCard
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.temp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
