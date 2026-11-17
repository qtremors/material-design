package dev.qtremors.material.samples.foundations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.rememberSliderState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class VariableTypographyAxes(
    val weight: Float,
    val width: Float,
    val opticalSize: Float,
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun TypographySample(modifier: Modifier = Modifier) {
    val weightSlider = rememberSliderState(value = 650f, trackRange = 300f..900f)
    val weight = weightSlider.value
    val widthSlider = rememberSliderState(value = 105f, trackRange = 75f..125f)
    val width = widthSlider.value
    val axes = VariableTypographyAxes(weight = weight, width = width, opticalSize = 32f)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        VariableTypographyPreview(
            text = "Material moves",
            axes = axes,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Weight ${weight.toInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(
            state = weightSlider,
            onValueChange = { weightSlider.value = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Width ${width.toInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(
            state = widthSlider,
            onValueChange = { widthSlider.value = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Title large", style = MaterialTheme.typography.titleLarge)
        Text("Body large — readable content with a deliberate rhythm.", style = MaterialTheme.typography.bodyLarge)
        Text("Label large", style = MaterialTheme.typography.labelLarge)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun VariableTypographyPreview(
    text: String,
    axes: VariableTypographyAxes,
    modifier: Modifier = Modifier,
) {
    val fontFamily = remember(axes) {
        FontFamily(
            Font(
                resId = R.font.google_sans_flex_variable,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(axes.weight.toInt().coerceIn(1, 1000)),
                    FontVariation.width(axes.width.coerceIn(25f, 150f)),
                    FontVariation.Setting("opsz", axes.opticalSize.coerceIn(6f, 72f)),
                ),
            ),
        )
    }
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.displaySmall.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
        ),
    )
}

@Composable
fun DividerSample(modifier: Modifier = Modifier) {
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
