package iti.student.finalproject.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import iti.student.finalproject.R
import iti.student.finalproject.presentation.navigation.BottomNavItem
import iti.student.finalproject.presentation.navigation.Screen
import iti.student.finalproject.presentation.screen.WeatherViewModel

@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {

    val items = listOf(
        BottomNavItem(stringResource(R.string.home), Screen.Home.route, painterResource(R.drawable.ic_home)),
        BottomNavItem(stringResource(R.string.favorites), Screen.Favorites.route, painterResource(R.drawable.ic_fav)),
        BottomNavItem(
            stringResource(R.string.notifications),
            Screen.Notification.route,
            painterResource(R.drawable.ic_notification)
        ),
        BottomNavItem(stringResource(R.string.settings), Screen.Settings.route, painterResource(R.drawable.ic_setting))
    )

    val mainRoutes = listOf(
        Screen.Home.route,
        Screen.Favorites.route,
        Screen.Notification.route,
        Screen.Settings.route
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    AnimatedVisibility(currentRoute in mainRoutes, modifier) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            shape = RoundedCornerShape(34.dp),
            shadowElevation = 20.dp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.97f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
//                    label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.blue),
                            unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            selectedTextColor = colorResource(R.color.blue),
                            unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}