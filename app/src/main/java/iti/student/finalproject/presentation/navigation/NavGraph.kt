package iti.student.finalproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import iti.student.finalproject.presentation.screen.favorites.FavoritesScreen
import iti.student.finalproject.presentation.screen.forecast.ForecastScreen
import iti.student.finalproject.presentation.screen.home.HomeScreen
import iti.student.finalproject.presentation.screen.notification.NotificationScreen
import iti.student.finalproject.presentation.screen.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToForecast = {
                    navController.navigate(Screen.Forecast.route)
                }
            )
        }

        composable(Screen.Favorites.route) { FavoritesScreen() }

        composable(Screen.Notification.route) { NotificationScreen() }

        composable(Screen.Settings.route) { SettingsScreen() }

        composable(Screen.Forecast.route) { ForecastScreen() }
    }
}