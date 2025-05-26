package mefetran.dgusev.meddocs.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import mefetran.dgusev.meddocs.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.select_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel_button))
            }
        },
        modifier = modifier,
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "en")
@Composable
fun ModalDatePickerPreview(modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(true) }
    var date by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = date)
        if (showDatePicker) {
            ModalDatePicker(
                onDateSelected = { timeMillis ->
                    timeMillis?.let {
                        date = formatDate(timeMillis)
                    }
                },
                onDismiss = {
                    showDatePicker = !showDatePicker
                }
            )
        }
    }
}

fun formatDate(millis: Long, pattern: String = "yyyy-MM-dd"): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}