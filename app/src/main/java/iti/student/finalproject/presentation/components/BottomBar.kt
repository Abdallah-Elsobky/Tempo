package iti.student.finalproject.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import iti.student.finalproject.R
import iti.student.finalproject.presentation.navigation.BottomNavItem
import iti.student.finalproject.presentation.navigation.Screen

@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {

    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, painterResource(R.drawable.ic_home)),
        BottomNavItem("Favorites", Screen.Favorites.route, painterResource(R.drawable.ic_fav)),
        BottomNavItem(
            "Notification",
            Screen.Notification.route,
            painterResource(R.drawable.ic_notification)
        ),
        BottomNavItem("Settings", Screen.Settings.route, painterResource(R.drawable.ic_setting))
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(34.dp),
        shadowElevation = 20.dp,
        color = Color.White.copy(alpha = 0.97f)
    ) {

        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

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
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = colorResource(R.color.blue),
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}