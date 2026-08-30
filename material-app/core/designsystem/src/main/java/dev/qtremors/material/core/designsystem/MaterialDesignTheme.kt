package dev.qtremors.material.core.designsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalReducedMotion = staticCompositionLocalOf { false }

@Composable
fun MaterialDesignTheme(
    themeState: ThemeState = ThemeState(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val effectivelyDark = when (themeState.themeMode) {
        ThemeMode.SYSTEM -> darkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.OLED -> true
    }

    val colorScheme = resolveThemeColorScheme(
        themeState = themeState,
        effectivelyDark = effectivelyDark,
        context = context
    )

    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !effectivelyDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !effectivelyDark
        }
    }

    CompositionLocalProvider(LocalReducedMotion provides themeState.reducedMotion) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            shapes = ExpressiveShapes,
            motionScheme = ExpressiveMotion.motionScheme(themeState.reducedMotion),
            content = content,
        )
    }
}

fun resolveThemeColorScheme(
    themeState: ThemeState,
    effectivelyDark: Boolean,
    context: android.content.Context,
): ColorScheme {
    return when (themeState.themePreset) {
        ThemePreset.DRACULA -> {
            if (effectivelyDark) {
                if (themeState.themeMode == ThemeMode.OLED) DraculaOledColorScheme else DraculaDarkColorScheme
            } else {
                DraculaLightColorScheme
            }
        }
        ThemePreset.TOKYO_NIGHT -> {
            if (effectivelyDark) {
                if (themeState.themeMode == ThemeMode.OLED) TokyoNightOledColorScheme else TokyoNightDarkColorScheme
            } else {
                TokyoNightLightColorScheme
            }
        }
        ThemePreset.CUSTOM -> {
            val primaryColor = parseColor(themeState.customPrimaryColorHex, Color(0xFFBD93F9))
            val rawBg = parseColor(themeState.customBackgroundColorHex, Color(0xFF282A36))
            val bg = if (themeState.themeMode == ThemeMode.OLED) Color.Black else rawBg
            val fg = getContrastColor(bg)
            val scheme = buildScheme(primaryColor, effectivelyDark)
            val surfaceVar = if (fg == Color.White) {
                Color.White.copy(alpha = 0.08f).compositeOver(bg)
            } else {
                Color.Black.copy(alpha = 0.08f).compositeOver(bg)
            }
            scheme.copy(
                primary = primaryColor,
                background = bg,
                surface = bg,
                onBackground = fg,
                onSurface = fg,
                surfaceVariant = surfaceVar,
                onSurfaceVariant = fg
            )
        }
        ThemePreset.NONE -> when {
            themeState.accentColor == AccentColor.MONOCHROME -> {
                buildMonochromeScheme(
                    isDark = effectivelyDark,
                    isOled = themeState.themeMode == ThemeMode.OLED
                )
            }
            themeState.accentColor == AccentColor.DYNAMIC && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (effectivelyDark) {
                    if (themeState.themeMode == ThemeMode.OLED) {
                        dynamicDarkColorScheme(context).copy(
                            background = Color.Black,
                            surface = Color.Black,
                            surfaceVariant = OledSurfaceVariant
                        )
                    } else {
                        dynamicDarkColorScheme(context)
                    }
                } else {
                    dynamicLightColorScheme(context)
                }
            }
            themeState.accentColor != AccentColor.DYNAMIC -> {
                val primaryColor = themeState.accentColor.color ?: AccentBlue
                val scheme = buildScheme(primaryColor, effectivelyDark)
                if (themeState.themeMode == ThemeMode.OLED) {
                    scheme.copy(
                        background = Color.Black,
                        surface = Color.Black,
                        surfaceVariant = OledSurfaceVariant,
                        surfaceContainerLowest = OledContainerLowest,
                        surfaceContainerLow = OledContainerLow
                    )
                } else {
                    scheme
                }
            }
            themeState.themeMode == ThemeMode.OLED -> OledColorScheme
            effectivelyDark -> DarkColorScheme
            else -> LightColorScheme
        }
    }
}
