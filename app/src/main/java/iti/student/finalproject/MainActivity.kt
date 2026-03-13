package iti.student.finalproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import iti.student.finalproject.data.local.database.AppDatabase
import iti.student.finalproject.data.local.datasource.WeatherLocalDataSourceImpl
import iti.student.finalproject.data.remote.api.RetrofitInstance
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSourceImpl
import iti.student.finalproject.data.repository.WeatherRepositoryImpl
import iti.student.finalproject.presentation.components.BottomBar
import iti.student.finalproject.presentation.navigation.NavGraph
import iti.student.finalproject.presentation.screen.FavViewModel
import iti.student.finalproject.presentation.screen.FavViewModelFactory
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModelFactory
import iti.student.finalproject.ui.theme.FinalProjectTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "app_database"
        ).build()

        val favDao = db.favDao()
        val localDataSource = WeatherLocalDataSourceImpl(favDao)
        val remoteDataSource = WeatherRemoteDataSourceImpl(RetrofitInstance.api)
        val repository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
        val weatherFactory = WeatherViewModelFactory(
            repository
        )
        val favFactory = FavViewModelFactory(repository)

        val weatherViewModel: WeatherViewModel =
            ViewModelProvider(this, weatherFactory)[WeatherViewModel::class.java]

        val favViewModel: FavViewModel =
            ViewModelProvider(this, favFactory)[FavViewModel::class.java]



        setContent {
            FinalProjectTheme {
                val navController = rememberNavController()
                val weatherState by weatherViewModel.weatherState.collectAsState()
                val forecastState by weatherViewModel.forecastState.collectAsState()
                val cityNamesLocalizedState by weatherViewModel.cityNamesLocalized.collectAsState()
                val possibleCitiesState by weatherViewModel.possibleCitiesState.collectAsState()

                weatherViewModel.loadPossibleCities("cairo")
                Scaffold { padding ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEAF2FF))
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        MainScreen(navController, weatherViewModel, favViewModel)
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
fun MainScreen(navController: NavHostController, weatherViewModel: WeatherViewModel, favViewModel: FavViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavGraph(navController, weatherViewModel,favViewModel)
        BottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}