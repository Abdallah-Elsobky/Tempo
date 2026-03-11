package iti.student.finalproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import iti.student.finalproject.data.remote.api.RetrofitInstance
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSourceImpl
import iti.student.finalproject.data.repository.WeatherRepositoryImpl
import iti.student.finalproject.presentation.components.BottomBar
import iti.student.finalproject.presentation.navigation.NavGraph
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModelFactory
import iti.student.finalproject.ui.theme.FinalProjectTheme
import iti.student.finalproject.utils.ResultState
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = WeatherViewModelFactory(
            WeatherRepositoryImpl(
                WeatherRemoteDataSourceImpl(RetrofitInstance.api)
            )
        )

        val viewModel: WeatherViewModel =
            ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        val repo = WeatherRepositoryImpl(
            WeatherRemoteDataSourceImpl(RetrofitInstance.api)
        )

//        lifecycleScope.launch {
//            repo.getHourlyForecast(30.0, 31.0).collect {
//                Log.d("loco", it.toString())
//            }
//        }
        setContent {
            FinalProjectTheme {
                val navController = rememberNavController()
                val weatherState by viewModel.weatherState.collectAsState()
                val forecastState by viewModel.forecastState.collectAsState()
                val cityNamesLocalizedState by viewModel.cityNamesLocalized.collectAsState()
                val possibleCitiesState by viewModel.possibleCitiesState.collectAsState()

                viewModel.loadPossibleCities("cairo")
                Scaffold { padding ->
                    Box(
                        modifier = Modifier.background(Color(0xFFEAF2FF))
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        MainScreen(navController)
                        // test view model
//                        when (val state = possibleCitiesState) {
//                            is ResultState.Loading -> {
//                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                            }
//
//                            is ResultState.Success -> {
//                                Text(text = state.data.get(0))
//                            }
//
//                            is ResultState.Error -> {
//                                Text(text = state.message)
//                            }
//                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavGraph(navController)
        BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}