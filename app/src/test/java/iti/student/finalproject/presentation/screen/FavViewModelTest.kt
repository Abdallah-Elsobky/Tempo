package iti.student.finalproject.presentation.screen

import iti.student.finalproject.data.local.entity.FavLocationEntity
import iti.student.finalproject.MainDispatcherRule
import iti.student.finalproject.domain.model.FavLocationModel
import iti.student.finalproject.domain.repository.WeatherRepository
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeWeatherRepository : WeatherRepository {
        val favoritesFlow = MutableStateFlow<List<FavLocationEntity>>(emptyList())
        var insertCalls = 0
        var deleteCalls = 0

        override fun getFavorites(): Flow<List<FavLocationEntity>> = favoritesFlow

        override suspend fun insertFavorite(location: FavLocationEntity) {
            insertCalls++
            favoritesFlow.value = favoritesFlow.value + location
        }

        override suspend fun deleteFavorite(location: FavLocationEntity) {
            deleteCalls++
            favoritesFlow.value = favoritesFlow.value.filterNot { it == location }
        }

        override suspend fun getWeather(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): Flow<ResultState<iti.student.finalproject.data.remote.dto.WeatherResponseDto>> {
            throw NotImplementedError()
        }

        override suspend fun getHourlyForecast(
            lat: Double,
            lon: Double,
            language: String,
            units: String
        ): Flow<ResultState<iti.student.finalproject.data.remote.dto.HourlyForecastResponseDto>> {
            throw NotImplementedError()
        }

        override suspend fun getPossibleCities(cityName: String): Flow<ResultState<List<iti.student.finalproject.data.remote.dto.CityDto>>> {
            throw NotImplementedError()
        }

        override suspend fun getCityNamesLocalized(
            lat: Double,
            lon: Double
        ): Flow<ResultState<List<String>>> {
            throw NotImplementedError()
        }
    }

    @Test
    fun `init collects favorites from repository`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val entity = FavLocationEntity(
            name = "Cairo",
            country = "EG",
            temp = 20f,
            iconUrl = "01d",
            lat = 0.0,
            lon = 0.0
        )
        fakeRepo.favoritesFlow.value = listOf(entity)

        val viewModel = FavViewModel(fakeRepo)
        advanceUntilIdle()

        val favorites = viewModel.favorites.value
        assertEquals(1, favorites.size)
        assertEquals("Cairo", favorites[0].name)
    }

    @Test
    fun `insertFavorite delegates to repository`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val viewModel = FavViewModel(fakeRepo)

        val model = FavLocationModel(
            name = "Cairo",
            country = "EG",
            temp = 20f,
            iconUrl = "01d",
            lat = 0.0,
            lon = 0.0
        )
        viewModel.insertFavorite(model)
        advanceUntilIdle()

        assertEquals(1, fakeRepo.insertCalls)
    }

    @Test
    fun `deleteFavorite delegates to repository`() = runTest {
        val fakeRepo = FakeWeatherRepository()
        val viewModel = FavViewModel(fakeRepo)

        val entity = FavLocationEntity(
            name = "Cairo",
            country = "EG",
            temp = 20f,
            iconUrl = "01d",
            lat = 0.0,
            lon = 0.0
        )
        viewModel.deleteFavorite(entity)
        advanceUntilIdle()

        assertEquals(1, fakeRepo.deleteCalls)
    }
}

