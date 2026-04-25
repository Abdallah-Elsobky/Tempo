package iti.student.finalproject.presentation.screen

import iti.student.finalproject.MainDispatcherRule
import iti.student.finalproject.data.remote.dto.City
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.Clouds
import iti.student.finalproject.data.remote.dto.Coord
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.ListItem
import iti.student.finalproject.data.remote.dto.Main
import iti.student.finalproject.data.remote.dto.Rain
import iti.student.finalproject.data.remote.dto.Sys
import iti.student.finalproject.data.remote.dto.WeatherItem
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.data.remote.dto.Wind
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeWeatherRepository : WeatherRepository {
        var weatherCallCount = 0
        var lastWeatherLat: Double? = null
        var lastWeatherLon: Double? = null

        override suspend fun getWeather(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): Flow<ResultState<WeatherResponseDto>> {
            weatherCallCount++
            lastWeatherLat = lat
            lastWeatherLon = lon
            val dto = WeatherResponseDto(
                visibility = 10_000,
                timezone = 0,
                main = Main(
                    temp = 20f,
                    tempMin = 15f,
                    tempMax = 25f,
                    grndLevel = 0,
                    tempKf = 0f,
                    humidity = 50,
                    pressure = 1013,
                    seaLevel = 0,
                    feelsLike = 20f
                ),
                clouds = Clouds(all = 0),
                sys = Sys(country = "EG", sunrise = 0, sunset = 0),
                dt = 0,
                coord = Coord(lon = lon.toFloat(), lat = lat.toFloat()),
                weather = listOf(
                    WeatherItem(
                        icon = "01d",
                        description = "clear sky",
                        main = "Clear",
                        id = 800
                    )
                ),
                name = "Cairo",
                cod = 200,
                id = 1,
                base = "stations",
                wind = Wind(deg = 0, speed = 3f, gust = 0f)
            )
            return flowOf(ResultState.Success(dto))
        }

        override suspend fun getHourlyForecast(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): Flow<ResultState<HourlyForecastResponseDto>> {
            val coord = Coord(lon = lon.toFloat(), lat = lat.toFloat())
            val dto = HourlyForecastResponseDto(
                city = City(
                    country = "EG",
                    coord = coord,
                    sunrise = 0,
                    timezone = 0,
                    sunset = 0,
                    name = "Cairo",
                    id = 1,
                    population = 0
                ),
                cnt = 1,
                cod = "200",
                message = 0,
                list = listOf(
                    ListItem(
                        dt = 0,
                        pop = 0.0,
                        visibility = 10_000,
                        dtTxt = "2026-01-01 00:00:00",
                        weather = listOf(
                            WeatherItem(
                                icon = "01d",
                                description = "clear sky",
                                main = "Clear",
                                id = 800
                            )
                        ),
                        main = Main(
                            temp = 20f,
                            tempMin = 15f,
                            tempMax = 25f,
                            grndLevel = 0,
                            tempKf = 0f,
                            humidity = 50,
                            pressure = 1013,
                            seaLevel = 0,
                            feelsLike = 20f
                        ),
                        clouds = Clouds(all = 0),
                        sys = Sys(country = "EG", sunrise = 0, sunset = 0),
                        wind = Wind(deg = 0, speed = 3f, gust = 0f),
                        rain = Rain(jsonMember3h = 0)
                    )
                )
            )
            return flowOf(ResultState.Success(dto))
        }

        override suspend fun getPossibleCities(cityName: String): Flow<ResultState<List<CityDto>>> {
            val cities = listOf(
                CityDto(
                    name = "Cairo",
                    localNames = null,
                    lat = 0.0,
                    lon = 0.0,
                    country = "EG",
                    state = null
                )
            )
            return flowOf(ResultState.Success(cities))
        }

        override suspend fun getCityNamesLocalized(
            lat: Double,
            lon: Double
        ): Flow<ResultState<List<String>>> {
            return flowOf(ResultState.Success(listOf("Cairo")))
        }

        override fun getFavorites(): Flow<List<iti.student.finalproject.data.local.entity.FavLocationEntity>> {
            return MutableStateFlow(emptyList())
        }

        override suspend fun insertFavorite(location: iti.student.finalproject.data.local.entity.FavLocationEntity) {
        }

        override suspend fun deleteFavorite(location: iti.student.finalproject.data.local.entity.FavLocationEntity) {
        }
    }

    @Test
    fun `loadWeather calls repository only once for same coordinates`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val viewModel = WeatherViewModel(fakeRepo)

        viewModel.loadWeather(10.0, 20.0)
        viewModel.loadWeather(10.0, 20.0)
        advanceUntilIdle()

        assertEquals(1, fakeRepo.weatherCallCount)
        assertEquals(10.0, fakeRepo.lastWeatherLat ?: -1.0, 0.0)
        assertEquals(20.0, fakeRepo.lastWeatherLon ?: -1.0, 0.0)
    }

    @Test
    fun `loadForecast updates forecastState`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val viewModel = WeatherViewModel(fakeRepo)

        viewModel.loadForecast(10.0, 20.0)
        advanceUntilIdle()

        val state = viewModel.forecastState.value
        assert(state is ResultState.Success)
    }

    @Test
    fun `loadPossibleCities updates possibleCitiesState`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val viewModel = WeatherViewModel(fakeRepo)

        viewModel.loadPossibleCities("Cairo")
        advanceUntilIdle()

        val state = viewModel.possibleCitiesState.value
        assert(state is ResultState.Success)
    }
}

