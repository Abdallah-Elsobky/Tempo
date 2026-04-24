package iti.student.finalproject.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import iti.student.finalproject.R
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.ui.theme.*

@Composable
fun MainWeather(weather: WeatherModel, temperatureUnit: TemperatureUnit) {
    val unitSymbol = if (temperatureUnit == TemperatureUnit.FAHRENHEIT) "°F" else "°C"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Log.d("loco", weather.iconUrl)
        AsyncImage(
            model = weather.iconUrl,
            contentDescription = "Weather icon",
            modifier = Modifier.size(160.dp),
            placeholder = painterResource(R.drawable.ic_cloud),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Light,
                        color = WeatherPrimaryDark
                    )
                ) {
                    append(weather.temp.toString())
                }
                withStyle(
                    SpanStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light,
                        color = WeatherPrimaryMedium
                    )
                ) {
                    append(unitSymbol)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = weather.description,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = WeatherPrimaryMedium,
            modifier = Modifier.padding(horizontal = 48.dp),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}
