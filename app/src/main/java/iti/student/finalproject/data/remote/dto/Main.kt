package iti.student.finalproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Main(
	val temp: Float,
	@SerializedName("temp_min")
	val tempMin: Float,
	@SerializedName("temp_max")
	val tempMax: Float,
	val grndLevel: Int,
	val tempKf: Float,
	val humidity: Int,
	val pressure: Int,
	val seaLevel: Int,
	val feelsLike: Float,
)
