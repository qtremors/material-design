package dev.qtremors.material.core.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Google Sans Flex, the Material 3 Expressive typeface, as one variable font file. Each weight is
 * an instance of that file, so the APK carries one font instead of four.
 */
@OptIn(ExperimentalTextApi::class)
private fun flex(weight: FontWeight) = Font(
    R.font.google_sans_flex_variable,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val GoogleSansFlex = FontFamily(
    flex(FontWeight.Normal),
    flex(FontWeight.Medium),
    flex(FontWeight.SemiBold),
    flex(FontWeight.Bold),
)

private fun style(size: Int, line: Int, weight: FontWeight = FontWeight.Normal, tracking: Double = 0.0) =
    TextStyle(
        fontFamily = GoogleSansFlex,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = line.sp,
        letterSpacing = tracking.sp,
    )

/** The Material type scale on Google Sans Flex, with tighter body tracking than Roboto needs. */
val AppTypography = Typography(
    displayLarge = style(57, 64, tracking = -0.25),
    displayMedium = style(45, 52),
    displaySmall = style(36, 44),
    headlineLarge = style(32, 40, FontWeight.Medium),
    headlineMedium = style(28, 36, FontWeight.Medium),
    headlineSmall = style(24, 32, FontWeight.Medium),
    titleLarge = style(22, 28, FontWeight.Medium),
    titleMedium = style(16, 24, FontWeight.Medium, 0.1),
    titleSmall = style(14, 20, FontWeight.Medium, 0.1),
    bodyLarge = style(16, 24, tracking = 0.15),
    bodyMedium = style(14, 20, tracking = 0.15),
    bodySmall = style(12, 16, tracking = 0.2),
    labelLarge = style(14, 20, FontWeight.Medium, 0.1),
    labelMedium = style(12, 16, FontWeight.Medium, 0.3),
    labelSmall = style(11, 16, FontWeight.Medium, 0.3),
)

/** A count or measurement shown large, such as "96 of 499". Digits stay aligned. */
val MetricTextStyle: TextStyle = style(28, 34, FontWeight.SemiBold).copy(fontFeatureSettings = "tnum")

/** API symbols, paths, and versions: monospaced so identifiers read as code. */
val CodeTextStyle: TextStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 13.sp,
    lineHeight = 18.sp,
)
