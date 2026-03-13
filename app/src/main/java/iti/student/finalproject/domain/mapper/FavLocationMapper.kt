package iti.student.finalproject.domain.mapper

import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.domain.model.FavLocationModel

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
}