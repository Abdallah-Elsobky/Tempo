package iti.student.finalproject.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.domain.mapper.ResultStateMapper
import iti.student.finalproject.domain.mapper.WeatherMapper.weatherToDomain
import iti.student.finalproject.domain.mapper.WeatherMapper.forecastToDomain
import iti.student.finalproject.domain.model.ForecastModel
import iti.student.finalproject.domain.model.WeatherModel
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    init {
        loadWeather(30.0444, 31.2357)
        loadForecast(30.0444, 31.2357)
    }

    private val _weatherState =
        MutableStateFlow<ResultState<WeatherModel>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResultState<List<ForecastModel>>>(ResultState.Loading)
    val forecastState = _forecastState.asStateFlow()


    private val _possibleCitiesState =
        MutableStateFlow<ResultState<List<String>>>(ResultState.Loading)
    val possibleCitiesState = _possibleCitiesState.asStateFlow()

    private val _cityNamesLocalized =
        MutableStateFlow<ResultState<List<String>>>(ResultState.Loading)
    val cityNamesLocalized = _cityNamesLocalized.asStateFlow()


    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getWeather(lat, lon).collect {
                _weatherState.value =
                    ResultStateMapper(::weatherToDomain)
                        .map(it)
            }
        }
    }

    fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getHourlyForecast(lat, lon).collect {
                _forecastState.value = ResultStateMapper(::forecastToDomain)
                    .map(it)
            }
        }
    }

    fun loadPossibleCities(cityName: String) {
        viewModelScope.launch {
            repository.getPossibleCities(cityName).collect {
                _possibleCitiesState.value = it
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
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}