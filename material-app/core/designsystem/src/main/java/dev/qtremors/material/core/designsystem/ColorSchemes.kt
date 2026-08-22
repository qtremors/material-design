package dev.qtremors.material.core.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

val OledSurfaceVariant = Color(0xFF121212)
val OledContainerLow = Color(0xFF0F0D13)
val OledContainerLowest = Color.Black

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF141318),
    surface = Color(0xFF141318),
    surfaceVariant = Color(0xFF49454F),
    surfaceContainerLowest = Color(0xFF0F0D13),
    surfaceContainerLow = Color(0xFF1D1B20),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    surfaceContainerHighest = Color(0xFF36343B),
    onSurface = Color(0xFFE6E0E9),
    onBackground = Color(0xFFE6E0E9),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFEF7FF),
    surface = Color(0xFFFEF7FF),
    surfaceContainerHighest = Color(0xFFE2E2E6),
    onSurface = Color(0xFF1B1B1F),
    onBackground = Color(0xFF1B1B1F),
    onSurfaceVariant = Color(0xFF44474E),
    outline = Color(0xFF74777F),
    outlineVariant = Color(0xFFC4C6D0),
)

val OledColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8),
    background = Color.Black,
    surface = Color.Black,
    surfaceVariant = Color(0xFF121212),
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF0A0A0A),
    surfaceContainer = Color(0xFF111111),
    surfaceContainerHigh = Color(0xFF1A1A1A),
    surfaceContainerHighest = Color(0xFF222222),
    onBackground = Color.White,
    onSurface = Color.White,
)

val DraculaDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBD93F9),
    onPrimary = Color(0xFF282A36),
    primaryContainer = Color(0xFF44475A),
    onPrimaryContainer = Color(0xFFF8F8F2),
    secondary = Color(0xFFFF79C6),
    onSecondary = Color(0xFF282A36),
    secondaryContainer = Color(0xFF44475A),
    onSecondaryContainer = Color(0xFFF8F8F2),
    tertiary = Color(0xFF8BE9FD),
    onTertiary = Color(0xFF282A36),
    tertiaryContainer = Color(0xFF44475A),
    onTertiaryContainer = Color(0xFFF8F8F2),
    background = Color(0xFF282A36),
    onBackground = Color(0xFFF8F8F2),
    surface = Color(0xFF282A36),
    onSurface = Color(0xFFF8F8F2),
    surfaceVariant = Color(0xFF343746),
    onSurfaceVariant = Color(0xFFF8F8F2),
    surfaceContainerLowest = Color(0xFF1E1F29),
    surfaceContainerLow = Color(0xFF21222C),
    surfaceContainer = Color(0xFF282A36),
    surfaceContainerHigh = Color(0xFF343746),
    surfaceContainerHighest = Color(0xFF44475A),
    outline = Color(0xFF6272A4),
    outlineVariant = Color(0xFF44475A),
    error = Color(0xFFFF5555),
    onError = Color(0xFF282A36),
)

val DraculaOledColorScheme = DraculaDarkColorScheme.copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceVariant = Color(0xFF121212),
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF0F0D13),
    surfaceContainer = Color(0xFF111111),
    surfaceContainerHigh = Color(0xFF1A1A1A),
    surfaceContainerHighest = Color(0xFF222222),
)

val DraculaLightColorScheme = lightColorScheme(
    primary = Color(0xFF6272A4),
    onPrimary = Color(0xFFF8F8F2),
    primaryContainer = Color(0xFFE2E2DC),
    onPrimaryContainer = Color(0xFF282A36),
    secondary = Color(0xFFFF5555),
    onSecondary = Color(0xFFF8F8F2),
    secondaryContainer = Color(0xFFE2E2DC),
    onSecondaryContainer = Color(0xFF282A36),
    tertiary = Color(0xFF50FA7B),
    onTertiary = Color(0xFF282A36),
    background = Color(0xFFF8F8F2),
    onBackground = Color(0xFF282A36),
    surface = Color(0xFFF8F8F2),
    onSurface = Color(0xFF282A36),
    surfaceVariant = Color(0xFFE2E2DC),
    onSurfaceVariant = Color(0xFF282A36),
    outline = Color(0xFF6272A4),
    outlineVariant = Color(0xFFE2E2DC),
    error = Color(0xFFFF5555),
    onError = Color(0xFFF8F8F2),
)

val TokyoNightDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7AA2F7),
    onPrimary = Color(0xFF1A1B26),
    primaryContainer = Color(0xFF24283B),
    onPrimaryContainer = Color(0xFFC0CAF5),
    secondary = Color(0xFFBB9AF3),
    onSecondary = Color(0xFF1A1B26),
    secondaryContainer = Color(0xFF24283B),
    onSecondaryContainer = Color(0xFFC0CAF5),
    tertiary = Color(0xFF7DCFFF),
    onTertiary = Color(0xFF1A1B26),
    tertiaryContainer = Color(0xFF24283B),
    onTertiaryContainer = Color(0xFFC0CAF5),
    background = Color(0xFF1A1B26),
    onBackground = Color(0xFFC0CAF5),
    surface = Color(0xFF1A1B26),
    onSurface = Color(0xFFC0CAF5),
    surfaceVariant = Color(0xFF24283B),
    onSurfaceVariant = Color(0xFFC0CAF5),
    surfaceContainerLowest = Color(0xFF16161E),
    surfaceContainerLow = Color(0xFF1F202E),
    surfaceContainer = Color(0xFF24283B),
    surfaceContainerHigh = Color(0xFF2F3549),
    surfaceContainerHighest = Color(0xFF383E56),
    outline = Color(0xFF565F89),
    outlineVariant = Color(0xFF24283B),
    error = Color(0xFFF7768E),
    onError = Color(0xFF1A1B26),
)

val TokyoNightOledColorScheme = TokyoNightDarkColorScheme.copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceVariant = Color(0xFF121212),
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF0F0D13),
    surfaceContainer = Color(0xFF111111),
    surfaceContainerHigh = Color(0xFF1A1A1A),
    surfaceContainerHighest = Color(0xFF222222),
)

val TokyoNightLightColorScheme = lightColorScheme(
    primary = Color(0xFF385898),
    onPrimary = Color(0xFFE1E2E7),
    primaryContainer = Color(0xFFCFD0D7),
    onPrimaryContainer = Color(0xFF3760BF),
    secondary = Color(0xFF9854F1),
    onSecondary = Color(0xFFE1E2E7),
    secondaryContainer = Color(0xFFCFD0D7),
    onSecondaryContainer = Color(0xFF3760BF),
    tertiary = Color(0xFF00668E),
    onTertiary = Color(0xFFE1E2E7),
    background = Color(0xFFE1E2E7),
    onBackground = Color(0xFF3760BF),
    surface = Color(0xFFE1E2E7),
    onSurface = Color(0xFF3760BF),
    surfaceVariant = Color(0xFFCFD0D7),
    onSurfaceVariant = Color(0xFF3760BF),
    outline = Color(0xFF8F93A2),
    outlineVariant = Color(0xFFCFD0D7),
    error = Color(0xFF8C4351),
    onError = Color(0xFFE1E2E7),
)

fun buildScheme(primary: Color, isDark: Boolean): ColorScheme {
    val hsl = FloatArray(3)
    val argb = primary.toArgb()
    android.graphics.Color.colorToHSV(argb, hsl)
    val hue = hsl[0]
    val sat = hsl[1]

    val primaryTonal = if (isDark) {
        fromHsv(hue, (sat * 0.7f).coerceIn(0f, 1f), 0.88f)
    } else {
        fromHsv(hue, sat.coerceIn(0f, 1f), 0.60f)
    }

    val primaryContainerTonal = if (isDark) {
        fromHsv(hue, (sat * 0.8f).coerceIn(0f, 1f), 0.30f)
    } else {
        fromHsv(hue, (sat * 0.3f).coerceIn(0f, 1f), 0.90f)
    }

    val onPrimaryContainerTonal = if (isDark) {
        fromHsv(hue, (sat * 0.3f).coerceIn(0f, 1f), 0.92f)
    } else {
        fromHsv(hue, sat.coerceIn(0f, 1f), 0.20f)
    }

    val secondaryTonal = fromHsv((hue + 15f) % 360f, (sat * 0.4f).coerceIn(0f, 1f), if (isDark) 0.80f else 0.45f)
    val secondaryContainerTonal = if (isDark) {
        fromHsv((hue + 15f) % 360f, (sat * 0.4f).coerceIn(0f, 1f), 0.25f)
    } else {
        fromHsv((hue + 15f) % 360f, (sat * 0.2f).coerceIn(0f, 1f), 0.92f)
    }

    return if (isDark) {
        darkColorScheme(
            primary = primaryTonal,
            onPrimary = getContrastColor(primaryTonal),
            primaryContainer = primaryContainerTonal,
            onPrimaryContainer = onPrimaryContainerTonal,
            secondary = secondaryTonal,
            onSecondary = getContrastColor(secondaryTonal),
            secondaryContainer = secondaryContainerTonal,
            onSecondaryContainer = if (isDark) Color(0xFFE8DEF8) else Color(0xFF1D192B),
            background = Color(0xFF141318),
            onBackground = Color(0xFFE6E0E9),
            surface = Color(0xFF141318),
            onSurface = Color(0xFFE6E0E9),
            surfaceVariant = Color(0xFF49454F),
            onSurfaceVariant = Color(0xFFCAC4D0),
            surfaceContainerLowest = Color(0xFF0F0D13),
            surfaceContainerLow = Color(0xFF1D1B20),
            surfaceContainer = Color(0xFF211F26),
            surfaceContainerHigh = Color(0xFF2B2930),
            surfaceContainerHighest = Color(0xFF36343B),
            outline = Color(0xFF938F99),
            outlineVariant = Color(0xFF49454F),
        )
    } else {
        lightColorScheme(
            primary = primaryTonal,
            onPrimary = getContrastColor(primaryTonal),
            primaryContainer = primaryContainerTonal,
            onPrimaryContainer = onPrimaryContainerTonal,
            secondary = secondaryTonal,
            onSecondary = getContrastColor(secondaryTonal),
            secondaryContainer = secondaryContainerTonal,
            onSecondaryContainer = Color(0xFF1D192B),
            background = Color(0xFFFEF7FF),
            onBackground = Color(0xFF1B1B1F),
            surface = Color(0xFFFEF7FF),
            onSurface = Color(0xFF1B1B1F),
            surfaceVariant = Color(0xFFE7E0EC),
            onSurfaceVariant = Color(0xFF49454E),
            surfaceContainerLowest = Color.White,
            surfaceContainerLow = Color(0xFFF7F2FA),
            surfaceContainer = Color(0xFFF3EDF7),
            surfaceContainerHigh = Color(0xFFECE6F0),
            surfaceContainerHighest = Color(0xFFE6E0E9),
            outline = Color(0xFF79747E),
            outlineVariant = Color(0xFFCAC4D0),
        )
    }
}

private fun fromHsv(h: Float, s: Float, v: Float): Color {
    val rgb = android.graphics.Color.HSVToColor(floatArrayOf(h, s, v))
    return Color(rgb)
}

fun buildMonochromeScheme(isDark: Boolean, isOled: Boolean = false): ColorScheme {
    val background = when {
        isOled -> Color.Black
        isDark -> Color(0xFF121212)
        else -> Color(0xFFFAFAFA)
    }
    val surface = background
    return if (isDark || isOled) {
        darkColorScheme(
            primary = Color(0xFFE0E0E0),
            onPrimary = Color(0xFF1A1A1A),
            primaryContainer = Color(0xFF3A3A3A),
            onPrimaryContainer = Color(0xFFEDEDED),
            secondary = Color(0xFFCFCFCF),
            onSecondary = Color(0xFF1F1F1F),
            secondaryContainer = Color(0xFF333333),
            onSecondaryContainer = Color(0xFFE6E6E6),
            tertiary = Color(0xFFBDBDBD),
            onTertiary = Color(0xFF1F1F1F),
            tertiaryContainer = Color(0xFF2F2F2F),
            onTertiaryContainer = Color(0xFFE0E0E0),
            background = background,
            onBackground = Color(0xFFEDEDED),
            surface = surface,
            onSurface = Color(0xFFEDEDED),
            surfaceVariant = if (isOled) OledSurfaceVariant else Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFC7C7C7),
            surfaceContainerLowest = if (isOled) Color.Black else Color(0xFF0D0D0D),
            surfaceContainerLow = if (isOled) Color(0xFF0A0A0A) else Color(0xFF171717),
            surfaceContainer = Color(0xFF1F1F1F),
            surfaceContainerHigh = Color(0xFF2A2A2A),
            surfaceContainerHighest = Color(0xFF353535),
            outline = Color(0xFF8A8A8A),
            outlineVariant = Color(0xFF454545),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF424242),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE0E0E0),
            onPrimaryContainer = Color(0xFF1B1B1B),
            secondary = Color(0xFF616161),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE8E8E8),
            onSecondaryContainer = Color(0xFF202020),
            tertiary = Color(0xFF757575),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFEDEDED),
            onTertiaryContainer = Color(0xFF242424),
            background = background,
            onBackground = Color(0xFF1B1B1F),
            surface = surface,
            onSurface = Color(0xFF1B1B1F),
            surfaceVariant = Color(0xFFE3E3E3),
            onSurfaceVariant = Color(0xFF464646),
            surfaceContainerLowest = Color.White,
            surfaceContainerLow = Color(0xFFF5F5F5),
            surfaceContainer = Color(0xFFEFEFEF),
            surfaceContainerHigh = Color(0xFFE8E8E8),
            surfaceContainerHighest = Color(0xFFE0E0E0),
            outline = Color(0xFF777777),
            outlineVariant = Color(0xFFC7C7C7),
            error = Color(0xFFBA1A1A),
            onError = Color.White,
        )
    }
}

fun parseColor(hex: String, fallback: Color): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        fallback
    }
}

fun getContrastColor(backgroundColor: Color): Color {
    val luminance = backgroundColor.red * 0.299f + backgroundColor.green * 0.587f + backgroundColor.blue * 0.114f
    return if (luminance > 0.5f) Color.Black else Color.White
}
