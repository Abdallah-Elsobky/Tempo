package iti.student.finalproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
	@SerializedName("name") val name: String?,
	@SerializedName("local_names") val localNames: LocalNamesDto?,
	@SerializedName("lat") val lat: Double,
	@SerializedName("lon") val lon: Double,
	@SerializedName("country") val country: String?,
	@SerializedName("state") val state: String?
)

data class LocalNamesDto(
	@SerializedName("en") val en: String?,
	@SerializedName("ar") val ar: String?
)
