package iti.student.finalproject.presentation.screen.favorites.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.domain.mapper.FavLocationMapper.weatherToLocation
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.presentation.utils.getTemperatureColor
import iti.student.finalproject.ui.theme.WeatherAccentBlue
import iti.student.finalproject.ui.theme.WeatherSecondaryText
import iti.student.finalproject.utils.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCityBottomSheet(
    weatherState: ResultState<WeatherModel>,
    onDismiss: () -> Unit,
    onAddFavourite: (location: FavLocationModel) -> Unit
) {

    when (weatherState) {
        is ResultState.Error -> {
            Box(Modifier.fillMaxSize()) {
                Text((weatherState).message, color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }

        ResultState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is ResultState.Success<*> -> {
            val weather = (weatherState as ResultState.Success).data
            DialogContent(
                weather,
                onDismiss,
                { onAddFavourite(weatherToLocation(weather)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogContent(
    weatherModel: WeatherModel,
    onDismiss: () -> Unit,
    onAddFavourite: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
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
                    Text(
                        weatherModel.city ?: stringResource(R.string.unknown),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "${weatherModel.country ?: stringResource(R.string.unknown)} | ${weatherModel.city ?: stringResource(R.string.unknown)}",
                        fontSize = 12.sp,
                        color = WeatherSecondaryText
                    )
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
                        weatherModel.description,
                        fontSize = 12.sp,
                        color = color,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append(stringResource(R.string.currently))
                        }
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        ) {
                            append("${weatherModel.temp}°C")
                        }
                    }
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAddFavourite,
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

                    Text(stringResource(R.string.add_to_favourite))
                }
            }
        }
    }
}