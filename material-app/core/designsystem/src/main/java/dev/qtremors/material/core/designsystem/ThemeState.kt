package dev.qtremors.material.core.designsystem

import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    OLED
}

// Accent Colors (Material 500)
val AccentRed = Color(0xFFF44336)
val AccentPink = Color(0xFFE91E63)
val AccentPurple = Color(0xFF9C27B0)
val AccentDeepPurple = Color(0xFF673AB7)
val AccentIndigo = Color(0xFF3F51B5)
val AccentBlue = Color(0xFF2196F3)
val AccentLightBlue = Color(0xFF03A9F4)
val AccentCyan = Color(0xFF00BCD4)
val AccentTeal = Color(0xFF009688)
val AccentGreen = Color(0xFF4CAF50)
val AccentLightGreen = Color(0xFF8BC34A)
val AccentLime = Color(0xFFCDDC39)
val AccentYellow = Color(0xFFFFEB3B)
val AccentAmber = Color(0xFFFFC107)
val AccentOrange = Color(0xFFFF9800)
val AccentDeepOrange = Color(0xFFFF5722)
val AccentBrown = Color(0xFF795548)
val AccentBlueGrey = Color(0xFF607D8B)
val AccentGrey = Color(0xFF757575)
val AccentBlack = Color(0xFF111111)

enum class AccentColor(val color: Color?) {
    DYNAMIC(null),
    MONOCHROME(null),
    RED(AccentRed),
    PINK(AccentPink),
    PURPLE(AccentPurple),
    DEEP_PURPLE(AccentDeepPurple),
    CYAN(AccentCyan),
    LIGHT_BLUE(AccentLightBlue),
    BLUE(AccentBlue),
    INDIGO(AccentIndigo),
    TEAL(AccentTeal),
    GREEN(AccentGreen),
    LIGHT_GREEN(AccentLightGreen),
    LIME(AccentLime),
    DEEP_ORANGE(AccentDeepOrange),
    ORANGE(AccentOrange),
    AMBER(AccentAmber),
    YELLOW(AccentYellow),
    BROWN(AccentBrown),
    BLUE_GREY(AccentBlueGrey),
    GREY(AccentGrey),
    BLACK(AccentBlack),
}

enum class ThemePreset {
    NONE,
    DRACULA,
    TOKYO_NIGHT,
    CUSTOM
}

data class ThemeState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accentColor: AccentColor = AccentColor.DYNAMIC,
    val themePreset: ThemePreset = ThemePreset.NONE,
    val customPrimaryColorHex: String = "#BD93F9",
    val customBackgroundColorHex: String = "#282A36",
    val harmonizeColors: Boolean = true,
    val reducedMotion: Boolean = false,
)
