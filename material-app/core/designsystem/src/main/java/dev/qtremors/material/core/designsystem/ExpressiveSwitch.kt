package dev.qtremors.material.core.designsystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun ExpressiveSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val trackColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        label = "switchTrackColor",
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) {
            Color.Transparent
        } else {
            MaterialTheme.colorScheme.outline
        },
        label = "switchBorderColor",
    )

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 4.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "switchThumbOffset",
    )

    val checkedFraction by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "switchCheckedFraction",
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.outline
        },
        label = "switchThumbColor",
    )

    val checkColor = MaterialTheme.colorScheme.primary
    val closeColor = MaterialTheme.colorScheme.surface

    val clickableModifier = if (onCheckedChange != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
        ) {
            onCheckedChange(!checked)
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(52.dp, 32.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .border(
                width = if (checked) 0.dp else 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(50),
            )
            .then(clickableModifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(thumbOffset.roundToPx(), 0) }
                .size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = thumbColor,
                    radius = size.minDimension / 2f,
                )

                val w = size.width
                val h = size.height

                if (checkedFraction > 0.05f) {
                    val strokeWidth = 2.dp.toPx()
                    val checkPath = Path().apply {
                        moveTo(w * 0.28f, h * 0.52f)
                        lineTo(w * 0.44f, h * 0.68f)
                        lineTo(w * 0.72f, h * 0.36f)
                    }
                    drawPath(
                        path = checkPath,
                        color = checkColor.copy(alpha = checkedFraction),
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                    )
                }

                if (checkedFraction < 0.95f) {
                    val strokeWidth = 2.dp.toPx()
                    val closeAlpha = 1f - checkedFraction
                    val crossPath = Path().apply {
                        moveTo(w * 0.34f, h * 0.34f)
                        lineTo(w * 0.66f, h * 0.66f)
                        moveTo(w * 0.66f, h * 0.34f)
                        lineTo(w * 0.34f, h * 0.66f)
                    }
                    drawPath(
                        path = crossPath,
                        color = closeColor.copy(alpha = closeAlpha),
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                        ),
                    )
                }
            }
        }
    }
}
