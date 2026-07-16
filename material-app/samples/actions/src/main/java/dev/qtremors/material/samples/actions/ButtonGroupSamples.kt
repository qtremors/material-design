package dev.qtremors.material.samples.actions

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ElevatedToggleButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TonalToggleButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@Composable
fun ButtonGroupsSample(modifier: Modifier = Modifier) {
    var lastAction by remember { mutableStateOf("No action yet") }
    var selectedView by remember { mutableIntStateOf(0) }
    val enabledTools = remember { mutableStateListOf(0, 2) }
    val views = listOf("Cards", "List", "Preview")
    val viewIcons = listOf(Icons.Outlined.GridView, Icons.Outlined.ViewAgenda, Icons.Outlined.Preview)
    val selectedViewIcons = listOf(Icons.Filled.GridView, Icons.Filled.ViewAgenda, Icons.Filled.Preview)
    val tools = listOf("Edit", "Color", "Code")
    val toolIcons = listOf(Icons.Outlined.Edit, Icons.Outlined.Palette, Icons.Outlined.Code)
    val selectedToolIcons = listOf(Icons.Filled.Edit, Icons.Filled.Palette, Icons.Filled.Code)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ActionReferenceSection(
            title = "Standard group with overflow",
            description = "Related actions share a row. The group preserves every action in an overflow menu when width is constrained.",
        ) {
            ButtonGroup(
                overflowIndicator = { menuState ->
                    ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                clickableItem(
                    onClick = { lastAction = "Created a reference" },
                    label = "Create",
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    weight = 1f,
                )
                clickableItem(
                    onClick = { lastAction = "Duplicated the reference" },
                    label = "Duplicate",
                    icon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                )
                clickableItem(
                    onClick = { lastAction = "Shared the reference" },
                    label = "Share",
                    icon = { Icon(Icons.Default.Share, contentDescription = null) },
                )
                clickableItem(
                    onClick = { lastAction = "Removed the reference" },
                    label = "Delete",
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                )
            }
            Text(
                lastAction,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ActionReferenceSection(
            title = "Connected single choice",
            description = "Connected buttons express one choice from a compact, closely related set.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                views.forEachIndexed { index, label ->
                    val selected = selectedView == index
                    ToggleButton(
                        checked = selected,
                        onCheckedChange = { selectedView = index },
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            views.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                        modifier = Modifier.semantics { role = Role.RadioButton },
                    ) {
                        Icon(
                            if (selected) selectedViewIcons[index] else viewIcons[index],
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(label)
                    }
                }
            }
            Text(
                "${views[selectedView]} presentation selected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ActionReferenceSection(
            title = "Connected multiple choice",
            description = "Independent toggles may be connected only when their effects are compatible and can operate together.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                tools.forEachIndexed { index, label ->
                    val checked = index in enabledTools
                    ToggleButton(
                        checked = checked,
                        onCheckedChange = {
                            if (checked) enabledTools.remove(index) else enabledTools.add(index)
                        },
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            tools.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                    ) {
                        Icon(
                            if (checked) selectedToolIcons[index] else toolIcons[index],
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(label)
                    }
                }
            }
            Text(
                if (enabledTools.isEmpty()) "No tools visible" else "Visible: ${enabledTools.sorted().joinToString { tools[it] }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ActionReferenceSection(
            title = "Command history",
            description = "Groups clarify peer actions, including temporary disabled states, without implying selection.",
        ) {
            ButtonGroup(
                overflowIndicator = { menuState -> ButtonGroupDefaults.OverflowIndicator(menuState) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                clickableItem(
                    onClick = { lastAction = "Undid the latest change" },
                    label = "Undo",
                    icon = { Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null) },
                    weight = 1f,
                )
                clickableItem(
                    onClick = { lastAction = "Redid the latest change" },
                    label = "Redo",
                    icon = { Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = null) },
                    enabled = false,
                    weight = 1f,
                )
                clickableItem(
                    onClick = { lastAction = "Opened command history" },
                    label = "History",
                    icon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@Composable
fun ToggleButtonsSample(modifier: Modifier = Modifier) {
    var filled by remember { mutableStateOf(true) }
    var tonal by remember { mutableStateOf(false) }
    var elevated by remember { mutableStateOf(false) }
    var outlined by remember { mutableStateOf(false) }
    var sizeExample by remember { mutableIntStateOf(1) }
    val sizeLabels = listOf("Small", "Medium", "Large")
    val sizes = listOf(
        ButtonDefaults.ExtraSmallContainerHeight,
        ButtonDefaults.MediumContainerHeight,
        ButtonDefaults.LargeContainerHeight,
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ActionReferenceSection(
            title = "Visual hierarchy",
            description = "Toggle button styles carry the same emphasis ladder as ordinary buttons while retaining checked state.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ToggleButton(checked = filled, onCheckedChange = { filled = it }) {
                    ToggleIcon(filled, Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite)
                    Text("Filled")
                }
                TonalToggleButton(checked = tonal, onCheckedChange = { tonal = it }) {
                    ToggleIcon(tonal, Icons.Outlined.Palette, Icons.Filled.Palette)
                    Text("Tonal")
                }
                ElevatedToggleButton(checked = elevated, onCheckedChange = { elevated = it }) {
                    ToggleIcon(elevated, Icons.Outlined.Share, Icons.Filled.Share)
                    Text("Elevated")
                }
                OutlinedToggleButton(checked = outlined, onCheckedChange = { outlined = it }) {
                    ToggleIcon(outlined, Icons.Outlined.Code, Icons.Filled.Code)
                    Text("Outlined")
                }
            }
            val active = buildList {
                if (filled) add("Filled")
                if (tonal) add("Tonal")
                if (elevated) add("Elevated")
                if (outlined) add("Outlined")
            }
            Text(
                if (active.isEmpty()) "No independent options selected" else "Selected: ${active.joinToString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ActionReferenceSection(
            title = "Expressive size scale",
            description = "Container height, icon size, padding, text style, and shape scale together as one system.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                sizes.forEachIndexed { index, size ->
                    val selected = sizeExample == index
                    ToggleButton(
                        checked = selected,
                        onCheckedChange = { sizeExample = index },
                        modifier = Modifier.heightIn(min = size).semantics { role = Role.RadioButton },
                        shapes = ToggleButtonDefaults.shapesFor(size),
                        contentPadding = ButtonDefaults.contentPaddingFor(size),
                    ) {
                        Icon(
                            if (selected) Icons.Filled.Check else Icons.Outlined.Preview,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
                        )
                        Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(size)))
                        Text(sizeLabels[index], style = ButtonDefaults.textStyleFor(size), maxLines = 1)
                    }
                }
            }
        }

        ActionReferenceSection(
            title = "Unavailable option",
            description = "A disabled toggle communicates both the remembered state and its temporary unavailability.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ToggleButton(checked = true, onCheckedChange = {}, enabled = false) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Grid locked")
                }
                OutlinedToggleButton(checked = false, onCheckedChange = {}, enabled = false) {
                    Text("Labels unavailable")
                }
            }
        }
    }
}

@Composable
private fun ToggleIcon(
    checked: Boolean,
    uncheckedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    checkedIcon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Icon(
        if (checked) checkedIcon else uncheckedIcon,
        contentDescription = null,
        modifier = Modifier.size(ButtonDefaults.IconSize),
    )
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
}

@Composable
private fun ActionReferenceSection(
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
