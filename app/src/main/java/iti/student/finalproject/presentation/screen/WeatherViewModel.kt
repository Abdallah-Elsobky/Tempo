package iti.student.finalproject.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.domain.mapper.FavLocationMapper.cityDtoToModel
import iti.student.finalproject.domain.mapper.FavLocationMapper.cityToModel
import iti.student.finalproject.domain.mapper.ResultStateMapper
import iti.student.finalproject.domain.mapper.WeatherMapper.weatherToDomain
import iti.student.finalproject.domain.mapper.WeatherMapper.forecastToDomain
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {


    private val _weatherState =
        MutableStateFlow<ResultState<WeatherModel>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResultState<List<ForecastModel>>>(ResultState.Loading)
    val forecastState = _forecastState.asStateFlow()


    private val _possibleCitiesState =
        MutableStateFlow<ResultState<List<FavLocationModel>>>(ResultState.Loading)
    val possibleCitiesState = _possibleCitiesState.asStateFlow()

    private val _cityNamesLocalized =
        MutableStateFlow<ResultState<List<String>>>(ResultState.Loading)
    val cityNamesLocalized = _cityNamesLocalized.asStateFlow()

    private var lastWeatherLat: Double? = null
    private var lastWeatherLon: Double? = null
    private var lastForecastLat: Double? = null
    private var lastForecastLon: Double? = null

    fun loadWeather(lat: Double, lon: Double) {
        if (lastWeatherLat == lat && lastWeatherLon == lon) return
        lastWeatherLat = lat
        lastWeatherLon = lon
        loadWeather(lat, lon, "en", "metric")
    }

    fun loadWeather(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getWeather(lat, lon, language, units).collect {
                _weatherState.value =
                    ResultStateMapper(::weatherToDomain)
                        .map(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadForecast(lat: Double, lon: Double) {
        if (lastForecastLat == lat && lastForecastLon == lon) return
        lastForecastLat = lat
        lastForecastLon = lon
        loadForecast(lat, lon, "en", "metric")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadForecast(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getHourlyForecast(lat, lon, language, units).collect {
                _forecastState.value = ResultStateMapper(::forecastToDomain)
                    .map(it)
            }
        }
    }

    fun loadPossibleCities(cityName: String) {
        viewModelScope.launch {
            repository.getPossibleCities(cityName).collect {
                _possibleCitiesState.value = ResultStateMapper(::cityToModel)
                    .map(it)
            }
        }
    }

    fun loadCityNamesLocalized(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getCityNamesLocalized(lat, lon).collect {
                _cityNamesLocalized.value = it
            }
        }
    }
}

class WeatherViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}