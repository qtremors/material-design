package dev.qtremors.material.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import dev.qtremors.material.core.designsystem.LocalHapticsEnabled

/** Named haptic moments. Every call is a no-op when the user turned haptics off. */
@Stable
class AppHaptics internal constructor(
    private val feedback: HapticFeedback,
    private val enabled: Boolean
) {
    fun tap() = perform(HapticFeedbackType.VirtualKey)
    fun longPress() = perform(HapticFeedbackType.LongPress)
    fun toggle(on: Boolean) = perform(if (on) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff)
    fun tick() = perform(HapticFeedbackType.SegmentTick)
    fun confirm() = perform(HapticFeedbackType.Confirm)

    private fun perform(type: HapticFeedbackType) {
        if (enabled) feedback.performHapticFeedback(type)
    }
}

@Composable
fun rememberAppHaptics(): AppHaptics {
    val feedback = LocalHapticFeedback.current
    val enabled = LocalHapticsEnabled.current
    return remember(feedback, enabled) { AppHaptics(feedback, enabled) }
}
