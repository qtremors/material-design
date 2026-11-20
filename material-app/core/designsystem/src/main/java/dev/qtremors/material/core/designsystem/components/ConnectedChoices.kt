package dev.qtremors.material.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.spacing
import kotlin.math.ceil

/**
 * A row inside a [ConnectedGroup] holding a title, a one-line explanation, and connected choices
 * underneath, so a setting with a few options never needs a dialog.
 */
@Composable
fun <T> ChoiceRow(
    title: String,
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    icon: ImageVector? = null
) {
    Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.rowHorizontal, vertical = MaterialTheme.spacing.rowVertical),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    IconBadge(icon = icon, key = title)
                    Spacer(Modifier.width(MaterialTheme.spacing.medium))
                }
                Column {
                    Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    if (supporting != null) {
                        Text(supporting, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            ConnectedChoices(options = options, selected = selected, label = label, onSelect = onSelect)
        }
    }
}

/**
 * Pick one of a few options with joined buttons ("System / Light / Dark"), instead of a radio
 * dialog. Buttons always share the full width. When the labels cannot fit on one line, the set
 * wraps into balanced rows (six options become two rows of three), so no option is ever cut off
 * or hidden behind a sideways scroll.
 */
@Composable
fun <T> ConnectedChoices(
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    /** Called when the already selected option is tapped again, for example to pick a date. */
    onReselect: ((T) -> Unit)? = null
) {
    val haptics = rememberAppHaptics()
    val labels = options.map { label(it) }
    val measurer = rememberTextMeasurer()
    val style = MaterialTheme.typography.labelLarge
    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val gap = ButtonGroupDefaults.ConnectedSpaceBetween
        // The widest label decides how many buttons fit side by side.
        val widest = with(density) { labels.maxOfOrNull { measurer.measure(it, style).size.width }?.toDp() } ?: 0.dp
        val needed = widest + BUTTON_PADDING * 2
        val fit = (((maxWidth + gap) / (needed + gap)).toInt()).coerceIn(1, options.size.coerceAtLeast(1))
        val rowCount = ceil(options.size / fit.toFloat()).toInt().coerceAtLeast(1)
        val perRow = ceil(options.size / rowCount.toFloat()).toInt().coerceAtLeast(1)
        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
            options.indices.chunked(perRow).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(gap)) {
                    row.forEachIndexed { position, index ->
                        val option = options[index]
                        val shapes = when {
                            row.size == 1 -> ToggleButtonShapes(CircleShape, CircleShape, CircleShape)
                            position == 0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            position == row.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        }
                        ToggleButton(
                            checked = option == selected,
                            onCheckedChange = {
                                haptics.tick()
                                if (option != selected) onSelect(option) else onReselect?.invoke(option)
                            },
                            modifier = Modifier.heightIn(min = 48.dp).weight(1f),
                            shapes = shapes,
                            colors = ToggleButtonDefaults.colors(
                                // Rows are surfaceBright, so unselected buttons sit one step above them to stay visible.
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                checkedContainerColor = MaterialTheme.colorScheme.primary,
                                checkedContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Text(labels[index], style = style, maxLines = 1, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

/** Room each button keeps on either side of its label. */
private val BUTTON_PADDING = 14.dp
