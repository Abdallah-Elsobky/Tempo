package iti.student.finalproject.presentation.components

import android.util.Log
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
import coil3.compose.AsyncImage
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.ui.theme.*

@Composable
fun HourlyForecast(forecast: List<ForecastModel>, temperatureUnit: TemperatureUnit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(forecast.take(8)) { index, item ->
            HourlyCard(item, index, temperatureUnit)
        }
    }
}

@Composable
private fun HourlyCard(item: ForecastModel, index: Int, temperatureUnit: TemperatureUnit) {
    val temperatureSymbol = if (temperatureUnit == TemperatureUnit.FAHRENHEIT) "°F" else "°C"
    val backgroundColor = if (index == 0) WeatherActiveCard else WeatherSurfaceCard
    val contentColor = if (index == 0) Color.White else WeatherPrimaryDark
    val secondaryColor = if (index == 0) Color.White.copy(alpha = 0.8f) else WeatherSecondaryText

    Surface(
        modifier = Modifier
            .width(76.dp)
            .height(160.dp),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Text(
                text = if (index == 0) "Now" else item.dayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryColor
            )
            Text(
                text = item.dayTime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryColor
            )

            Spacer(modifier = Modifier.height(15.dp))
            AsyncImage(
                model = item.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                placeholder = painterResource(R.drawable.ic_cloud),
                error = painterResource(R.drawable.ic_location)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "${item.temperature}$temperatureSymbol",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
