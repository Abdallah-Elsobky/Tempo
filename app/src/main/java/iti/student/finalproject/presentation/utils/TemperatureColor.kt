package iti.student.finalproject.presentation.utils

import androidx.compose.ui.graphics.Color
import iti.student.finalproject.ui.theme.*


fun getTemperatureColor(temp: Float): Color {
    return when {
        temp <= 0 -> WeatherAccentBlueLight
        temp <= 10 -> WeatherAccentBlue
        temp <= 20 -> WeatherAccentGreen
        temp <= 30 -> WeatherYellow
        temp <= 40 -> WeatherOrange
        else -> WeatherAccentRed
    }
}