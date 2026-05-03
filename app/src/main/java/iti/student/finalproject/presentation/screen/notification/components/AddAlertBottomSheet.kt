package iti.student.finalproject.presentation.screen.notification.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import iti.student.finalproject.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iti.student.finalproject.presentation.screen.notification.AlertType
import iti.student.finalproject.ui.theme.WeatherAccentBlue
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    onDismiss: () -> Unit,
    onSave: (start: Long, end: Long, type: AlertType) -> Unit
) {

    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var selectedType by remember { mutableStateOf(AlertType.RAIN) }

    var startHour by remember { mutableStateOf(0) }
    var startMinute by remember { mutableStateOf(0) }

    var endHour by remember { mutableStateOf(0) }
    var endMinute by remember { mutableStateOf(0) }

    var openStartTimePicker by remember { mutableStateOf(false) }
    var openEndTimePicker by remember { mutableStateOf(false) }

    var openStartPicker by remember { mutableStateOf(false) }
    var openEndPicker by remember { mutableStateOf(false) }

    var validationMessageId by remember { mutableStateOf<Int?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                stringResource(R.string.create_weather_alert),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(R.string.alert_type),
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.horizontalScroll(
                    state = rememberScrollState(),
                    overscrollEffect = null,
                    enabled = true,
                    flingBehavior = null,
                    reverseScrolling = false
                )
            ) {
                AlertTypeChip(AlertType.RAIN, selectedType) { selectedType = it }
                AlertTypeChip(AlertType.STORM, selectedType) { selectedType = it }
                AlertTypeChip(AlertType.WEATHER, selectedType) { selectedType = it }
                AlertTypeChip(AlertType.TEMPERATURE, selectedType) { selectedType = it }
            }

            DatePickerField(
                title = stringResource(R.string.start_date),
                date = startDate,
                onClick = { openStartPicker = true }
            )

            DatePickerField(
                title = stringResource(R.string.end_date),
                date = endDate,
                onClick = { openEndPicker = true }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (startDate == null || endDate == null) return@Button
                    val now = System.currentTimeMillis()
                    if (startDate!! <= now || endDate!! <= now) {
                        validationMessageId = R.string.date_must_be_future
                        return@Button
                    }
                    if (endDate!! <= startDate!!) {
                        validationMessageId = R.string.end_must_be_after_start
                        return@Button
                    }
                    onSave(startDate!!, endDate!!, selectedType)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WeatherAccentBlue
                )
            ) {
                Text(stringResource(R.string.save_alert))
            }
        }
    }

    if (openStartPicker) {

        val state = rememberDatePickerState(selectableDates = selectableFromDayOnward(todayStartMillis()))

        DatePickerDialog(
            onDismissRequest = { openStartPicker = false },
            confirmButton = {
                Button(onClick = {
                    startDate = state.selectedDateMillis
                    openStartPicker = false
                    openStartTimePicker = true
                }) { Text(stringResource(R.string.ok)) }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (openEndPicker) {

        val minEndDay = maxOf(
            todayStartMillis(),
            startDate?.let { calendarStartFromPickerMillis(it) } ?: todayStartMillis()
        )
        val state =
            rememberDatePickerState(selectableDates = selectableFromDayOnward(minEndDay))

        DatePickerDialog(
            onDismissRequest = { openEndPicker = false },
            confirmButton = {
                Button(onClick = {
                    endDate = state.selectedDateMillis
                    openEndPicker = false
                    openEndTimePicker = true
                }) { Text(stringResource(R.string.ok)) }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (openStartTimePicker) {

        val timeState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { openStartTimePicker = false },
            confirmButton = {
                Button(onClick = {

                    startHour = timeState.hour
                    startMinute = timeState.minute

                    val combined = startDate?.let {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = it
                            set(Calendar.HOUR_OF_DAY, startHour)
                            set(Calendar.MINUTE, startMinute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        calendar.timeInMillis
                    }
                    combined?.let {
                        if (it <= System.currentTimeMillis()) {
                            validationMessageId = R.string.date_must_be_future
                        } else {
                            startDate = it
                        }
                    }

                    openStartTimePicker = false
                }) { Text(stringResource(R.string.ok)) }
            },
            text = {
                TimePicker(state = timeState)
            }
        )
    }


    if (openEndTimePicker) {

        val timeState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { openEndTimePicker = false },
            confirmButton = {
                Button(onClick = {

                    endHour = timeState.hour
                    endMinute = timeState.minute

                    val combined = endDate?.let {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = it
                            set(Calendar.HOUR_OF_DAY, endHour)
                            set(Calendar.MINUTE, endMinute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        calendar.timeInMillis
                    }
                    combined?.let { ts ->
                        val now = System.currentTimeMillis()
                        when {
                            ts <= now -> validationMessageId = R.string.date_must_be_future
                            startDate != null && ts <= startDate!! ->
                                validationMessageId = R.string.end_must_be_after_start

                            else -> endDate = ts
                        }
                    }

                    openEndTimePicker = false
                }) { Text(stringResource(R.string.ok)) }
            },
            text = {
                TimePicker(state = timeState)
            }
        )
    }

    validationMessageId?.let { msgId ->
        AlertDialog(
            onDismissRequest = { validationMessageId = null },
            confirmButton = {
                TextButton(onClick = { validationMessageId = null }) {
                    Text(stringResource(R.string.ok))
                }
            },
            text = { Text(stringResource(msgId)) }
        )
    }
}

private fun calendarStartFromPickerMillis(millis: Long): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun todayStartMillis(): Long =
    calendarStartFromPickerMillis(Calendar.getInstance().timeInMillis)

@OptIn(ExperimentalMaterial3Api::class)
private fun selectableFromDayOnward(minDayStartInclusive: Long) = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return calendarStartFromPickerMillis(utcTimeMillis) >= minDayStartInclusive
    }
}