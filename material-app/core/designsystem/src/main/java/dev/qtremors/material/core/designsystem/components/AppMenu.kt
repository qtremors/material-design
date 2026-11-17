package dev.qtremors.material.core.designsystem.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SelectableDropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One row of an [AppMenu]. [destructive] actions (remove, reset) are drawn in the error color.
 * Give every row of a pick-one group a [selected] value; the chosen row is highlighted and checked.
 */
data class MenuAction(
    val label: String,
    val icon: ImageVector? = null,
    val supporting: String? = null,
    val destructive: Boolean = false,
    val enabled: Boolean = true,
    val selected: Boolean? = null,
    val onClick: () -> Unit
)

/**
 * The app's overflow and long-press menu, built on the Material 3 Expressive grouped menu: each
 * group is its own rounded card, items inside a group are joined, and an item's shape morphs when
 * pressed. Pass related actions together in one group and separate kinds of action (open, change,
 * remove) into their own groups. Empty groups are dropped, and the menu closes before an action runs.
 * Anchor it the same way as any dropdown: place it inside the Box that holds its button.
 */
@Composable
fun AppMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    groups: List<List<MenuAction>>,
    modifier: Modifier = Modifier
) {
    val haptics = rememberAppHaptics()
    val shown = groups.filter { it.isNotEmpty() }
    DropdownMenuPopup(expanded = expanded, onDismissRequest = onDismissRequest, modifier = modifier) {
        shown.forEachIndexed { groupIndex, group ->
            // Vibrant: the Expressive high-emphasis menu color, so the menu stands clear of the
            // rows behind it in dark themes, where the standard color reads as a hole in the page.
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(groupIndex, shown.size),
                containerColor = MenuDefaults.groupVibrantContainerColor
            ) {
                group.forEachIndexed { index, action ->
                    val onClick = {
                        haptics.tap()
                        onDismissRequest()
                        action.onClick()
                    }
                    val shapes = MenuDefaults.itemShape(index, group.size)
                    val leading: (@Composable () -> Unit)? = action.icon?.let { icon ->
                        { Icon(icon, contentDescription = null, modifier = Modifier.size(MenuDefaults.LeadingIconSize)) }
                    }
                    val supporting: (@Composable () -> Unit)? = action.supporting?.let { text -> { Text(text) } }
                    if (action.selected != null) {
                        SelectableDropdownMenuItem(
                            selected = action.selected,
                            onClick = onClick,
                            text = { Text(action.label) },
                            shapes = shapes,
                            leadingIcon = leading,
                            selectedLeadingIcon = {
                                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(MenuDefaults.LeadingIconSize))
                            },
                            supportingText = supporting,
                            enabled = action.enabled,
                            colors = MenuDefaults.selectableItemVibrantColors()
                        )
                    } else {
                        val error = MaterialTheme.colorScheme.error
                        DropdownMenuItem(
                            onClick = onClick,
                            text = { Text(action.label) },
                            shape = shapes.shape,
                            leadingIcon = leading,
                            supportingText = supporting,
                            enabled = action.enabled,
                            colors = if (action.destructive) {
                                MenuDefaults.itemVibrantColors(textColor = error, leadingIconColor = error)
                            } else {
                                MenuDefaults.itemVibrantColors()
                            }
                        )
                    }
                }
            }
            if (groupIndex != shown.lastIndex) Spacer(Modifier.height(MenuDefaults.GroupSpacing))
        }
    }
}
