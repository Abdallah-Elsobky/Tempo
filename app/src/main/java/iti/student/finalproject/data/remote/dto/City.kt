package iti.student.finalproject.data.remote.dto

data class City(
	val country: String,
	val coord: Coord,
	val sunrise: Int,
	val timezone: Int,
	val sunset: Int,
	val name: String,
	val id: Int,
	val population: Int
)
