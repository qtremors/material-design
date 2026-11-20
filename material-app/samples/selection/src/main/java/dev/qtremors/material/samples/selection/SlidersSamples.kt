package dev.qtremors.material.samples.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.rememberRangeSliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SliderSample(modifier: Modifier = Modifier) {
    val continuousValueSlider = rememberSliderState(value = 40f, trackRange = 0f..100f)
    val continuousValue = continuousValueSlider.value
    val discreteValueSlider = rememberSliderState(value = 60f, steps = 4, trackRange = 0f..100f)
    val discreteValue = discreteValueSlider.value
    val rangeSlider = rememberRangeSliderState(startValue = 20f, endValue = 80f, trackRange = 0f..100f)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Continuous Slider", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Value: ${continuousValue.roundToInt()}%", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    state = continuousValueSlider,
                    onValueChange = { continuousValueSlider.value = it },
                )
            }
        }

        Text("Discrete Slider (Steps = 5)", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Value: ${discreteValue.roundToInt()}%", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    state = discreteValueSlider,
                    onValueChange = { discreteValueSlider.value = it },
                )
            }
        }

        Text("Range Slider", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Range: ${rangeSlider.startValue.roundToInt()}% - ${rangeSlider.endValue.roundToInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                RangeSlider(
                    state = rangeSlider,
                    onValueChange = { range ->
                        rangeSlider.startValue = range.start
                        rangeSlider.endValue = range.endInclusive
                    },
                )
            }
        }
    }
}
