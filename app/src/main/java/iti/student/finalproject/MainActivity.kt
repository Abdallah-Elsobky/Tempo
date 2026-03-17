package iti.student.finalproject

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import iti.student.finalproject.data.local.database.AppDatabase
import iti.student.finalproject.data.local.datasource.alert.AlertLocalDataSource
import iti.student.finalproject.data.local.datasource.alert.AlertLocalDataSourceImpl
import iti.student.finalproject.data.local.datasource.weather.WeatherLocalDataSourceImpl
import iti.student.finalproject.data.remote.api.RetrofitInstance
import iti.student.finalproject.data.remote.datasource.WeatherRemoteDataSourceImpl
import iti.student.finalproject.data.repository.AlertRepositoryImpl
import iti.student.finalproject.data.repository.WeatherRepositoryImpl
import iti.student.finalproject.presentation.components.BottomBar
import iti.student.finalproject.presentation.navigation.NavGraph
import iti.student.finalproject.presentation.screen.AlertViewModel
import iti.student.finalproject.presentation.screen.AlertViewModelFactory
import iti.student.finalproject.presentation.screen.FavViewModel
import iti.student.finalproject.presentation.screen.FavViewModelFactory
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModelFactory
import iti.student.finalproject.ui.theme.FinalProjectTheme
import iti.student.finalproject.worker.AlertScheduler
import org.osmdroid.config.Configuration


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val db = (application as iti.student.finalproject.WeatherApp).database
        val favDao = db.favDao()
        val weatherLocalDataSource = WeatherLocalDataSourceImpl(favDao)
        val alertDao = db.alertDao()
        val alertLocalDataSource = AlertLocalDataSourceImpl(alertDao)
        val remoteDataSource = WeatherRemoteDataSourceImpl(RetrofitInstance.api)
        val weatherRepository = WeatherRepositoryImpl(remoteDataSource, weatherLocalDataSource)
        val alertRepository = AlertRepositoryImpl(alertLocalDataSource)
        val alertScheduler = AlertScheduler(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val weatherFactory = WeatherViewModelFactory(weatherRepository)
        val favFactory = FavViewModelFactory(weatherRepository)
        val alertFactory = AlertViewModelFactory(alertRepository, alertScheduler)

        val weatherViewModel: WeatherViewModel =
            ViewModelProvider(this, weatherFactory)[WeatherViewModel::class.java]

        val favViewModel: FavViewModel =
            ViewModelProvider(this, favFactory)[FavViewModel::class.java]

        val alertViewModel: AlertViewModel =
            ViewModelProvider(this, alertFactory)[AlertViewModel::class.java]

        Configuration.getInstance().apply {
            load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            )
            userAgentValue = packageName
        }

        setContent {
            FinalProjectTheme {
                val navController = rememberNavController()
                Scaffold { padding ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEAF2FF))
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        MainScreen(navController, weatherViewModel, favViewModel, alertViewModel)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    favViewModel: FavViewModel,
    alertViewModel: AlertViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavGraph(navController, weatherViewModel, favViewModel, alertViewModel)
        BottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}