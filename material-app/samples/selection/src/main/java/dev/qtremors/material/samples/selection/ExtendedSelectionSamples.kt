package dev.qtremors.material.samples.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.RichTimePickerDialog
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberSliderState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSlidersSample(modifier: Modifier = Modifier) {
    val brightnessState = rememberSliderState(value = 60f, valueRange = 0f..100f)
    val volumeState = rememberSliderState(value = 40f, valueRange = 0f..100f)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Vertical sliders", style = MaterialTheme.typography.titleMedium)
        Card {
            Row(
                Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VerticalSlider(
                        state = brightnessState,
                        modifier = Modifier.height(180.dp),
                    )
                    Text("Brightness: ${brightnessState.value.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VerticalSlider(
                        state = volumeState,
                        modifier = Modifier.height(180.dp),
                        topToBottom = false,
                    )
                    Text("Volume (bottom-up): ${volumeState.value.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBarsSample(modifier: Modifier = Modifier) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val suggestions = listOf("Buttons", "Dialogs", "Sliders", "Navigation bar")
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { },
            placeholder = { Text("Search references...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (textFieldState.text.isNotEmpty()) {
                    IconButton(onClick = { textFieldState.clearText() }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear query")
                    }
                }
            },
        )
    }

    Box(modifier.fillMaxSize()) {
        TopSearchBar(state = searchBarState, inputField = inputField)
        ExpandedFullScreenSearchBar(
            state = searchBarState,
            inputField = inputField,
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(suggestions.filter {
                    it.contains(textFieldState.text.toString(), ignoreCase = true)
                }) { suggestion ->
                    Text(
                        suggestion,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogsSample(modifier: Modifier = Modifier) {
    var showStandard by remember { mutableStateOf(false) }
    var showRich by remember { mutableStateOf(false) }
    var confirmed by remember { mutableStateOf<String?>(null) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(confirmed ?: "No dialog confirmed yet.", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { showStandard = true }) { Text("TimePickerDialog") }
            Button(onClick = { showRich = true }) { Text("RichTimePickerDialog") }
        }
    }

    if (showStandard) {
        val timePickerState = rememberTimePickerState()
        TimePickerDialog(
            onDismissRequest = { showStandard = false },
            confirmButton = {
                TextButton(onClick = {
                    confirmed = "Standard dialog: ${timePickerState.hour}:${timePickerState.minute}"
                    showStandard = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showStandard = false }) { Text("Dismiss") }
            },
            title = { Text("Select time") },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (showRich) {
        val richState = rememberTimePickerState()
        RichTimePickerDialog(
            onDismissRequest = { showRich = false },
            confirmButton = {
                TextButton(onClick = {
                    confirmed = "Rich dialog: ${richState.hour}:${richState.minute}"
                    showRich = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showRich = false }) { Text("Dismiss") }
            },
        ) {
            TimePicker(state = richState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialogsSample(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Basic alert dialogs", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { showDialog = true }) { Text("Open basic alert dialog") }
    }

    if (showDialog) {
        BasicAlertDialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Custom alert layout", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "BasicAlertDialog provides only the dialog window; you compose the surface, text, and actions.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = { showDialog = false }) { Text("Close") }
                }
            }
        }
    }
}

@Composable
fun DividersSample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Horizontal and vertical dividers", style = MaterialTheme.typography.titleMedium)
        Text("Section above", style = MaterialTheme.typography.bodyMedium)
        HorizontalDivider()
        Text("Section below a full-width horizontal divider", style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Left of vertical divider", style = MaterialTheme.typography.bodyMedium)
            Box(Modifier.height(24.dp).padding(horizontal = 8.dp)) {
                VerticalDivider()
            }
            Text("Right of vertical divider", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
