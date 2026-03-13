package iti.student.finalproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ListItem(
	val dt: Int,
	val pop: Double,
	val visibility: Int,
	@SerializedName("dt_txt")
	val dtTxt: String,
	val weather: List<WeatherItem>,
	val main: Main,
	val clouds: Clouds,
	val sys: Sys,
	val wind: Wind,
	val rain: Rain
)
