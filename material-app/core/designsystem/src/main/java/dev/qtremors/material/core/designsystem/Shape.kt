package dev.qtremors.material.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Text fields: rounded like the rows around them, not Material's default 4 dp. */
val FieldShape: Shape = RoundedCornerShape(16.dp)

/** Outer corner of a group of connected rows. */
val GroupCorner: Dp = 24.dp

/** Inner corner where two connected rows meet. */
val RowCorner: Dp = 6.dp

/** Gap between connected rows; the page shows through it as a hairline. */
val RowGap: Dp = 2.dp

/**
 * The shape of row [index] in a group of [count] connected rows: rounded outside, tight inside.
 * A single row is fully rounded.
 */
fun connectedRowShape(index: Int, count: Int, outer: Dp = GroupCorner, inner: Dp = RowCorner): Shape {
    val top = if (index == 0) outer else inner
    val bottom = if (index == count - 1) outer else inner
    return RoundedCornerShape(topStart = top, topEnd = top, bottomStart = bottom, bottomEnd = bottom)
}
