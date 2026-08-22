package dev.qtremors.material.core.designsystem

import androidx.compose.material3.MotionScheme
import androidx.compose.animation.core.SnapSpec
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpressiveMotionTest {
    @Test
    fun pressScaleOnlyCompressesEnabledPressedContent() {
        assertEquals(ExpressiveMotion.PressedScale, ExpressiveMotion.pressScale(pressed = true, enabled = true), 0f)
        assertEquals(ExpressiveMotion.RestingScale, ExpressiveMotion.pressScale(pressed = false, enabled = true), 0f)
        assertEquals(ExpressiveMotion.RestingScale, ExpressiveMotion.pressScale(pressed = true, enabled = false), 0f)
    }

    @Test
    fun inheritedPhysicsMatchTheStandaloneReference() {
        assertEquals(0.94f, ExpressiveMotion.PressedScale, 0f)
        assertEquals(12, ExpressiveMotion.SelectedIconLiftDp)
        assertEquals(1_500L, ExpressiveMotion.HoldDurationMillis)
        assertEquals(1_200L, ExpressiveMotion.LoadingDurationMillis)
        assertEquals(100L, ExpressiveMotion.TapDurationMillis)
        assertEquals(10_000f, ExpressiveMotion.ShakeStiffness, 0f)
    }

    @Test
    fun holdProgressClampsInvalidAndInterruptedTiming() {
        assertEquals(0f, ExpressiveMotion.holdProgress(-50L, 1_500L), 0f)
        assertEquals(0f, ExpressiveMotion.holdProgress(0L, 0L), 0f)
        assertEquals(0.5f, ExpressiveMotion.holdProgress(750L, 1_500L), 0f)
        assertEquals(1f, ExpressiveMotion.holdProgress(2_000L, 1_500L), 0f)
    }

    @Test
    fun fullMotionUsesTheOfficialExpressiveScheme() {
        assertSame(MotionScheme.expressive(), ExpressiveMotion.motionScheme(reducedMotion = false))
    }

    @Test
    fun reducedMotionSnapsEveryOfficialSpecFamily() {
        val scheme = ExpressiveMotion.motionScheme(reducedMotion = true)
        assertNotSame(MotionScheme.expressive(), scheme)
        listOf(
            scheme.defaultSpatialSpec<Float>(),
            scheme.fastSpatialSpec<Float>(),
            scheme.slowSpatialSpec<Float>(),
            scheme.defaultEffectsSpec<Float>(),
            scheme.fastEffectsSpec<Float>(),
            scheme.slowEffectsSpec<Float>(),
        ).forEach { spec -> assertTrue("Expected snap spec but was $spec", spec is SnapSpec) }
    }
}
