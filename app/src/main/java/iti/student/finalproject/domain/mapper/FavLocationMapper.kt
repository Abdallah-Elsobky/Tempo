package iti.student.finalproject.domain.mapper

import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.domain.model.WeatherModel

object FavLocationMapper {
    fun entityToModel(entity: FavLocationEntity): FavLocationModel {
        return FavLocationModel(
            entity.name,
            entity.country,
            entity.temp,
            entity.iconUrl,
            entity.lat,
            entity.lon
        )
    }

    fun cityDtoToModel(city: CityDto): FavLocationModel {
        return FavLocationModel(
            city.name ?: "Unknown",
            city.country ?: "Unknown",
            0.0f,
            "",
            city.lat,
            city.lon
        )
    }

    fun cityToModel(cities: List<CityDto>): List<FavLocationModel> {
        return cities.map { cityDtoToModel(it) }
    }

    fun entityToModel(entities: List<FavLocationEntity>): List<FavLocationModel> {
        return entities.map { entityToModel(it) }
    }

    fun modelToEntity(model: FavLocationModel): FavLocationEntity {
        return FavLocationEntity(
            model.name,
            model.country,
            model.temp,
            model.iconUrl,
            model.lat,
            model.lon
        )
    }

    fun weatherToLocation(weather: WeatherModel): FavLocationModel {
        return FavLocationModel(
            weather.city ?: "Unknown",
            weather.country ?: "Unknown",
            weather.temp,
            weather.iconUrl,
            weather.lat.toDouble(),
            weather.lon.toDouble()
        )
    }
}