package dev.qtremors.material.samples.foundations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var weight by remember { mutableFloatStateOf(650f) }
    var width by remember { mutableFloatStateOf(105f) }
    val axes = VariableTypographyAxes(weight = weight, width = width, opticalSize = 32f)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        VariableTypographyPreview(
            text = "Material moves",
            axes = axes,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Weight ${weight.toInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(
            value = weight,
            onValueChange = { weight = it },
            valueRange = 300f..900f,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Width ${width.toInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(
            value = width,
            onValueChange = { width = it },
            valueRange = 75f..125f,
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
