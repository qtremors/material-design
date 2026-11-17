package dev.qtremors.material.core.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FormatShapes
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The icon that stands for a catalog category or foundation everywhere in the app, so a category
 * looks the same in search, lists, and detail headers. Paired with [IconBadge] keyed by the same
 * name, it also gets the same tint everywhere.
 */
fun categoryIcon(category: String): ImageVector = when (category.lowercase().substringBefore(' ')) {
    "actions" -> Icons.Rounded.TouchApp
    "communication" -> Icons.Rounded.Notifications
    "containment" -> Icons.Rounded.Inbox
    "navigation" -> Icons.Rounded.Explore
    "selection" -> Icons.Rounded.Tune
    "layout" -> Icons.Rounded.Dashboard
    "color" -> Icons.Rounded.Palette
    "typography" -> Icons.Rounded.TextFields
    "shape" -> Icons.Rounded.FormatShapes
    "motion" -> Icons.Rounded.Animation
    "elevation" -> Icons.Rounded.Layers
    "accessibility" -> Icons.Rounded.Accessibility
    else -> Icons.Rounded.Category
}
