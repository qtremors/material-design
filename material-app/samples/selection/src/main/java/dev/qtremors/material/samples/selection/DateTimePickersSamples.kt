package dev.qtremors.material.samples.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSample(modifier: Modifier = Modifier) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showRangePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val dateRangePickerState = rememberDateRangePickerState()
    val locale = LocalLocale.current.platformLocale

    val selectedDateText = datePickerState.selectedDateMillis?.let {
        SimpleDateFormat("MMM dd, yyyy", locale).format(Date(it))
    } ?: "No date selected"

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Date Picker Dialog", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Selected date: $selectedDateText", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { showDatePickerDialog = true }) {
                    Text("Select Date")
                }
            }
        }

        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Text("Inline Date Range Picker", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { showRangePicker = !showRangePicker }) {
                    Text(if (showRangePicker) "Hide Range Picker" else "Show Range Picker")
                }
                if (showRangePicker) {
                    DateRangePicker(
                        state = dateRangePickerState,
                        modifier = Modifier.height(420.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSample(modifier: Modifier = Modifier) {
    var showDialPicker by remember { mutableStateOf(false) }
    var useTextEntry by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(initialHour = 10, initialMinute = 30)
    val locale = LocalLocale.current.platformLocale
    val formattedTime = String.format(locale, "%02d:%02d", timePickerState.hour, timePickerState.minute)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Time Picker", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Selected time: $formattedTime", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        useTextEntry = false
                        showDialPicker = true
                    }) {
                        Text("Dial Picker")
                    }
                    OutlinedButton(onClick = {
                        useTextEntry = true
                        showDialPicker = true
                    }) {
                        Text("Text Input")
                    }
                }
            }
        }

        if (showDialPicker) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDialPicker = false },
                title = { Text(if (useTextEntry) "Enter Time" else "Select Time") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (useTextEntry) {
                            TimeInput(state = timePickerState)
                        } else {
                            TimePicker(state = timePickerState)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialPicker = false }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialPicker = false }) { Text("Cancel") }
                }
            )
        }
    }
}
