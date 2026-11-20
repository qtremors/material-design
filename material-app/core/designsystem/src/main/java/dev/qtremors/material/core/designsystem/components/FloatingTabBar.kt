package dev.qtremors.material.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.R

@Immutable
data class TabItem(val icon: ImageVector, val label: String)

/**
 * The floating toolbar at the bottom of every screen, built on Material 3's floating toolbar. On a
 * top-level screen it shows the tabs; on a detail page the selected tab's pill morphs into a Back
 * button beside the page title.
 *
 * The last title and Back action are remembered, so the bar never loses its label mid-transition.
 * The title fits its text between a minimum and a maximum width, and the size change animates.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FloatingTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backTitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    val haptics = rememberAppHaptics()
    val colors = MaterialTheme.colorScheme
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Room for the Back button and side padding; the rest goes to the title, up to a limit.
    val titleWidth = (screenWidth - TITLE_RESERVED).coerceIn(0.dp, TITLE_MAX)
    var lastTitle by remember { mutableStateOf(backTitle) }
    var lastBack by remember { mutableStateOf(onBack) }
    if (backTitle != null) lastTitle = backTitle
    if (onBack != null) lastBack = onBack
    // Everything inside the toolbar gets a definite, animated width. The toolbar measures its content
    // with unbounded width, and while the tabs morph into the Back row a child can report an
    // unbounded size that then overflows and crashes. A fixed outer width clamps it. The title
    // slot is the text's own width plus padding, kept between a minimum (so the bar stays a pill)
    // and what the screen allows.
    val shownTitle = lastTitle ?: stringResource(R.string.action_back)
    val titleStyle = MaterialTheme.typography.titleMedium
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val wanted = with(density) { measurer.measure(shownTitle, titleStyle, maxLines = 1).size.width.toDp() } + TITLE_PADDING_START + TITLE_PADDING_END
    val slotTarget = wanted.coerceIn(TITLE_MIN, titleWidth.coerceAtLeast(TITLE_MIN))
    val slotWidth by animateDpAsState(targetValue = slotTarget, label = "toolbarTitleWidth")
    val tabsWidth = TAB_WIDTH * tabs.size + TAB_GAP * (tabs.size - 1).coerceAtLeast(0)
    val barWidth by animateDpAsState(
        targetValue = if (onBack != null) BACK_BUTTON + slotTarget else tabsWidth,
        label = "toolbarWidth"
    )

    HorizontalFloatingToolbar(
        expanded = true,
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(
            toolbarContainerColor = colors.primary,
            toolbarContentColor = colors.onPrimary
        )
    ) {
        SharedTransitionLayout(modifier = Modifier.width(barWidth).height(BACK_BUTTON)) {
            AnimatedContent(
                targetState = onBack != null,
                transitionSpec = {
                    (fadeIn() + scaleIn(initialScale = 0.85f)) togetherWith
                        (fadeOut() + scaleOut(targetScale = 0.85f)) using null
                },
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
                label = "toolbarMode"
            ) { backMode ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (backMode) {
                        IconButton(shapes = IconButtonDefaults.shapes(),
                            onClick = {
                                haptics.tap()
                                lastBack?.invoke()
                            },
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = PILL),
                                    this@AnimatedContent,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .size(48.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = colors.onPrimary, contentColor = colors.primary)
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.action_back))
                        }
                        Row(
                            modifier = Modifier.width(slotWidth).clipToBounds().padding(start = TITLE_PADDING_START, end = TITLE_PADDING_END),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AnimatedContent(
                                targetState = shownTitle,
                                transitionSpec = {
                                    (fadeIn() + slideInVertically { it / 2 }) togetherWith
                                        (fadeOut() + slideOutVertically { -it / 2 }) using SizeTransform(clip = false)
                                },
                                contentAlignment = Alignment.Center,
                                label = "toolbarTitle"
                            ) { title ->
                                Text(
                                    text = title,
                                    style = titleStyle,
                                    color = colors.onPrimary,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.basicMarquee()
                                )
                            }
                        }
                    } else {
                        tabs.forEachIndexed { index, tab ->
                            val selected = index == selectedIndex
                            IconButton(shapes = IconButtonDefaults.shapes(),
                                onClick = {
                                    haptics.tap()
                                    onSelect(index)
                                },
                                modifier = Modifier
                                    .then(
                                        if (selected) {
                                            Modifier.sharedBounds(
                                                rememberSharedContentState(key = PILL),
                                                this@AnimatedContent,
                                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                            )
                                        } else Modifier
                                    )
                                    .width(TAB_WIDTH)
                                    .height(48.dp),
                                colors = if (selected) {
                                    IconButtonDefaults.filledIconButtonColors(containerColor = colors.onPrimary, contentColor = colors.primary)
                                } else {
                                    IconButtonDefaults.iconButtonColors(containerColor = colors.primary, contentColor = colors.onPrimary)
                                }
                            ) {
                                Icon(tab.icon, contentDescription = tab.label)
                            }
                            if (index < tabs.lastIndex) Spacer(Modifier.width(TAB_GAP))
                        }
                    }
                }
            }
        }
    }
}

private const val PILL = "toolbarPill"
private val TAB_WIDTH = 64.dp
private val TAB_GAP = 4.dp
private val TITLE_RESERVED = 32.dp + 48.dp + 32.dp + 48.dp
private val TITLE_MAX = 200.dp
// Keeps the back bar a pill, never a circle, whatever the title.
private val TITLE_MIN = 64.dp
private val BACK_BUTTON = 48.dp
private val TITLE_PADDING_START = 12.dp
private val TITLE_PADDING_END = 16.dp
