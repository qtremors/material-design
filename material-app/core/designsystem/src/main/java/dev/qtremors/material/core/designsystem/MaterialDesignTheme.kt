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
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** True when decorative motion should snap instead of animate. */
val LocalReducedMotion = staticCompositionLocalOf { false }

/** False when the person turned haptics off. Read through rememberAppHaptics(). */
val LocalHapticsEnabled = staticCompositionLocalOf { true }

val LocalSpacing = staticCompositionLocalOf { Spacing() }

/** Whether catalog maturity badges are shown, from Settings. */
val LocalShowBadges = staticCompositionLocalOf { true }

@Composable
fun MaterialDesignTheme(
    themeState: ThemeState = ThemeState(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val dark = when (themeState.themeMode) {
        ThemeMode.SYSTEM -> darkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val context = LocalContext.current
    val colorScheme = remember(themeState, dark) { resolveColorScheme(context, themeState, dark) }

    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !dark
                isAppearanceLightNavigationBars = !dark
            }
        }
    }

    CompositionLocalProvider(
        LocalReducedMotion provides themeState.reducedMotion,
        LocalHapticsEnabled provides themeState.hapticsEnabled,
        LocalShowBadges provides themeState.showBadges,
        LocalSpacing provides Spacing(),
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            motionScheme = ExpressiveMotion.motionScheme(themeState.reducedMotion),
            content = content,
        )
    }
}

/** Wallpaper colors on Android 12+, otherwise a generated scheme from the preset or custom seed. */
fun resolveColorScheme(context: android.content.Context, themeState: ThemeState, dark: Boolean): ColorScheme {
    val wallpaperAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val base = when {
        themeState.accentSource == AccentSource.WALLPAPER && wallpaperAvailable ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        themeState.accentSource == AccentSource.CUSTOM ->
            seedColorScheme(parseHexColor(themeState.customSeedHex) ?: BrandSeed, dark)
        else -> seedColorScheme(themeState.preset.seed, dark, themeState.preset.monochrome)
    }
    return if (dark && themeState.pureBlack) base.toPureBlack() else base
}
