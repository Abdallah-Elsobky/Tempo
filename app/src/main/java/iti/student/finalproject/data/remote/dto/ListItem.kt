package iti.student.finalproject.data.remote.dto

data class ListItem(
	val dt: Int,
	val pop: Double,
	val visibility: Int,
	val dtTxt: String,
	val weather: List<WeatherItem>,
	val main: Main,
	val clouds: Clouds,
	val sys: Sys,
	val wind: Wind,
	val rain: Rain
)
