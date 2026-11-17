package dev.qtremors.material.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Spacing on a 4 dp grid, plus the semantic gaps screens use. */
@Immutable
data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val space12: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val space20: Dp = 20.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    /** Side margin of every screen. */
    val screenGutter: Dp = 16.dp,
    /** Horizontal padding inside a row. */
    val rowHorizontal: Dp = 16.dp,
    /** Vertical padding inside a row. */
    val rowVertical: Dp = 14.dp,
    /** Space above a section header. */
    val sectionGap: Dp = 20.dp,
    /** Section header inset, so headers line up with row text rather than row edges. */
    val sectionHeaderInset: Dp = 16.dp,
    /** Space kept free under the floating tab bar. */
    val toolbarClearance: Dp = 112.dp,
)

val MaterialTheme.spacing: Spacing
    @Composable @ReadOnlyComposable get() = LocalSpacing.current
