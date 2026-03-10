package iti.student.finalproject.data.remote.dto

data class WeatherResponseDto(
	val visibility: Int,
	val timezone: Int,
	val main: Main,
	val clouds: Clouds,
	val sys: Sys,
	val dt: Int,
	val coord: Coord,
	val weather: List<WeatherItem>,
	val name: String,
	val cod: Int,
	val id: Int,
	val base: String,
	val wind: Wind
)
