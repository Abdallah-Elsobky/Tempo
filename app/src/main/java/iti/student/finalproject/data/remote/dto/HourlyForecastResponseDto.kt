package iti.student.finalproject.data.remote.dto

data class HourlyForecastResponseDto(
	val city: City,
	val cnt: Int,
	val cod: String,
	val message: Int,
	val list: List<ListItem>
)
