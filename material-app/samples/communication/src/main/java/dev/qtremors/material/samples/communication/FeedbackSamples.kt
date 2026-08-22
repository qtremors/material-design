package dev.qtremors.material.samples.communication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private enum class DialogExample { INFORMATION, CONFIRMATION, DESTRUCTIVE }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogsSample(modifier: Modifier = Modifier) {
    var activeDialog by remember { mutableStateOf<DialogExample?>(null) }
    var result by remember { mutableStateOf("Choose a dialog purpose") }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FeedbackSection(
            title = "Dialog purpose",
            description = "Dialogs interrupt the journey only for focused information, a necessary choice, or a consequential confirmation.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = { activeDialog = DialogExample.INFORMATION }) { Text("Information") }
                Button(onClick = { activeDialog = DialogExample.CONFIRMATION }) { Text("Confirmation") }
                TextButton(onClick = { activeDialog = DialogExample.DESTRUCTIVE }) { Text("Destructive") }
            }
            Text(result, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
        }

        FeedbackSection(
            title = "Interaction contract",
            description = "The title names the decision, body text explains consequences, and actions use explicit verbs.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DialogRule("Dismiss", "Back, outside tap, or a clear cancel action returns without changing state.")
                    DialogRule("Confirm", "The primary action applies once and closes only after the state is accepted.")
                    DialogRule("Restore focus", "Keyboard and accessibility focus return to the control that opened the dialog.")
                }
            }
        }

        FeedbackSection(
            title = "Avoid dialog chains",
            description = "Long forms, browsing, and multi-step setup belong on a destination or sheet, not in stacked dialogs.",
        ) {
            Text(
                "Use the least interruptive surface that preserves context and gives the content enough space.",
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    activeDialog?.let { example ->
        AlertDialog(
            onDismissRequest = {
                result = "Dismissed without changing state"
                activeDialog = null
            },
            icon = {
                Icon(
                    when (example) {
                        DialogExample.INFORMATION -> Icons.Default.Info
                        DialogExample.CONFIRMATION -> Icons.Default.Refresh
                        DialogExample.DESTRUCTIVE -> Icons.Default.Delete
                    },
                    contentDescription = null,
                )
            },
            title = {
                Text(
                    when (example) {
                        DialogExample.INFORMATION -> "Experimental API"
                        DialogExample.CONFIRMATION -> "Reset the preview?"
                        DialogExample.DESTRUCTIVE -> "Clear all bookmarks?"
                    },
                )
            },
            text = {
                Text(
                    when (example) {
                        DialogExample.INFORMATION -> "This reference uses an API from the pinned alpha Material artifact."
                        DialogExample.CONFIRMATION -> "This restores the component to its initial interactive state."
                        DialogExample.DESTRUCTIVE -> "Saved component IDs will be removed from this device. This cannot be undone."
                    },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        result = when (example) {
                            DialogExample.INFORMATION -> "Information acknowledged"
                            DialogExample.CONFIRMATION -> "Preview reset"
                            DialogExample.DESTRUCTIVE -> "Bookmarks cleared"
                        }
                        activeDialog = null
                    },
                ) {
                    Text(
                        when (example) {
                            DialogExample.INFORMATION -> "Got it"
                            DialogExample.CONFIRMATION -> "Reset"
                            DialogExample.DESTRUCTIVE -> "Clear bookmarks"
                        },
                    )
                }
            },
            dismissButton = if (example == DialogExample.INFORMATION) {
                null
            } else {
                {
                    TextButton(onClick = { activeDialog = null }) { Text("Cancel") }
                }
            },
        )
    }
}

@Composable
private fun DialogRule(label: String, description: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(label, fontWeight = FontWeight.Bold)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetsSample(modifier: Modifier = Modifier) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("No sheet action selected") }
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FeedbackSection(
            title = "Contextual sheet",
            description = "A modal bottom sheet preserves the current destination while offering a short task or related action set.",
        ) {
            Button(onClick = { showSheet = true }) {
                Icon(Icons.Default.Tune, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Configure reference")
            }
            Text(selectedAction, color = MaterialTheme.colorScheme.primary)
        }

        FeedbackSection(
            title = "Sheet behavior",
            description = "Drag, scrim, back, and a completion action all dismiss predictably and clean up transient state.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column {
                    ListItem(
                        leadingContent = { Icon(Icons.Default.FilterList, contentDescription = null) },
                        supportingContent = { Text("Short choices and contextual controls") },
                    ) { Text("Use a sheet") }
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ListItem(
                        leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                        supportingContent = { Text("Long forms, deep navigation, or permanent destinations") },
                    ) { Text("Use a full screen") }
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            Column(
                Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                    Text("Configure reference", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Choose what to inspect next", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                listOf(
                    Triple("Preview behavior", Icons.Default.Refresh, "Reset and replay interactive states"),
                    Triple("Copy source", Icons.Default.ContentCopy, "Copy the repository-relative source path"),
                    Triple("Share guidance", Icons.Default.Share, "Share the official name and usage rules"),
                ).forEach { (label, icon, support) ->
                    ListItem(
                        onClick = {
                            selectedAction = label
                            showSheet = false
                        },
                        leadingContent = { Icon(icon, contentDescription = null) },
                        trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
                        supportingContent = { Text(support) },
                    ) { Text(label) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SnackbarsSample(modifier: Modifier = Modifier) {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var outcome by remember { mutableStateOf("No transient message shown") }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FeedbackSection(
            title = "Transient feedback",
            description = "Snackbars confirm a completed or failed operation without blocking the current task.",
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val result = hostState.showSnackbar(
                                message = "Added to bookmarks",
                                actionLabel = "Undo",
                                withDismissAction = true,
                                duration = SnackbarDuration.Long,
                            )
                            outcome = if (result == SnackbarResult.ActionPerformed) "Bookmark undone" else "Bookmark saved"
                        }
                    },
                ) { Text("Show with action") }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            hostState.showSnackbar("Source path copied", withDismissAction = true)
                            outcome = "Copy feedback dismissed"
                        }
                    },
                ) { Text("Show confirmation") }
            }
            Card(Modifier.fillMaxWidth().heightIn(min = 88.dp)) {
                Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                    SnackbarHost(hostState)
                }
            }
            Text(outcome, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        FeedbackSection(
            title = "Queue and timing",
            description = "Messages appear one at a time, remain long enough to read, and provide an action only when it reverses or resolves the event.",
        ) {
            Text("Do not use a snackbar for critical consent, destructive confirmation, or information that must remain visible.")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TooltipsSample(modifier: Modifier = Modifier) {
    val plainState = rememberTooltipState()
    val richState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FeedbackSection(
            title = "Plain tooltip",
            description = "A plain tooltip names an unfamiliar icon action. The action still has its own accessible name.",
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = {
                        PlainTooltip(
                            Modifier.semantics {
                                liveRegion = LiveRegionMode.Assertive
                                paneTitle = "Bookmark component"
                            },
                        ) { Text("Bookmark component") }
                    },
                    state = plainState,
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Bookmark component")
                    }
                }
                OutlinedButton(onClick = { scope.launch { plainState.show() } }) { Text("Show tooltip") }
            }
        }

        FeedbackSection(
            title = "Rich tooltip",
            description = "A rich tooltip provides short supporting context and an optional action without becoming a hidden documentation page.",
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = {
                        RichTooltip(
                            title = { Text("Experimental API") },
                            action = {
                                TextButton(onClick = { scope.launch { richState.dismiss() } }) { Text("View API") }
                            },
                        ) { Text("This symbol requires an explicit opt-in annotation.") }
                    },
                    hasAction = true,
                    state = richState,
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = "Experimental API information")
                    }
                }
                OutlinedButton(onClick = { scope.launch { richState.show() } }) { Text("Show rich tooltip") }
            }
        }

        FeedbackSection(
            title = "Input and timing",
            description = "Tooltips support pointer hover, keyboard focus, touch-and-hold, and programmatic education when appropriate.",
        ) {
            Text("Never make a tooltip the only way to discover a required action or essential consequence.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenusSample(modifier: Modifier = Modifier) {
    var overflowExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }
    var densityMenuExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All references") }
    var selectedDensity by remember { mutableStateOf("Comfortable") }
    var result by remember { mutableStateOf("Choose a menu action") }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FeedbackSection(
            title = "Overflow commands",
            description = "A menu exposes secondary commands from a stable anchor and dismisses after a choice, outside tap, or back.",
        ) {
            Box {
                IconButton(onClick = { overflowExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More reference actions")
                }
                DropdownMenu(expanded = overflowExpanded, onDismissRequest = { overflowExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Duplicate reference") },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                        onClick = { result = "Reference duplicated"; overflowExpanded = false },
                    )
                    DropdownMenuItem(
                        text = { Text("Share guidance") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = { result = "Guidance shared"; overflowExpanded = false },
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Delete local draft") },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
                        onClick = { result = "Local draft deleted"; overflowExpanded = false },
                    )
                }
            }
            Text(result, color = MaterialTheme.colorScheme.primary)
        }

        FeedbackSection(
            title = "Current selection",
            description = "Selection menus display the active value outside the menu so state remains visible while the menu is closed.",
        ) {
            Box {
                OutlinedButton(onClick = { filterExpanded = true }) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(selectedFilter)
                }
                DropdownMenu(expanded = filterExpanded, onDismissRequest = { filterExpanded = false }) {
                    listOf("All references", "Stable APIs", "Experimental APIs", "Foundations").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            trailingIcon = if (selectedFilter == option) {
                                { Icon(Icons.Default.Check, contentDescription = "Selected") }
                            } else {
                                null
                            },
                            onClick = { selectedFilter = option; filterExpanded = false },
                        )
                    }
                }
            }
        }

        FeedbackSection(
            title = "Editable value entry",
            description = "Exposed dropdown menus combine a text field with a menu so known values stay one tap away while the label remains visible.",
        ) {
            ExposedDropdownMenuBox(
                expanded = densityMenuExpanded,
                onExpandedChange = { densityMenuExpanded = it },
            ) {
                OutlinedTextField(
                    value = selectedDensity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("List density") },
                    trailingIcon = { Icon(Icons.Default.ExpandMore, contentDescription = null) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded = densityMenuExpanded,
                    onDismissRequest = { densityMenuExpanded = false },
                ) {
                    listOf("Compact", "Comfortable", "Spacious").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedDensity = option
                                result = "Density set to $option"
                                densityMenuExpanded = false
                            },
                        )
                    }
                }
            }
        }

        FeedbackSection(
            title = "Menu scope",
            description = "Keep labels concise, group related commands, preserve a predictable order, and avoid deeply nested submenus on touch devices.",
        ) {
            Text("Use a dialog, sheet, or destination when choices require explanation, preview, or multi-step input.")
        }
    }
}

@Composable
private fun FeedbackSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        content()
    }
}
