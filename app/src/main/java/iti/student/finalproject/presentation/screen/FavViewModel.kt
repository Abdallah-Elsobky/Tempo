package iti.student.finalproject.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.domain.mapper.FavLocationMapper.entityToModel
import iti.student.finalproject.domain.mapper.FavLocationMapper.modelToEntity
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<FavLocationModel>>(emptyList())
    val favorites: StateFlow<List<FavLocationModel>> = _favorites

    init {
        viewModelScope.launch {
            repository.getFavorites()
                .collect {
                    _favorites.value = entityToModel(it)
                }
        }
    }

    fun insertFavorite(location: FavLocationModel) {
        viewModelScope.launch {
            repository.insertFavorite(modelToEntity(location))
        }
    }

    fun deleteFavorite(location: FavLocationEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(location)
        }
    }
}

class FavViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}