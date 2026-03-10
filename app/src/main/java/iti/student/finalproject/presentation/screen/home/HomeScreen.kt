package iti.student.finalproject.presentation.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import iti.student.finalproject.R
import iti.student.finalproject.presentation.components.HourlyForecast
import iti.student.finalproject.presentation.components.MainWeather
import iti.student.finalproject.presentation.components.WeatherDetails

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cairo, EG",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2E3A59)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main weather icon + temp
        MainWeather()

        Spacer(modifier = Modifier.height(50.dp))

        // Weather details: wind, humidity, UV
        WeatherDetails()

        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E3A59)
            )
            Text(
                text = "7 DAYS",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.blue),
                modifier = Modifier.clickable {

                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        // Hourly forecast
        HourlyForecast()

        Spacer(modifier = Modifier.height(16.dp))
    }
}