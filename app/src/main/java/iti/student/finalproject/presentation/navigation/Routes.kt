package iti.student.finalproject.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Notification : Screen("notification")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object Forecast : Screen("forecast?lat={lat}&lon={lon}")
    object NewFav : Screen("new_fav")
}