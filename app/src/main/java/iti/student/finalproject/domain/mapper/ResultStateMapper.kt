package iti.student.finalproject.domain.mapper

import iti.student.finalproject.utils.ResultState

class ResultStateMapper<DTO, Model>(
    private val dtoToDomain: (DTO) -> Model
) {
    fun map(resultState: ResultState<DTO>): ResultState<Model> {
        return when (resultState) {
            is ResultState.Error -> ResultState.Error(resultState.message)
            is ResultState.Loading -> ResultState.Loading
            is ResultState.Success<*> -> ResultState.Success(dtoToDomain(resultState.data as DTO))
        }
    }
}