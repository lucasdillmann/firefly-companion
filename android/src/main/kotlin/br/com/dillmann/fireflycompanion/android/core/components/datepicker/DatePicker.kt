package br.com.dillmann.fireflycompanion.android.core.components.datepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextField
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    dateTimeState: MutableState<OffsetDateTime>,
    type: DatePickerType,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    val context = LocalContext.current
    val dateTime by dateTimeState

    val formatter = when (type) {
        DatePickerType.DATE_ONLY ->
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

        DatePickerType.TIME_ONLY ->
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

        DatePickerType.DATE_AND_TIME ->
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    }

    AppTextField(
        value = TextFieldValue(formatter.format(dateTime)),
        onChange = {},
        label = label,
        errorMessage = errorMessage,
        readOnly = true,
        enabled = enabled && !readOnly,
        modifier = modifier
            .fillMaxWidth()
            .onFocusEvent {
                if (!it.isFocused || !enabled || readOnly)
                    return@onFocusEvent

                when (type) {
                    DatePickerType.DATE_ONLY ->
                        showDatePicker(context, dateTime, dateTimeState)

                    DatePickerType.TIME_ONLY ->
                        showTimePicker(context, dateTime, dateTimeState)

                    DatePickerType.DATE_AND_TIME ->
                        showDatePicker(context, dateTime, dateTimeState, true)
                }
            },
    )
}

private fun showDatePicker(
    context: android.content.Context,
    currentDateTime: OffsetDateTime,
    dateTimeState: MutableState<OffsetDateTime>,
    showTimeAfter: Boolean = false
) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newDateTime = currentDateTime
                .withYear(year)
                .withMonth(month + 1)
                .withDayOfMonth(dayOfMonth)

            dateTimeState.value = newDateTime

            if (showTimeAfter) {
                showTimePicker(context, newDateTime, dateTimeState)
            }
        },
        currentDateTime.year,
        currentDateTime.monthValue - 1,
        currentDateTime.dayOfMonth
    )

    datePickerDialog.show()
}

private fun showTimePicker(
    context: android.content.Context,
    currentDateTime: OffsetDateTime,
    dateTimeState: MutableState<OffsetDateTime>
) {
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val newDateTime = currentDateTime
                .withHour(hourOfDay)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0)

            dateTimeState.value = newDateTime
        },
        currentDateTime.hour,
        currentDateTime.minute,
        true // 24-hour format
    )

    timePickerDialog.show()
}
