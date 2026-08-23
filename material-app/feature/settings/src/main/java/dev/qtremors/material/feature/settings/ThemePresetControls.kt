package dev.qtremors.material.feature.settings

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState

@Composable
fun ThemePresetSelector(
    currentPreset: ThemePreset,
    onPresetSelected: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.settings_theme_preset_header),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ThemePreset.entries.forEach { preset ->
                val isSelected = currentPreset == preset
                val label = when (preset) {
                    ThemePreset.NONE -> stringResource(R.string.settings_theme_preset_none)
                    ThemePreset.DRACULA -> stringResource(R.string.settings_theme_preset_dracula)
                    ThemePreset.TOKYO_NIGHT -> stringResource(R.string.settings_theme_preset_tokyo_night)
                    ThemePreset.CUSTOM -> stringResource(R.string.settings_theme_preset_custom)
                }
                val colors = if (isSelected) {
                    ButtonDefaults.filledTonalButtonColors()
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
                val border = if (isSelected) {
                    null
                } else {
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                }

                OutlinedButton(
                    onClick = { onPresetSelected(preset) },
                    colors = colors,
                    border = border,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
fun CustomThemeCreatorPanel(
    themeState: ThemeState,
    onThemeChange: (ThemeState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var primaryInput by remember(themeState.customPrimaryColorHex) {
        mutableStateOf(themeState.customPrimaryColorHex)
    }
    var backgroundInput by remember(themeState.customBackgroundColorHex) {
        mutableStateOf(themeState.customBackgroundColorHex)
    }
    var isPrimaryFocused by remember { mutableStateOf(false) }
    var isBackgroundFocused by remember { mutableStateOf(false) }

    val primaryScale by animateFloatAsState(
        targetValue = if (isPrimaryFocused) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "primaryScale",
    )
    val backgroundScale by animateFloatAsState(
        targetValue = if (isBackgroundFocused) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "backgroundScale",
    )

    val primaryParsed = remember(primaryInput) { primaryInput.parseColorOrNull() }
    val backgroundParsed = remember(backgroundInput) { backgroundInput.parseColorOrNull() }
    val colorsTooSimilar = remember(primaryParsed, backgroundParsed) {
        primaryParsed != null &&
            backgroundParsed != null &&
            kotlin.math.abs(backgroundParsed.luminanceEstimate() - primaryParsed.luminanceEstimate()) < 0.25f
    }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.settings_custom_colors_header),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            ThemeColorInput(
                value = primaryInput,
                onValueChange = { input ->
                    primaryInput = input
                    if (input.isValidThemeColor()) {
                        onThemeChange(themeState.copy(customPrimaryColorHex = input))
                    }
                },
                label = stringResource(R.string.settings_primary_color_hex_label),
                parsedColor = primaryParsed,
                scale = primaryScale,
                onFocusChange = { isPrimaryFocused = it },
                modifier = Modifier.padding(bottom = 12.dp),
            )
            ThemeColorInput(
                value = backgroundInput,
                onValueChange = { input ->
                    backgroundInput = input
                    if (input.isValidThemeColor()) {
                        onThemeChange(themeState.copy(customBackgroundColorHex = input))
                    }
                },
                label = stringResource(R.string.settings_background_color_hex_label),
                parsedColor = backgroundParsed,
                scale = backgroundScale,
                onFocusChange = { isBackgroundFocused = it },
                modifier = Modifier.padding(bottom = 8.dp),
            )
            if (colorsTooSimilar) {
                Text(
                    text = stringResource(R.string.settings_color_contrast_warning),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ThemeColorInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    parsedColor: Color?,
    scale: Float,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        trailingIcon = {
            parsedColor?.let { color ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChange(it.isFocused) }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
    )
}

private fun String.isValidThemeColor(): Boolean =
    startsWith("#") && (length == 7 || length == 9) && parseColorOrNull() != null

private fun String.parseColorOrNull(): Color? =
    try {
        Color(android.graphics.Color.parseColor(this))
    } catch (_: IllegalArgumentException) {
        null
    }

private fun Color.luminanceEstimate(): Float = red * 0.299f + green * 0.587f + blue * 0.114f
