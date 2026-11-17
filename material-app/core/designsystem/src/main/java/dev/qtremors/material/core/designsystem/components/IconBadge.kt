package dev.qtremors.material.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/** A soft circle color and the saturated icon color that reads on it, in every theme. */
@Immutable
data class BadgeColors(val container: Color, val content: Color)

/**
 * Picks a stable pastel for a key (usually the row's id), so the same row always gets the same
 * color. The icon color is the pastel pushed to high saturation and lower value, which keeps
 * contrast on the pastel in light and dark themes alike.
 */
object BadgePalette {
    private val pastels = listOf(
        0xFFF8BBD0, 0xFFE1BEE7, 0xFFD1C4E9, 0xFFC5CAE9, 0xFFBBDEFB, 0xFFB3E5FC,
        0xFFB2EBF2, 0xFFB2DFDB, 0xFFC8E6C9, 0xFFDCEDC8, 0xFFF0F4C3, 0xFFFFF9C4,
        0xFFFFECB3, 0xFFFFE0B2, 0xFFFFCCBC, 0xFFD7CCC8, 0xFFCFD8DC
    ).map { Color(it) }

    /**
     * Light themes get the pastel circle with a deep icon; dark and pure black themes get a deep
     * tonal circle with the pastel as the icon, so badges do not glare on dark backgrounds.
     */
    fun forKey(key: String, dark: Boolean = false): BadgeColors =
        forColor(pastels[abs(key.hashCode()) % pastels.size], dark)

    /** Same idea for callers that want a specific pastel, such as status colors. */
    fun forColor(pastel: Color, dark: Boolean = false): BadgeColors =
        if (dark) BadgeColors(container = deep(pastel), content = pastel) else BadgeColors(pastel, rich(pastel))

    /** The pastel's hue, dark and muted enough to sit quietly on a dark row. */
    private fun deep(color: Color): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)
        hsv[1] = (hsv[1] * 2.2f).coerceIn(0.35f, 0.6f)
        hsv[2] = 0.3f
        return Color(android.graphics.Color.HSVToColor(hsv))
    }

    private fun rich(color: Color): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)
        hsv[1] = (hsv[1] * 2.5f).coerceIn(0.6f, 1f)
        hsv[2] = (hsv[2] * 0.65f).coerceIn(0.2f, 0.75f)
        return Color(android.graphics.Color.HSVToColor(hsv))
    }
}

/** The colored circle with an icon that leads every navigational row. */
@Composable
fun IconBadge(
    icon: ImageVector,
    key: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 22.dp,
    colors: BadgeColors? = null
) {
    val dark = MaterialTheme.colorScheme.surface.luminance() < DARK_SURFACE
    val resolved = colors ?: remember(key, dark) { BadgePalette.forKey(key, dark) }
    Box(
        modifier = modifier
            .size(size)
            .background(resolved.container, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = resolved.content,
            modifier = Modifier.size(iconSize)
        )
    }
}

private const val DARK_SURFACE = 0.5f
