package dev.qtremors.material.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Where accent colors come from. Wallpaper falls back to the preset below Android 12. */
enum class AccentSource { WALLPAPER, PRESET, CUSTOM }

/** The Material baseline seed: the app's own color, used whenever wallpaper colors are not. */
val BrandSeed = Color(0xFF6750A4)

/**
 * Named seeds. Each produces a complete tonal scheme in light, dark, and pure black through
 * Material Color Utilities, so no preset ships hand-picked colors with broken contrast.
 */
enum class ThemePreset(val seed: Color, val monochrome: Boolean = false) {
    BASELINE(BrandSeed),
    BLUE(Color(0xFF1B6EF3)),
    GREEN(Color(0xFF2E8B57)),
    AMBER(Color(0xFFE9A400)),
    CORAL(Color(0xFFEF5145)),
    GRAPHITE(Color(0xFF5F6368), monochrome = true),
}

/**
 * Everything the theme and app behavior read from settings. Defaults: system light and dark,
 * wallpaper colors, haptics on, full motion, catalog badges shown.
 */
@Immutable
data class ThemeState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    /** Pure black surfaces in dark mode, for OLED screens. */
    val pureBlack: Boolean = false,
    val accentSource: AccentSource = AccentSource.WALLPAPER,
    val preset: ThemePreset = ThemePreset.BASELINE,
    /** Seed for [AccentSource.CUSTOM], as `#RRGGBB`. */
    val customSeedHex: String = "#6750A4",
    val reducedMotion: Boolean = false,
    val hapticsEnabled: Boolean = true,
    val showBadges: Boolean = true,
)

/** Parses `#RRGGBB` or `#AARRGGBB`, or returns null. */
fun parseHexColor(hex: String): Color? {
    val digits = hex.trim().removePrefix("#")
    if (digits.length != 6 && digits.length != 8) return null
    val value = digits.toLongOrNull(16) ?: return null
    return if (digits.length == 6) Color(0xFF000000 or value) else Color(value)
}

/** Formats a color as `#RRGGBB`. */
fun Color.toHex(): String {
    val argb = (alpha * 255).toInt() shl 24 or ((red * 255).toInt() shl 16) or
        ((green * 255).toInt() shl 8) or (blue * 255).toInt()
    return "#%06X".format(argb and 0xFFFFFF)
}
