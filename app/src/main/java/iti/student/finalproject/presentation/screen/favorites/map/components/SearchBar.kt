package iti.student.finalproject.presentation.screen.favorites.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import iti.student.finalproject.R
import iti.student.finalproject.ui.theme.WeatherDivider
import iti.student.finalproject.ui.theme.WeatherSurfaceCard


@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.Transparent),
        shape = RoundedCornerShape(34.dp),
        shadowElevation = 50.dp,
    ) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search by name") },
            leadingIcon = { Icon(painterResource(R.drawable.ic_search), null) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = WeatherDivider,
                unfocusedContainerColor = WeatherDivider,
                focusedContainerColor = WeatherSurfaceCard
            ),
            shape = RoundedCornerShape(34.dp)
        )

    }
}