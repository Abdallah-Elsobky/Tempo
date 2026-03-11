package iti.student.finalproject.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    init {
//        loadWeather(30.0, 31.0)
//        loadForecast(30.0, 31.0)
    }

    private val _weatherState =
        MutableStateFlow<ResultState<WeatherResponseDto>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResultState<HourlyForecastResponseDto>>(ResultState.Loading)
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
                _weatherState.value = it
            }
        }
    }

    fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getHourlyForecast(lat, lon).collect {
                _forecastState.value = it
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