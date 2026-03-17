package iti.student.finalproject.domain.mapper

import iti.student.finalproject.data.local.entity.AlertEntity
import iti.student.finalproject.domain.model.AlertModel

object AlertMapper {
    fun entityToModel(entity: AlertEntity): AlertModel {
        return AlertModel(
            entity.id,
            entity.startDate,
            entity.endDate,
            entity.alertType,
        )
    }

    fun entityToModel(entity: List<AlertEntity>): List<AlertModel> {
        return entity.map { entityToModel(it) }
    }

    fun modelToEntity(model: AlertModel): AlertEntity {
        return AlertEntity(
            id = model.id,
            startDate = model.startDate,
            endDate = model.endDate,
            alertType = model.alertType,
        )
    }
}