package iti.student.finalproject.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _homeWeatherState =
        MutableStateFlow<ResultState<WeatherModel>>(ResultState.Loading)
    val homeWeatherState = _homeWeatherState.asStateFlow()

    private val _homeForecastState =
        MutableStateFlow<ResultState<List<ForecastModel>>>(ResultState.Loading)
    val homeForecastState = _homeForecastState.asStateFlow()

    private val _detailForecastState =
        MutableStateFlow<ResultState<List<ForecastModel>>>(ResultState.Loading)
    val detailForecastState = _detailForecastState.asStateFlow()

    private val _pickerWeatherState =
        MutableStateFlow<ResultState<WeatherModel>>(ResultState.Loading)
    val pickerWeatherState = _pickerWeatherState.asStateFlow()

    private val _possibleCitiesState =
        MutableStateFlow<ResultState<List<FavLocationModel>>>(ResultState.Loading)
    val possibleCitiesState = _possibleCitiesState.asStateFlow()

    private val _cityNamesLocalized =
        MutableStateFlow<ResultState<List<String>>>(ResultState.Loading)
    val cityNamesLocalized = _cityNamesLocalized.asStateFlow()

    private var lastHomeWeatherLat: Double? = null
    private var lastHomeWeatherLon: Double? = null
    private var lastHomeForecastLat: Double? = null
    private var lastHomeForecastLon: Double? = null

    fun loadWeather(lat: Double, lon: Double) {
        if (lastHomeWeatherLat == lat && lastHomeWeatherLon == lon) return
        lastHomeWeatherLat = lat
        lastHomeWeatherLon = lon
        loadHomeWeather(lat, lon, "en", "metric")
    }

    fun loadHomeWeather(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getWeather(lat, lon, language, units).collect {
                _homeWeatherState.value =
                    ResultStateMapper(::weatherToDomain)
                        .map(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadForecast(lat: Double, lon: Double) {
        if (lastHomeForecastLat == lat && lastHomeForecastLon == lon) return
        lastHomeForecastLat = lat
        lastHomeForecastLon = lon
        loadHomeForecast(lat, lon, "en", "metric")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadHomeForecast(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getHourlyForecast(lat, lon, language, units).collect {
                _homeForecastState.value = ResultStateMapper(::forecastToDomain)
                    .map(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDetailForecast(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getHourlyForecast(lat, lon, language, units).collect {
                _detailForecastState.value = ResultStateMapper(::forecastToDomain)
                    .map(it)
            }
        }
    }

    fun loadPickerWeather(lat: Double, lon: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getWeather(lat, lon, language, units).collect {
                _pickerWeatherState.value = ResultStateMapper(::weatherToDomain)
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
