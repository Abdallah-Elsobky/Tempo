package iti.student.finalproject.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.data.local.preferences.SettingsDataStore
import iti.student.finalproject.domain.model.AppLanguage
import iti.student.finalproject.domain.model.AppSettings
import iti.student.finalproject.domain.model.AppTheme
import iti.student.finalproject.domain.model.LocationMode
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.domain.model.WindSpeedUnit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    val settings: StateFlow<AppSettings> = settingsDataStore.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppSettings()
    )

    fun setTemperatureUnit(unit: TemperatureUnit) = viewModelScope.launch {
        settingsDataStore.updateTemperatureUnit(unit)
    }

    fun setWindSpeedUnit(unit: WindSpeedUnit) = viewModelScope.launch {
        settingsDataStore.updateWindSpeedUnit(unit)
    }

    fun setLocationMode(mode: LocationMode) = viewModelScope.launch {
        settingsDataStore.updateLocationMode(mode)
    }

    fun setLanguage(language: AppLanguage) = viewModelScope.launch {
        settingsDataStore.updateLanguage(language)
    }

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        settingsDataStore.updateTheme(theme)
    }

    fun setMapLocation(lat: Double, lon: Double) = viewModelScope.launch {
        settingsDataStore.updateMapLocation(lat, lon)
    }
}

class SettingsViewModelFactory(
    private val settingsDataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
