package dev.qtremors.material.samples.selection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsSample(modifier: Modifier = Modifier) {
    var compact by remember { mutableStateOf(true) }
    val activeFilters = remember { mutableStateListOf("Working") }
    val collaborators = remember { mutableStateListOf("Ari", "Mina") }
    var suggestionMessage by remember { mutableStateOf("Choose a suggestion") }
    val reducedMotion = LocalReducedMotion.current

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ReferenceSection(
            title = "Action and suggestion",
            description = "Assist chips trigger a useful action. Suggestion chips offer a contextual response.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistChip(
                    onClick = { compact = !compact },
                    label = { Text(if (compact) "Show details" else "Use compact view") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                        )
                    },
                )
                ElevatedAssistChip(
                    onClick = { suggestionMessage = "Source path copied" },
                    label = { Text("Copy source path") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                        )
                    },
                )
                SuggestionChip(
                    onClick = { suggestionMessage = "Opened the accessibility state" },
                    label = { Text("Inspect accessibility") },
                )
            }
            AnimatedVisibility(
                visible = !compact || suggestionMessage != "Choose a suggestion",
                enter = if (reducedMotion) EnterTransition.None else fadeIn(),
                exit = if (reducedMotion) ExitTransition.None else fadeOut(),
            ) {
                Text(
                    if (!compact) "Supporting information remains close to the action it explains." else suggestionMessage,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        ReferenceSection(
            title = "Filter",
            description = "Filter chips independently include or exclude categories. Selection survives reflow.",
        ) {
            val options = listOf("Working", "Official API", "Expressive", "Foundations", "Bookmarked")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                options.forEach { label ->
                    val selected = label in activeFilters
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected) activeFilters.remove(label) else activeFilters.add(label)
                        },
                        label = { Text(label) },
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }
            Text(
                if (activeFilters.isEmpty()) "No filters: showing everything" else "Showing: ${activeFilters.joinToString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ReferenceSection(
            title = "Input",
            description = "Input chips represent user-provided values. Removal is a separate, labelled action.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                collaborators.forEach { person ->
                    InputChip(
                        selected = true,
                        onClick = { collaborators.remove(person) },
                        label = { Text(person) },
                        avatar = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove $person",
                                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            )
                        },
                    )
                }
                AssistChip(
                    onClick = {
                        val next = listOf("Noor", "Sam", "Ira").firstOrNull { it !in collaborators }
                        if (next != null) collaborators.add(next)
                    },
                    enabled = collaborators.size < 5,
                    label = { Text("Add person") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                        )
                    },
                )
            }
        }

        ReferenceSection(
            title = "Constrained width",
            description = "A single-line chip set scrolls horizontally instead of compressing labels below readable sizes.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("Color", "Typography", "Shape", "Motion", "Elevation", "Accessibility").forEach { label ->
                    FilterChip(
                        selected = label == "Motion",
                        onClick = {},
                        label = { Text(label, maxLines = 1) },
                    )
                }
            }
        }
    }
}

@Composable
fun SegmentedButtonsSample(modifier: Modifier = Modifier) {
    var densityIndex by remember { mutableIntStateOf(1) }
    val visibleLayers = remember { mutableStateListOf(0, 1) }
    val densityLabels = listOf("Compact", "Comfortable", "Expanded")
    val densityIcons = listOf(Icons.AutoMirrored.Outlined.FormatListBulleted, Icons.Outlined.GridView, Icons.Outlined.Contrast)
    val selectedDensityIcons = listOf(Icons.AutoMirrored.Filled.FormatListBulleted, Icons.Filled.GridView, Icons.Filled.Contrast)
    val layerLabels = listOf("Labels", "Grid", "Code")
    val layerIcons = listOf(Icons.AutoMirrored.Outlined.FormatListBulleted, Icons.Outlined.GridView, Icons.Outlined.Code)
    val selectedLayerIcons = listOf(Icons.AutoMirrored.Filled.FormatListBulleted, Icons.Filled.GridView, Icons.Filled.Code)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ReferenceSection(
            title = "Single choice",
            description = "Use a segmented button when one option from a small, stable set must remain visible.",
        ) {
            Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                SingleChoiceSegmentedButtonRow {
                    densityLabels.forEachIndexed { index, label ->
                        SegmentedButton(
                            selected = densityIndex == index,
                            onClick = { densityIndex = index },
                            shape = SegmentedButtonDefaults.itemShape(index, densityLabels.size),
                            icon = {
                                SegmentedButtonDefaults.Icon(active = densityIndex == index) {
                                    Icon(
                                        if (densityIndex == index) selectedDensityIcons[index] else densityIcons[index],
                                        contentDescription = null,
                                        modifier = Modifier.size(SegmentedButtonDefaults.IconSize),
                                    )
                                }
                            },
                        ) { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    }
                }
            }
            Text(
                "${densityLabels[densityIndex]} density selected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ReferenceSection(
            title = "Multiple choice",
            description = "Each segment independently controls a compatible layer; at least one remains selected here.",
        ) {
            Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                MultiChoiceSegmentedButtonRow {
                    layerLabels.forEachIndexed { index, label ->
                        val checked = index in visibleLayers
                        SegmentedButton(
                            checked = checked,
                            onCheckedChange = {
                                if (checked && visibleLayers.size > 1) visibleLayers.remove(index)
                                else if (!checked) visibleLayers.add(index)
                            },
                            shape = SegmentedButtonDefaults.itemShape(index, layerLabels.size),
                            icon = {
                                SegmentedButtonDefaults.Icon(active = checked) {
                                    Icon(
                                        if (checked) selectedLayerIcons[index] else layerIcons[index],
                                        contentDescription = null,
                                        modifier = Modifier.size(SegmentedButtonDefaults.IconSize),
                                    )
                                }
                            },
                        ) { Text(label) }
                    }
                }
            }
            Text(
                "Visible layers: ${visibleLayers.sorted().joinToString { layerLabels[it] }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ReferenceSection(
            title = "Disabled context",
            description = "Keep the complete set visible when temporarily unavailable so the user understands the model.",
        ) {
            SingleChoiceSegmentedButtonRow {
                listOf("Day", "Week", "Month").forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = index == 1,
                        onClick = {},
                        enabled = false,
                        shape = SegmentedButtonDefaults.itemShape(index, 3),
                    ) { Text(label) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectionControlsSample(modifier: Modifier = Modifier) {
    val checklist = remember { mutableStateListOf(true, false, false) }
    var themeIndex by remember { mutableIntStateOf(0) }
    var dynamicColor by remember { mutableStateOf(true) }
    var motionPreview by remember { mutableStateOf(true) }
    val themeLabels = listOf("System", "Light", "Dark")
    val themeIcons = listOf(Icons.Outlined.Contrast, Icons.Outlined.LightMode, Icons.Outlined.DarkMode)
    val selectedThemeIcons = listOf(Icons.Filled.Contrast, Icons.Filled.LightMode, Icons.Filled.DarkMode)
    val checkedCount = checklist.count { it }
    val parentState = when (checkedCount) {
        0 -> ToggleableState.Off
        checklist.size -> ToggleableState.On
        else -> ToggleableState.Indeterminate
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ReferenceSection(
            title = "Checkbox hierarchy",
            description = "Checkboxes support independent choices. A parent reflects the combined child state.",
        ) {
            ListItem(
                supportingContent = { Text("$checkedCount of ${checklist.size} enabled") },
                leadingContent = {
                    TriStateCheckbox(
                        state = parentState,
                        onClick = {
                            val next = parentState != ToggleableState.On
                            checklist.indices.forEach { checklist[it] = next }
                        },
                    )
                },
            ) { Text("All verification checks", fontWeight = FontWeight.Bold) }
            listOf("Touch targets", "Screen reader labels", "Large text reflow").forEachIndexed { index, label ->
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = checklist[index],
                            onCheckedChange = { checklist[index] = it },
                        )
                    },
                ) { Text(label) }
            }
        }

        ReferenceSection(
            title = "Radio choice",
            description = "Radio buttons select exactly one mutually exclusive option and expose a group relationship.",
        ) {
            Column(Modifier.selectableGroup()) {
                themeLabels.forEachIndexed { index, label ->
                    ListItem(
                        leadingContent = {
                            RadioButton(
                                selected = themeIndex == index,
                                onClick = { themeIndex = index },
                                modifier = Modifier.semantics { role = Role.RadioButton },
                            )
                        },
                        trailingContent = {
                            Icon(
                                if (themeIndex == index) selectedThemeIcons[index] else themeIcons[index],
                                contentDescription = null,
                            )
                        },
                    ) { Text(label) }
                }
            }
        }

        ReferenceSection(
            title = "Immediate settings",
            description = "Switches change a binary setting immediately; the complete row describes the consequence.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column {
                    ListItem(
                        checked = dynamicColor,
                        onCheckedChange = { dynamicColor = it },
                        supportingContent = { Text("Use colors derived from this device") },
                        trailingContent = {
                            Switch(checked = dynamicColor, onCheckedChange = null)
                        },
                    ) { Text("Dynamic color") }
                    ListItem(
                        checked = motionPreview,
                        onCheckedChange = { motionPreview = it },
                        supportingContent = { Text("Play decorative transitions in demonstrations") },
                        trailingContent = {
                            Switch(checked = motionPreview, onCheckedChange = null)
                        },
                    ) { Text("Motion preview") }
                }
            }
        }

        ReferenceSection(
            title = "Disabled state",
            description = "Disabled controls stay understandable and retain supporting context.",
        ) {
            ListItem(
                enabled = false,
                supportingContent = { Text("Unavailable when dynamic color is off") },
                trailingContent = { Switch(checked = false, onCheckedChange = null, enabled = false) },
            ) { Text("Device-only palette") }
        }
    }
}

@Composable
private fun ReferenceSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        content()
    }
}
