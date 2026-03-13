package iti.student.finalproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import iti.student.finalproject.presentation.screen.WeatherViewModel
import iti.student.finalproject.presentation.screen.favorites.FavoritesScreen
import iti.student.finalproject.presentation.screen.favorites.map.NewFavScreen
import iti.student.finalproject.presentation.screen.home.forecast.ForecastScreen
import iti.student.finalproject.presentation.screen.home.HomeScreen
import iti.student.finalproject.presentation.screen.notification.NotificationScreen
import iti.student.finalproject.presentation.screen.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: WeatherViewModel) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel,
                onNavigateToForecast = {
                    navController.navigate(Screen.Forecast.route)
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onAddNewCity = {
                    navController.navigate(Screen.NewFav.route)
                }
            )
        }

        composable(Screen.Notification.route) { NotificationScreen() }

        composable(Screen.Settings.route) { SettingsScreen() }

        composable(Screen.Forecast.route) {
            ForecastScreen(
                viewModel,
                onBackClick = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        composable(Screen.NewFav.route) {
            NewFavScreen(
                onBackClick = {
                    navController.popBackStack(Screen.Favorites.route, false)
                }
            )
        }
    }
}