package dev.qtremors.material.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalShowBadges
import dev.qtremors.material.core.designsystem.spacing

/** How strongly a [Tag] should stand out. Meaning is always in the label, never the color alone. */
enum class TagTone { NEUTRAL, PRIMARY, TERTIARY, ERROR }

@Immutable
data class Tag(val label: String, val tone: TagTone = TagTone.NEUTRAL)

/** A small rounded label for maturity and classification, such as "Experimental". */
@Composable
fun TagLabel(tag: Tag, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val (container, content) = when (tag.tone) {
        TagTone.NEUTRAL -> colors.surfaceContainerHighest to colors.onSurfaceVariant
        TagTone.PRIMARY -> colors.primaryContainer to colors.onPrimaryContainer
        TagTone.TERTIARY -> colors.tertiaryContainer to colors.onTertiaryContainer
        TagTone.ERROR -> colors.errorContainer to colors.onErrorContainer
    }
    Surface(color = container, contentColor = content, shape = MaterialTheme.shapes.small, modifier = modifier) {
        Text(
            tag.label,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

/** A wrapping row of [TagLabel]s; hidden when the person turned maturity badges off. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagRow(tags: List<Tag>, modifier: Modifier = Modifier, force: Boolean = false) {
    if (tags.isEmpty() || (!force && !LocalShowBadges.current)) return
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        tags.forEach { TagLabel(it) }
    }
}

/**
 * A catalog entry as a connected row: the category badge, the official name, one or two lines of
 * summary, and its tags. Long-press opens the entry's menu.
 */
@Composable
fun EntryRow(
    title: String,
    category: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    tags: List<Tag> = emptyList(),
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    /** Shares the badge with the entry's page, so it travels there on open. See [entryBadgeKey]. */
    sharedKey: String? = null,
) {
    ConnectedRow(
        title = title,
        modifier = modifier,
        leading = { IconBadge(categoryIcon(category), key = category, modifier = Modifier.sharedEntryBounds(sharedKey)) },
        trailing = trailing,
        onClick = onClick,
        onLongClick = onLongClick,
        containerColor = containerColor,
        supportingContent = if (supporting == null && tags.isEmpty()) null else {
            {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                    if (supporting != null) {
                        Text(
                            supporting,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    TagRow(tags)
                }
            }
        },
    )
}
