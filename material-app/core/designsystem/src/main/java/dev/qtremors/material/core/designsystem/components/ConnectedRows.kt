package dev.qtremors.material.core.designsystem.components

import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.GroupCorner
import dev.qtremors.material.core.designsystem.RowGap
import dev.qtremors.material.core.designsystem.connectedRowShape
import dev.qtremors.material.core.designsystem.spacing

/**
 * A group of rows that touch with hairline gaps, rounded only on the outside. Rows inside should
 * use [ConnectedRow] or any composable filling the width; the group clips them to its outline.
 */
@Composable
fun ConnectedGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(GroupCorner)),
        verticalArrangement = Arrangement.spacedBy(RowGap),
        content = content
    )
}

/**
 * One row of a connected group built item by item in a lazy list, where a [ConnectedGroup]
 * cannot wrap the rows. [index] and [count] pick the shape: the first item is rounded on top,
 * the last on the bottom, and the ones between meet with tight corners and a hairline gap.
 */
@Composable
fun ConnectedItem(
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (index < count - 1) RowGap else 0.dp)
            .clip(connectedRowShape(index, count)),
        content = content
    )
}

/** Title above a [ConnectedGroup], inset to line up with the row text. */
@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable RowScope.() -> Unit)? = null
) {
    val spacing = MaterialTheme.spacing
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = spacing.sectionHeaderInset,
                end = spacing.small,
                top = spacing.sectionGap,
                bottom = spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        trailing?.invoke(this)
    }
}

/**
 * One row of a connected group: optional leading content, a title with optional supporting text,
 * and optional trailing content. The whole row is the touch target.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectedRow(
    title: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    role: Role? = if (onClick != null) Role.Button else null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
    enabled: Boolean = true,
    /** False when [onClick] plays its own haptic, so a tap never buzzes twice. */
    tapHaptic: Boolean = true,
    /** Replaces [supporting] when the row needs more than a line of text under its title. */
    supportingContent: (@Composable () -> Unit)? = null
) {
    val spacing = MaterialTheme.spacing
    val haptics = rememberAppHaptics()
    val clickable = if (onClick != null || onLongClick != null) {
        Modifier.combinedClickable(
            enabled = enabled,
            role = role,
            onClick = {
                if (tapHaptic) haptics.tap()
                onClick?.invoke()
            },
            onLongClick = onLongClick?.let { handler ->
                {
                    haptics.longPress()
                    handler()
                }
            }
        )
    } else Modifier

    Surface(
        color = containerColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .then(clickable)
                .alpha(if (enabled) 1f else DISABLED_ALPHA)
                .heightIn(min = 64.dp)
                .padding(horizontal = spacing.rowHorizontal, vertical = spacing.rowVertical),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(spacing.medium))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (supportingContent != null) {
                    Spacer(Modifier.height(2.dp))
                    supportingContent()
                } else if (supporting != null) {
                    Text(
                        text = supporting,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (trailing != null) {
                Spacer(Modifier.width(spacing.space12))
                trailing()
            }
        }
    }
}

/** Disabled rows fade as one piece, so the icon, title, and explanation never disagree. */
private const val DISABLED_ALPHA = 0.5f

/** A row that opens another page: pastel icon on the left, chevron on the right. */
@Composable
fun NavigationRow(
    title: String,
    icon: ImageVector,
    badgeKey: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    onLongClick: (() -> Unit)? = null,
    trailingText: String? = null
) {
    ConnectedRow(
        title = title,
        supporting = supporting,
        modifier = modifier,
        leading = { IconBadge(icon = icon, key = badgeKey) },
        trailing = {
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        onClick = onClick,
        onLongClick = onLongClick
    )
}

/** A row whose whole surface toggles a setting, with an expressive switch on the right. */
@Composable
fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    icon: ImageVector? = null,
    badgeKey: String = title,
    enabled: Boolean = true
) {
    val haptics = rememberAppHaptics()
    ConnectedRow(
        title = title,
        supporting = supporting,
        modifier = modifier,
        leading = icon?.let { { IconBadge(icon = it, key = badgeKey) } },
        trailing = { ExpressiveSwitch(checked = checked, onCheckedChange = null, enabled = enabled) },
        onClick = {
            haptics.toggle(!checked)
            onCheckedChange(!checked)
        },
        role = Role.Switch,
        enabled = enabled,
        tapHaptic = false
    )
}
