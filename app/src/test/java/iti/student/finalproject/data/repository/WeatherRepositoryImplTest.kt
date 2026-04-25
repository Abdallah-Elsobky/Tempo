package iti.student.finalproject.data.repository

import iti.student.finalproject.data.local.datasource.weather.WeatherLocalDataSource
import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSource
import iti.student.finalproject.data.remote.dto.CityDto
import iti.student.finalproject.data.remote.dto.Clouds
import iti.student.finalproject.data.remote.dto.Coord
import iti.student.finalproject.data.remote.dto.ListItem
import iti.student.finalproject.data.remote.dto.Main
import iti.student.finalproject.data.remote.dto.Rain
import iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto
import iti.student.finalproject.data.remote.dto.Sys
import iti.student.finalproject.data.remote.dto.WeatherResponseDto
import iti.student.finalproject.data.remote.dto.WeatherItem
import iti.student.finalproject.data.remote.dto.Wind
import iti.student.finalproject.data.remote.dto.City
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.collections.emptyList

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private class FakeRemoteDataSource : WeatherRemoteDataSource {
        var shouldThrow = false

        override suspend fun getWeather(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): WeatherResponseDto {
            if (shouldThrow) throw RuntimeException("Error")
            return WeatherResponseDto(
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
        }

        override suspend fun getHourlyForecast(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): HourlyForecastResponseDto {
            if (shouldThrow) throw RuntimeException("Error")
            val coord = Coord(lon = lon.toFloat(), lat = lat.toFloat())
            return HourlyForecastResponseDto(
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
        }

        override suspend fun getPossibleCities(cityName: String): List<CityDto> {
            if (shouldThrow) throw RuntimeException("Error")
            return listOf(
                CityDto(
                    name = "Cairo",
                    localNames = null,
                    lat = 0.0,
                    lon = 0.0,
                    country = "EG",
                    state = null
                )
            )
        }

        override suspend fun getCityNamesLocalized(lat: Double, lon: Double): List<CityDto> {
            if (shouldThrow) throw RuntimeException("Error")
            return listOf(
                CityDto(
                    name = "Cairo",
                    localNames = null,
                    lat = 0.0,
                    lon = 0.0,
                    country = "EG",
                    state = null
                )
            )
        }
    }

    private class FakeLocalDataSource : WeatherLocalDataSource {
        val favoritesFlow = MutableStateFlow<List<FavLocationEntity>>(emptyList())
        var insertCalls = 0
        var deleteCalls = 0

        override fun getAllFav(): Flow<List<FavLocationEntity>> = favoritesFlow

        override suspend fun insertNewFav(location: FavLocationEntity) {
            insertCalls++
            favoritesFlow.value = favoritesFlow.value + location
        }

        override suspend fun deleteFavLocation(location: FavLocationEntity) {
            deleteCalls++
            favoritesFlow.value = favoritesFlow.value.filterNot { it.name == location.name }
        }
    }

    @Test
    fun `getWeather emits loading then success`() = runTest {
        val repo = WeatherRepositoryImpl(FakeRemoteDataSource(), FakeLocalDataSource())

        val emissions = repo.getWeather(0.0, 0.0, "en", "metric").take(2).toList()

        assertTrue(emissions.first() is ResultState.Loading)
        assertTrue(emissions.last() is ResultState.Success)
    }

    @Test
    fun `getHourlyForecast emits error when remote throws`() = runTest {
        val remote = FakeRemoteDataSource().apply { shouldThrow = true }
        val repo = WeatherRepositoryImpl(remote, FakeLocalDataSource())

        val emissions = repo.getHourlyForecast(0.0, 0.0, "en", "metric").take(2).toList()

        assertTrue(emissions.first() is ResultState.Loading)
        assertTrue(emissions.last() is ResultState.Error)
    }

    @Test
    fun `getFavorites delegates to local data source`() = runTest {
        val local = FakeLocalDataSource()
        val repo = WeatherRepositoryImpl(FakeRemoteDataSource(), local)

        val entity = FavLocationEntity(
            name = "Cairo",
            country = "EG",
            temp = 20f,
            iconUrl = "01d",
            lat = 0.0,
            lon = 0.0
        )
        local.favoritesFlow.value = listOf(entity)

        val result = repo.getFavorites().first()

        assertEquals(1, result.size)
        assertEquals("Cairo", result[0].name)
    }
}

