package dev.qtremors.material.samples.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SlidersSample(modifier: Modifier = Modifier) {
    var continuousValue by remember { mutableFloatStateOf(40f) }
    var discreteValue by remember { mutableFloatStateOf(60f) }
    var rangeValues by remember { mutableStateOf(20f..80f) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Continuous Slider", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Value: ${continuousValue.roundToInt()}%", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = continuousValue,
                    onValueChange = { continuousValue = it },
                    valueRange = 0f..100f
                )
            }
        }

        Text("Discrete Slider (Steps = 5)", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Value: ${discreteValue.roundToInt()}%", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = discreteValue,
                    onValueChange = { discreteValue = it },
                    valueRange = 0f..100f,
                    steps = 4
                )
            }
        }

        Text("Range Slider", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Range: ${rangeValues.start.roundToInt()}% - ${rangeValues.endInclusive.roundToInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                RangeSlider(
                    value = rangeValues,
                    onValueChange = { rangeValues = it },
                    valueRange = 0f..100f
                )
            }
        }
    }
}
