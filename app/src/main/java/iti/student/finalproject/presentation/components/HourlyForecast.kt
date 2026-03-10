package iti.student.finalproject.presentation.components

import iti.student.finalproject.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HourlyForecast() {
    val hours = listOf(
        "Now", "1 PM", "2 PM", "3 PM",
        "Now", "1 PM", "2 PM", "3 PM"
    )
    val temps = listOf(
        "72°", "73°", "74°", "75°",
        "72°", "73°", "74°", "75°"
    )
    val icons = listOf(
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
        R.drawable.ic_fav,
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(hours.size) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        if (index == 0) Color(0xFF5D8DEE) else Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(hours[index], color = if (index == 0) Color.White else Color(0xFF2E3A59))
                Icon(
                    painter = painterResource(icons[index]),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 8.dp).size(32.dp)
                )
                Text(temps[index], color = if (index == 0) Color.White else Color(0xFF2E3A59))
            }
        }
    }
}