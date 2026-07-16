package dev.qtremors.material.samples.foundations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.qtremors.material.core.designsystem.ExpressiveMotion
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.expressiveSpring
import dev.qtremors.material.core.designsystem.pressSpring
import dev.qtremors.material.core.designsystem.progressSpring
import dev.qtremors.material.core.designsystem.standardSpring
import kotlin.math.roundToInt

private data class ColorRole(
    val name: String,
    val color: Color,
    val onColor: Color,
    val container: Color,
    val onContainer: Color,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorSample(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val roles = listOf(
        ColorRole("Primary", scheme.primary, scheme.onPrimary, scheme.primaryContainer, scheme.onPrimaryContainer),
        ColorRole("Secondary", scheme.secondary, scheme.onSecondary, scheme.secondaryContainer, scheme.onSecondaryContainer),
        ColorRole("Tertiary", scheme.tertiary, scheme.onTertiary, scheme.tertiaryContainer, scheme.onTertiaryContainer),
        ColorRole("Error", scheme.error, scheme.onError, scheme.errorContainer, scheme.onErrorContainer),
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Semantic role families",
            description = "Every accent family pairs a strong role with an on-role color and a quieter container pair.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                roles.forEach { role ->
                    ColorRoleCard(role, Modifier.widthIn(min = 150.dp).weight(1f))
                }
            }
        }

        FoundationSection(
            title = "Surface hierarchy",
            description = "Surface containers separate regions quietly. Accent containers identify meaningful emphasis, not decoration.",
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = scheme.surfaceContainerLow,
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = scheme.primary)
                            Column(Modifier.weight(1f)) {
                                Text("Low container", fontWeight = FontWeight.Bold)
                                Text("Groups ordinary supporting content", color = scheme.onSurfaceVariant)
                            }
                        }
                    }
                    Surface(
                        color = scheme.surfaceContainerHigh,
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Default.Contrast, contentDescription = null, tint = scheme.secondary)
                            Column(Modifier.weight(1f)) {
                                Text("High container", fontWeight = FontWeight.Bold)
                                Text("Separates a stronger nested region", color = scheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "Meaning survives theme changes",
            description = "Components consume semantic roles. They do not assume that primary is purple or that a surface is white.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = {}) {
                    Icon(Icons.Default.LightMode, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Theme aware")
                }
                OutlinedButton(onClick = {}) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Role based")
                }
            }
        }

        FoundationSection(
            title = "Non-color cue",
            description = "Status uses icon, label, and semantics alongside color so meaning survives low vision and monochrome contexts.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(scheme.errorContainer, MaterialTheme.shapes.large).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = scheme.onErrorContainer)
                Column {
                    Text("Validation failed", color = scheme.onErrorContainer, fontWeight = FontWeight.Bold)
                    Text("The source path does not exist", color = scheme.onErrorContainer)
                }
            }
        }
    }
}

@Composable
private fun ColorRoleCard(role: ColorRole, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(64.dp).background(role.color).padding(12.dp),
                contentAlignment = Alignment.BottomStart,
            ) { Text(role.name, color = role.onColor, fontWeight = FontWeight.Bold) }
            Box(
                modifier = Modifier.fillMaxWidth().height(74.dp).background(role.container).padding(12.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                Column {
                    Text("${role.name} container", color = role.onContainer, style = MaterialTheme.typography.labelLarge)
                    Text("on-${role.name.lowercase()}", color = role.onContainer, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private data class ShapeRole(val name: String, val purpose: String, val shape: Shape)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShapeSample(modifier: Modifier = Modifier) {
    var expressive by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableIntStateOf(1) }
    val roles = listOf(
        ShapeRole("Small", "Chips and compact controls", MaterialTheme.shapes.small),
        ShapeRole("Medium", "Buttons and contained rows", MaterialTheme.shapes.medium),
        ShapeRole("Large", "Cards and grouped content", MaterialTheme.shapes.large),
        ShapeRole("Extra large", "Sheets and prominent regions", MaterialTheme.shapes.extraLarge),
    )
    val animatedCorner by animateDpAsState(
        targetValue = if (expressive) 12.dp else 32.dp,
        animationSpec = expressiveSpring(),
        label = "shape morph corner",
    )
    val animatedColor by animateColorAsState(
        targetValue = if (expressive) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = standardSpring(),
        label = "shape morph color",
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Shape scale",
            description = "Corner size follows component size and hierarchy. Expressiveness comes from contrast between roles.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                roles.forEachIndexed { index, role ->
                    val selected = selectedRole == index
                    Surface(
                        modifier = Modifier.widthIn(min = 150.dp).weight(1f).selectable(
                            selected = selected,
                            onClick = { selectedRole = index },
                            role = Role.RadioButton,
                        ),
                        shape = role.shape,
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    ) {
                        Column(Modifier.heightIn(min = 112.dp).padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(role.name, fontWeight = FontWeight.Bold)
                                RadioButton(selected = selected, onClick = null)
                            }
                            Text(role.purpose, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "Nested hierarchy",
            description = "Outer regions use broader corners; nested controls use tighter shapes so boundaries remain legible.",
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Reference collection", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.primary,
                            ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.GridView, null, tint = MaterialTheme.colorScheme.onPrimary) } }
                            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                                Text("Cards", fontWeight = FontWeight.Bold)
                                Text("Working containment reference", style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "Shape as state",
            description = "A controlled morph can reinforce state change, but the label, color, and semantics still carry meaning.",
        ) {
            Surface(
                onClick = { expressive = !expressive },
                modifier = Modifier.fillMaxWidth().height(86.dp).semantics {
                    stateDescription = if (expressive) "Focused state" else "Resting state"
                },
                shape = RoundedCornerShape(animatedCorner),
                color = animatedColor,
            ) {
                Row(
                    Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Icon(if (expressive) Icons.Default.Check else Icons.Default.OpenInFull, contentDescription = null)
                    Column(Modifier.weight(1f)) {
                        Text(if (expressive) "Focused state" else "Resting state", fontWeight = FontWeight.Bold)
                        Text("Tap to compare the shape response", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        FoundationSection(
            title = "Avoid uniform rounding",
            description = "If every region uses the same large pill shape, hierarchy disappears and the interface becomes visually noisy.",
        ) {
            Text(
                "Use shape contrast to clarify containment, interaction, and state—not as decoration applied to every object.",
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium).padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun MotionSample(modifier: Modifier = Modifier) {
    var destination by remember { mutableStateOf(false) }
    var pressedPreview by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.35f) }
    val reducedMotion = LocalReducedMotion.current
    val travel by animateDpAsState(
        targetValue = if (destination) 172.dp else 0.dp,
        animationSpec = expressiveSpring(),
        label = "expressive travel",
    )
    val scale by animateFloatAsState(
        targetValue = ExpressiveMotion.pressScale(pressedPreview, true),
        animationSpec = pressSpring(),
        label = "press scale preview",
    )
    val progressWidth by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressSpring(),
        label = "progress smoothing preview",
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Motion has jobs",
            description = "Different motion families communicate press, navigation, layout change, settling, progress, and rejection.",
        ) {
            val jobs = listOf(
                "Press" to "Medium-bouncy, immediate acknowledgement",
                "Spatial" to "Low-bouncy continuity across layout changes",
                "Settle" to "No-bounce shape and state resolution",
                "Progress" to "High-stiffness smoothing of frequent updates",
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                jobs.forEach { (name, purpose) ->
                    Row(
                        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.large).padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Animation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text(purpose, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "User-triggered spatial motion",
            description = "Motion starts from an action, preserves object identity, and remains interruptible when direction changes.",
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth().height(96.dp).background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.extraLarge).padding(12.dp),
            ) {
                val maxTravel = (maxWidth - 64.dp).coerceAtLeast(0.dp)
                val safeTravel = travel.coerceAtMost(maxTravel)
                Surface(
                    modifier = Modifier.offset(x = safeTravel).size(64.dp),
                    shape = if (destination) MaterialTheme.shapes.large else MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primary,
                ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = MaterialTheme.colorScheme.onPrimary) } }
            }
            Button(onClick = { destination = !destination }) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (destination) "Return" else "Move")
            }
        }

        FoundationSection(
            title = "Press feedback",
            description = "A press response is small, fast, and reversible. It never substitutes for the action's result.",
        ) {
            Surface(
                onClick = { pressedPreview = !pressedPreview },
                modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }.size(140.dp, 72.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(if (pressedPreview) "Pressed 0.94×" else "Resting 1.00×", fontWeight = FontWeight.Bold)
                }
            }
        }

        FoundationSection(
            title = "Progress smoothing",
            description = "Frequent updates settle quickly. The visual value follows real state and never invents completion.",
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(18.dp).clip(MaterialTheme.shapes.extraLarge).background(MaterialTheme.colorScheme.surfaceContainerHighest),
            ) {
                Box(
                    Modifier.fillMaxWidth(progressWidth).height(18.dp).background(MaterialTheme.colorScheme.primary),
                )
            }
            Slider(value = progress, onValueChange = { progress = it }, valueRange = 0f..1f)
            Text("Target ${(progress * 100).roundToInt()}%", style = MaterialTheme.typography.labelLarge)
        }

        FoundationSection(
            title = "Reduced motion",
            description = "Decorative spatial and opacity transitions snap to state while meaning and safety timing remain intact.",
        ) {
            Surface(
                color = if (reducedMotion) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.large,
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Animation, contentDescription = null)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(if (reducedMotion) "Reduced motion active" else "System motion active", fontWeight = FontWeight.Bold)
                        Text("Confirmation meaning and state changes are preserved", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ElevationSample(modifier: Modifier = Modifier) {
    var tonalLevel by remember { mutableFloatStateOf(2f) }
    var shadowLevel by remember { mutableFloatStateOf(1f) }
    val tonalDp = tonalLevel.roundToInt().dp
    val shadowDp = shadowLevel.roundToInt().dp

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Tonal and shadow elevation",
            description = "Tonal elevation changes surface color relationships. Shadow elevation communicates physical separation where needed.",
        ) {
            Box(
                Modifier.fillMaxWidth().height(190.dp).background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge).padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(130.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = tonalDp,
                    shadowElevation = shadowDp,
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Reference surface", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Tonal ${tonalDp.value.toInt()} dp · Shadow ${shadowDp.value.toInt()} dp")
                        Text("Adjust the two systems independently.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Text("Tonal elevation", style = MaterialTheme.typography.labelLarge)
            Slider(value = tonalLevel, onValueChange = { tonalLevel = it }, valueRange = 0f..6f, steps = 5)
            Text("Shadow elevation", style = MaterialTheme.typography.labelLarge)
            Slider(value = shadowLevel, onValueChange = { shadowLevel = it }, valueRange = 0f..6f, steps = 5)
        }

        FoundationSection(
            title = "Hierarchy comparison",
            description = "Most surfaces remain quiet. Elevation increases only where overlap, focus, or temporary prominence requires it.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                listOf(0.dp, 1.dp, 3.dp, 6.dp).forEach { level ->
                    Surface(
                        modifier = Modifier.widthIn(min = 130.dp).weight(1f).height(100.dp),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = level,
                        shadowElevation = if (level >= 3.dp) level else 0.dp,
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Text("${level.value.toInt()} dp", fontWeight = FontWeight.Bold)
                            Text(
                                when (level) {
                                    0.dp -> "Base"
                                    1.dp -> "Grouped"
                                    3.dp -> "Raised"
                                    else -> "Floating"
                                },
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "Avoid shadow-only hierarchy",
            description = "Shape, spacing, color roles, and content hierarchy should remain understandable when shadows are subtle or absent.",
        ) {
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.large).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null)
                Text("The relationship remains visible in dark themes and high-contrast contexts.", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun LayoutSample(modifier: Modifier = Modifier) {
    var simulatedWidth by remember { mutableFloatStateOf(360f) }
    val widthClass = when {
        simulatedWidth < 600f -> "Compact"
        simulatedWidth < 840f -> "Medium"
        else -> "Expanded"
    }
    val paneCount = when (widthClass) {
        "Compact" -> 1
        "Medium" -> 2
        else -> 3
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Window-based adaptation",
            description = "Layout decisions follow available window space, not a guessed device category or orientation.",
        ) {
            Text("Simulated width ${simulatedWidth.roundToInt()} dp · $widthClass", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Slider(value = simulatedWidth, onValueChange = { simulatedWidth = it }, valueRange = 280f..1000f)
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val previewWidth = simulatedWidth.dp.coerceAtMost(maxWidth)
                Surface(
                    modifier = Modifier.width(previewWidth).height(230.dp).align(Alignment.Center),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Row(Modifier.fillMaxSize().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(paneCount) { index ->
                            Surface(
                                modifier = Modifier.weight(if (index == 0) 1.2f else 1f).fillMaxSize(),
                                shape = MaterialTheme.shapes.large,
                                color = when (index) {
                                    0 -> MaterialTheme.colorScheme.primaryContainer
                                    1 -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.tertiaryContainer
                                },
                            ) {
                                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        when (index) {
                                            0 -> "Primary"
                                            1 -> "Supporting"
                                            else -> "Extra"
                                        },
                                        fontWeight = FontWeight.Bold,
                                    )
                                    repeat(3) { item ->
                                        Box(
                                            Modifier.fillMaxWidth().height(18.dp).background(
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f + item * 0.03f),
                                                MaterialTheme.shapes.small,
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FoundationSection(
            title = "Reflow before truncation",
            description = "Controls wrap, text grows vertically, and content moves between panes before meaningful labels are removed.",
        ) {
            BoxWithConstraints {
                if (maxWidth < 420.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdaptiveAction("Preview component", Icons.Default.Visibility)
                        AdaptiveAction("Inspect accessibility", Icons.Default.AccessibilityNew)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdaptiveAction("Preview component", Icons.Default.Visibility, Modifier.weight(1f))
                        AdaptiveAction("Inspect accessibility", Icons.Default.AccessibilityNew, Modifier.weight(1f))
                    }
                }
            }
        }

        FoundationSection(
            title = "Readable measure",
            description = "Expanded windows add panes or breathing room; they do not stretch paragraphs across the entire display.",
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.large,
            ) {
                Text(
                    "Constrain prose to a comfortable line length while allowing tables, galleries, and code to use the space their content requires.",
                    modifier = Modifier.padding(18.dp).widthIn(max = 620.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        FoundationSection(
            title = "Insets and obstruction",
            description = "Navigation, keyboards, cutouts, hinges, and floating controls reserve space instead of covering content.",
        ) {
            Row(
                Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.OpenInFull, contentDescription = null)
                Text("Test compact through expanded windows, resizable panes, rotation, and increased font scale.", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AdaptiveAction(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    FilledTonalButton(onClick = {}, modifier = modifier) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(label, maxLines = 2)
    }
}

@Composable
fun AccessibilitySample(modifier: Modifier = Modifier) {
    var textSize by remember { mutableFloatStateOf(18f) }
    var checked by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(false) }
    val systemFontScale = LocalDensity.current.fontScale

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FoundationSection(
            title = "Large text and reflow",
            description = "Text scaling is a layout condition. Containers grow and actions reflow before content is truncated.",
        ) {
            Text("Preview size ${textSize.roundToInt()} sp · system scale ${"%.2f".format(systemFontScale)}×", style = MaterialTheme.typography.labelLarge)
            Slider(value = textSize, onValueChange = { textSize = it }, valueRange = 14f..34f)
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Material interfaces stay understandable when text grows",
                        fontSize = textSize.sp,
                        lineHeight = (textSize * 1.25f).sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "Supporting content wraps naturally and keeps the primary action visible.",
                        fontSize = (textSize * 0.78f).coerceAtLeast(12f).sp,
                    )
                    Button(onClick = {}, modifier = Modifier.align(Alignment.End)) { Text("Continue") }
                }
            }
        }

        FoundationSection(
            title = "Touch target versus visible icon",
            description = "A compact icon can sit inside a larger target. The target, focus indication, and accessible name belong to the action.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.requiredSize(56.dp).border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = {}, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.TouchApp, contentDescription = "Open touch-target guidance", modifier = Modifier.size(24.dp))
                    }
                }
                Column {
                    Text("48 dp action target", fontWeight = FontWeight.Bold)
                    Text("24 dp visible icon", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        FoundationSection(
            title = "State has multiple cues",
            description = "Selection combines semantics, icon, label, shape, and color so no single sensory cue carries the meaning.",
        ) {
            Surface(
                onClick = { selected = !selected },
                modifier = Modifier.fillMaxWidth().semantics {
                    stateDescription = if (selected) "Selected" else "Not selected"
                },
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                shape = if (selected) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.large,
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (selected) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked, contentDescription = null)
                    Text(if (selected) "Included in comparison" else "Not included in comparison", Modifier.padding(start = 12.dp).weight(1f), fontWeight = FontWeight.Bold)
                }
            }
        }

        FoundationSection(
            title = "Complete row semantics",
            description = "The visible label and supporting text explain the control; the row exposes one coherent checked state.",
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Column(Modifier.padding(start = 12.dp).weight(1f)) {
                        Text("Run accessibility checks", fontWeight = FontWeight.Bold)
                        Text("Validate semantics and minimum touch targets", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        FoundationSection(
            title = "Accessibility review",
            description = "Automated checks catch regressions; manual assistive-technology and large-content review verifies the actual journey.",
        ) {
            val checks = listOf(
                "Touch and focus targets" to true,
                "Labels, roles, values, and state" to true,
                "Contrast and non-color cues" to true,
                "TalkBack order and announcements" to false,
                "Large text, zoom, and reflow" to false,
                "Reduced motion and interruption" to false,
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                checks.forEach { (label, automated) ->
                    Row(
                        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.medium).padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            if (automated) Icons.Default.Check else Icons.Default.AccessibilityNew,
                            contentDescription = null,
                            tint = if (automated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                        )
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(label, fontWeight = FontWeight.Bold)
                            Text(if (automated) "Automated + manual" else "Manual journey review", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FoundationSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        content()
    }
}
