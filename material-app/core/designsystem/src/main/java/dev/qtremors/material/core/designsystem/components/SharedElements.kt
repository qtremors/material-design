package dev.qtremors.material.core.designsystem.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import dev.qtremors.material.core.designsystem.LocalReducedMotion

/** The shared transition layout around the app's navigation, or null where none exists. */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/**
 * The navigation destination's visibility scope. Shared elements only animate between two
 * destinations, so content outside a destination (or inside a pane) provides null.
 */
val LocalNavAnimatedScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/** Key for an entry's badge, shared between a list row and the entry's page. */
fun entryBadgeKey(id: String): String = "entry-badge-$id"

/**
 * Lets an element travel between destinations: the same [key] in the outgoing and incoming page
 * morphs from one place and size to the other. Does nothing with Reduce motion on, or where no
 * shared transition is available.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedEntryBounds(key: String?): Modifier {
    val shared = LocalSharedTransitionScope.current
    val animated = LocalNavAnimatedScope.current
    if (key == null || shared == null || animated == null || LocalReducedMotion.current) return this
    return with(shared) {
        this@sharedEntryBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key),
            animatedVisibilityScope = animated,
            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(ContentScale.Fit),
        )
    }
}
