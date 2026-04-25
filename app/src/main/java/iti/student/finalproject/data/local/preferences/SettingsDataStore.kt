package iti.student.finalproject.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import iti.student.finalproject.domain.model.AppLanguage
import iti.student.finalproject.domain.model.AppSettings
import iti.student.finalproject.domain.model.AppTheme
import iti.student.finalproject.domain.model.LocationMode
import iti.student.finalproject.domain.model.TemperatureUnit
import iti.student.finalproject.domain.model.WindSpeedUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "app_settings")

class SettingsDataStore(private val context: Context) {
    private object Keys {
        val temperatureUnit = stringPreferencesKey("temperature_unit")
        val windSpeedUnit = stringPreferencesKey("wind_speed_unit")
        val locationMode = stringPreferencesKey("location_mode")
        val language = stringPreferencesKey("language")
        val theme = stringPreferencesKey("theme")
        val mapLatitude = doublePreferencesKey("map_latitude")
        val mapLongitude = doublePreferencesKey("map_longitude")
    }

    val settingsFlow: Flow<AppSettings> = context.settingsDataStore.data.map(::toAppSettings)

    suspend fun updateTemperatureUnit(unit: TemperatureUnit) {
        context.settingsDataStore.edit { it[Keys.temperatureUnit] = unit.name }
    }

    suspend fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        context.settingsDataStore.edit { it[Keys.windSpeedUnit] = unit.name }
    }

    suspend fun updateLocationMode(mode: LocationMode) {
        context.settingsDataStore.edit { it[Keys.locationMode] = mode.name }
    }

    suspend fun updateLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { it[Keys.language] = language.name }
    }

    suspend fun updateTheme(theme: AppTheme) {
        context.settingsDataStore.edit { it[Keys.theme] = theme.name }
    }

    suspend fun updateMapLocation(lat: Double, lon: Double) {
        context.settingsDataStore.edit {
            it[Keys.mapLatitude] = lat
            it[Keys.mapLongitude] = lon
        }
    }

    private fun toAppSettings(preferences: Preferences): AppSettings {
        return AppSettings(
            temperatureUnit = enumValueOfOrDefault(
                preferences[Keys.temperatureUnit],
                TemperatureUnit.CELSIUS
            ),
            windSpeedUnit = enumValueOfOrDefault(preferences[Keys.windSpeedUnit], WindSpeedUnit.KMH),
            locationMode = enumValueOfOrDefault(preferences[Keys.locationMode], LocationMode.GPS),
            language = enumValueOfOrDefault(preferences[Keys.language], AppLanguage.ENGLISH),
            theme = enumValueOfOrDefault(preferences[Keys.theme], AppTheme.LIGHT),
            mapLatitude = preferences[Keys.mapLatitude] ?: 30.0444,
            mapLongitude = preferences[Keys.mapLongitude] ?: 31.2357
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOfOrDefault(value: String?, default: T): T {
        return value?.let {
            runCatching { enumValueOf<T>(it) }.getOrNull()
        } ?: default
    }
}
