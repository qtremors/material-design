package dev.qtremors.material.core.designsystem

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable

object ExpressiveMotion {
    const val PressedScale = 0.94f
    const val RestingScale = 1f
    const val SelectedIconLiftDp = 12
    const val HoldDurationMillis = 1_500L
    const val LoadingDurationMillis = 1_200L
    const val TapDurationMillis = 100L
    const val FrameDurationMillis = 16L
    const val PressDampingRatio = Spring.DampingRatioMediumBouncy
    const val PressStiffness = Spring.StiffnessLow
    const val ExpressiveDampingRatio = Spring.DampingRatioLowBouncy
    const val ExpressiveStiffness = Spring.StiffnessLow
    const val StandardDampingRatio = Spring.DampingRatioNoBouncy
    const val StandardStiffness = Spring.StiffnessMedium
    const val ProgressStiffness = Spring.StiffnessHigh
    const val ShakeStiffness = 10_000f

    fun pressScale(pressed: Boolean, enabled: Boolean): Float =
        if (pressed && enabled) PressedScale else RestingScale

    fun holdProgress(elapsedMillis: Long, durationMillis: Long): Float =
        (elapsedMillis.coerceAtLeast(0L).toFloat() / durationMillis.coerceAtLeast(1L))
            .coerceIn(0f, 1f)

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    fun motionScheme(reducedMotion: Boolean): MotionScheme =
        if (reducedMotion) ReducedMotionScheme else MotionScheme.expressive()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private object ReducedMotionScheme : MotionScheme {
    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> = snap()
    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> = snap()
    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> = snap()
    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> = snap()
    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> = snap()
    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> = snap()
}

@Composable
fun <T> pressSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(
        dampingRatio = ExpressiveMotion.PressDampingRatio,
        stiffness = ExpressiveMotion.PressStiffness,
    )

@Composable
fun <T> expressiveSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(
        dampingRatio = ExpressiveMotion.ExpressiveDampingRatio,
        stiffness = ExpressiveMotion.ExpressiveStiffness,
    )

@Composable
fun <T> standardSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(
        dampingRatio = ExpressiveMotion.StandardDampingRatio,
        stiffness = ExpressiveMotion.StandardStiffness,
    )

@Composable
fun <T> softSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(
        dampingRatio = ExpressiveMotion.StandardDampingRatio,
        stiffness = ExpressiveMotion.ExpressiveStiffness,
    )

@Composable
fun <T> progressSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(
        dampingRatio = ExpressiveMotion.StandardDampingRatio,
        stiffness = ExpressiveMotion.ProgressStiffness,
    )

@Composable
fun <T> shakeSpring(): FiniteAnimationSpec<T> =
    if (LocalReducedMotion.current) snap() else spring(stiffness = ExpressiveMotion.ShakeStiffness)
