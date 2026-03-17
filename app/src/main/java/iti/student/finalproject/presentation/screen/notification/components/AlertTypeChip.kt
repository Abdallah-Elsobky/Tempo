package iti.student.finalproject.presentation.screen.notification.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp
import iti.student.finalproject.presentation.screen.notification.AlertType
import iti.student.finalproject.ui.theme.WeatherAccentBlue

@Composable
fun AlertTypeChip(
    type: AlertType,
    selected: AlertType,
    onSelect: (AlertType) -> Unit
) {

    val isSelected = type == selected

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onSelect(type) },
        color = if (isSelected) WeatherAccentBlue else LightGray.copy(.2f)
    ) {

        Text(
            type.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}