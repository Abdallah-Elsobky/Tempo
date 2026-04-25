package iti.student.finalproject.presentation.screen.notification.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.R
import iti.student.finalproject.ui.theme.WeatherSecondaryText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DatePickerField(
    title: String,
    date: Long?,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    title,
                    fontSize = 12.sp,
                    color = WeatherSecondaryText
                )

                Text(
                    date?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .format(Date(it))
                    } ?: stringResource(R.string.select_date),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                painterResource(R.drawable.ic_clock),
                null
            )
        }
    }
}