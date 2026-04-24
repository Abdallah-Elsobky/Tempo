package iti.student.finalproject.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import iti.student.finalproject.presentation.screen.AlertViewModel
import iti.student.finalproject.presentation.screen.FavViewModel
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.favorites.FavoritesScreen
import iti.student.finalproject.presentation.screen.favorites.map.NewFavScreen
import iti.student.finalproject.presentation.screen.home.forecast.ForecastScreen
import iti.student.finalproject.presentation.screen.home.HomeScreen
import iti.student.finalproject.presentation.screen.notification.NotificationScreen
import iti.student.finalproject.presentation.screen.settings.SettingsScreen
import iti.student.finalproject.presentation.screen.settings.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    favViewModel: FavViewModel,
    alertViewModel: AlertViewModel,
    settingsViewModel: SettingsViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            val settings = settingsViewModel.settings.collectAsState().value
            HomeScreen(
                weatherViewModel,
                settings,
                onNavigateToForecast = { lon, lat ->
                    navController.navigate("forecast?lat=${lat}&lon=${lon}")
                }
            )
        }

        composable(
            Screen.Favorites.route,
        ) {
            FavoritesScreen(
                favViewModel,
                onAddNewCity = {
                    navController.navigate(Screen.NewFav.route)
                },
                onFavClick = { favLocation ->
                    navController.navigate(
                        "forecast?lat=${favLocation.lat.toFloat()}&lon=${favLocation.lon.toFloat()}"
                    )
                }
            )
        }

        composable(Screen.Notification.route) { NotificationScreen(alertViewModel) }

        composable(Screen.Settings.route) { SettingsScreen(settingsViewModel) }

        composable(
            Screen.Forecast.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )) { backStackEntry ->
            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0.0f
            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0.0f
            ForecastScreen(
                lon, lat,
                weatherViewModel,
                settings = settingsViewModel.settings.collectAsState().value,
                onBackClick = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        composable(Screen.NewFav.route) {
            NewFavScreen(
                weatherViewModel,
                favViewModel,
                settingsViewModel,
                onBackClick = {
                    navController.popBackStack(Screen.Favorites.route, false)
                }
            )
        }
    }
}