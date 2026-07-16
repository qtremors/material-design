package dev.qtremors.material.samples.containment

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.standardSpring

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardsSample(modifier: Modifier = Modifier) {
    var bookmarked by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableIntStateOf(0) }
    val selectionLabels = listOf("Quick scan", "Deep study", "Implementation")
    val selectionIcons = listOf(Icons.Default.Schedule, Icons.Default.Info, Icons.Default.Code)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ContainmentReferenceSection(
            title = "Hierarchy variants",
            description = "Filled, elevated, and outlined cards separate content at different levels without making every surface equally loud.",
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Card(Modifier.weight(1f).height(124.dp)) {
                    CardLabel(Icons.Default.Palette, "Filled", "Related content")
                }
                ElevatedCard(Modifier.weight(1f).height(124.dp)) {
                    CardLabel(Icons.Default.Api, "Elevated", "Lifted priority")
                }
                OutlinedCard(Modifier.weight(1f).height(124.dp)) {
                    CardLabel(Icons.Default.Code, "Outlined", "Clear boundary")
                }
            }
        }

        ContainmentReferenceSection(
            title = "Responsive feature card",
            description = "A card adapts its internal composition while keeping one clear action and one content hierarchy.",
        ) {
            ElevatedCard(
                onClick = { bookmarked = !bookmarked },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
                BoxWithConstraints {
                    val horizontal = maxWidth >= 420.dp
                    if (horizontal) {
                        Row(Modifier.fillMaxWidth().height(210.dp)) {
                            FeatureArtwork(Modifier.fillMaxHeight().weight(0.8f))
                            FeatureContent(
                                bookmarked = bookmarked,
                                modifier = Modifier.fillMaxHeight().weight(1.2f),
                            )
                        }
                    } else {
                        Column {
                            FeatureArtwork(Modifier.fillMaxWidth().height(150.dp))
                            FeatureContent(bookmarked = bookmarked)
                        }
                    }
                }
            }
        }

        ContainmentReferenceSection(
            title = "Selectable cards",
            description = "Selectable cards expose radio semantics when the cards represent one mutually exclusive choice.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                selectionLabels.forEachIndexed { index, label ->
                    SelectableReferenceCard(
                        title = label,
                        description = when (index) {
                            0 -> "Overview, purpose, and common variants"
                            1 -> "Behavior, states, accessibility, and adaptation"
                            else -> "APIs, source locations, and implementation notes"
                        },
                        icon = selectionIcons[index],
                        selected = selectedCard == index,
                        onClick = { selectedCard = index },
                    )
                }
            }
        }

        ContainmentReferenceSection(
            title = "Unavailable content",
            description = "Preserve enough structure to explain why content is unavailable; do not disguise a disabled card as actionable.",
        ) {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth().alpha(0.7f).semantics {
                    contentDescription = "Motion comparison unavailable while reduced motion is enabled"
                },
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.BrokenImage, contentDescription = null)
                    Column(Modifier.weight(1f)) {
                        Text("Motion comparison unavailable", fontWeight = FontWeight.Bold)
                        Text(
                            "Turn off reduced motion to play this decorative comparison.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardLabel(icon: ImageVector, title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxHeight().padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(icon, contentDescription = null)
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun FeatureArtwork(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(88.dp).background(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.shapes.extraLarge,
            ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.AccessibilityNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.size(42.dp),
            )
        }
    }
}

@Composable
private fun FeatureContent(bookmarked: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Reference journey",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Icon(
                if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = if (bookmarked) "Bookmarked" else "Not bookmarked",
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Design the complete state change",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Start with purpose, then inspect behavior, motion, accessibility, and source.",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (bookmarked) "Saved for later" else "Tap to bookmark",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
            )
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun SelectableReferenceCard(
    title: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = standardSpring(),
        label = "$title card color",
    )
    Card(
        modifier = Modifier.fillMaxWidth().selectable(
            selected = selected,
            onClick = onClick,
            role = Role.RadioButton,
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(
                    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
                    MaterialTheme.shapes.large,
                ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Default.Check else icon,
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RadioButton(selected = selected, onClick = null)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListsSample(modifier: Modifier = Modifier) {
    var selectedReference by remember { mutableIntStateOf(0) }
    var notifications by remember { mutableStateOf(true) }
    var bookmarked by remember { mutableStateOf(false) }
    val groupedLabels = listOf("Preview", "Guidance", "API and source")
    val groupedIcons = listOf(Icons.Default.Palette, Icons.Default.Info, Icons.Default.Api)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ContainmentReferenceSection(
            title = "Content density",
            description = "One-, two-, and three-line items share alignment rules while supporting progressively richer context.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column {
                    ListItem(
                        leadingContent = { Icon(Icons.Default.Folder, contentDescription = null) },
                        trailingContent = { Text("12") },
                    ) { Text("One-line item") }
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ListItem(
                        supportingContent = { Text("Supporting text explains the destination") },
                        leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                        trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
                    ) { Text("Two-line item") }
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ListItem(
                        supportingContent = {
                            Text(
                                "Longer supporting content wraps naturally while the leading and trailing elements retain their alignment.",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        overlineContent = { Text("FOUNDATION") },
                        leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                        trailingContent = { IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, "More options") } },
                    ) { Text("Three-line item") }
                }
            }
        }

        ContainmentReferenceSection(
            title = "Interactive rows",
            description = "The row owns navigation. A trailing control owns its immediate setting and has a distinct label.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column {
                    ListItem(
                        onClick = { bookmarked = !bookmarked },
                        supportingContent = { Text(if (bookmarked) "Saved to your local library" else "Not saved") },
                        leadingContent = {
                            Icon(
                                if (bookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { bookmarked = !bookmarked }) {
                                Icon(
                                    if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = if (bookmarked) "Remove bookmark" else "Add bookmark",
                                )
                            }
                        },
                    ) { Text("Bookmarks") }
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ListItem(
                        checked = notifications,
                        onCheckedChange = { notifications = it },
                        supportingContent = { Text("Notify when reviewed API metadata changes") },
                        leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                        trailingContent = {
                            Switch(checked = notifications, onCheckedChange = null)
                        },
                    ) { Text("Reference notifications") }
                }
            }
        }

        ContainmentReferenceSection(
            title = "Segmented group",
            description = "Position-aware shapes bind closely related rows while preserving an individual selected state.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                groupedLabels.forEachIndexed { index, label ->
                    val selected = selectedReference == index
                    SegmentedListItem(
                        selected = selected,
                        onClick = { selectedReference = index },
                        shapes = ListItemDefaults.segmentedShapes(index, groupedLabels.size),
                        leadingContent = {
                            Icon(groupedIcons[index], contentDescription = null)
                        },
                        trailingContent = {
                            if (selected) Icon(Icons.Default.Check, contentDescription = "Selected")
                        },
                        supportingContent = {
                            Text(
                                when (index) {
                                    0 -> "Interact with working states"
                                    1 -> "Understand purpose and behavior"
                                    else -> "Verify symbols and implementation locations"
                                },
                            )
                        },
                        content = { Text(label) },
                    )
                }
            }
        }

        ContainmentReferenceSection(
            title = "Disabled row",
            description = "Disabled styling applies to the whole row, while supporting text explains the constraint.",
        ) {
            ListItem(
                enabled = false,
                supportingContent = { Text("Open on a window at least 600 dp wide") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
            ) { Text("Large-screen comparison") }
        }
    }
}

@Composable
private fun ContainmentReferenceSection(
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
