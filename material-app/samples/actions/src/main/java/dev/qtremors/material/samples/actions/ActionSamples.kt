package dev.qtremors.material.samples.actions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import dev.qtremors.material.core.designsystem.ExpressiveMotion
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.expressiveSpring
import dev.qtremors.material.core.designsystem.pressSpring
import dev.qtremors.material.core.designsystem.progressSpring
import dev.qtremors.material.core.designsystem.shakeSpring
import dev.qtremors.material.core.designsystem.softSpring
import dev.qtremors.material.core.designsystem.standardSpring
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonsSample(modifier: Modifier = Modifier) {
    var loading by remember { mutableStateOf(false) }
    var confirmed by remember { mutableStateOf(false) }
    LaunchedEffect(loading) {
        if (loading) {
            delay(ExpressiveMotion.LoadingDurationMillis)
            loading = false
        }
    }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(onClick = {}) { Text("Filled") }
        ExpressiveActionButton(onClick = {}, text = "Shape-morphing — press and hold")
        ExpressiveActionButton(
            onClick = { loading = true },
            text = "Run action",
            loading = loading,
        )
        HoldToConfirmButton(
            text = if (confirmed) "Confirmed" else "Hold to confirm",
            onHoldComplete = { confirmed = true },
        )
        FluidToggleButtonGroup()
        RejectedActionButton()
        FilledTonalButton(onClick = {}) { Text("Filled tonal") }
        ElevatedButton(onClick = {}) { Text("Elevated") }
        OutlinedButton(onClick = {}) { Text("Outlined") }
        TextButton(onClick = {}) { Text("Text") }
        Button(onClick = {}, enabled = false) { Text("Disabled") }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveActionButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    var tapGeneration by remember { mutableIntStateOf(0) }
    var tapped by remember { mutableStateOf(false) }
    LaunchedEffect(tapGeneration) {
        if (tapGeneration > 0) {
            tapped = true
            delay(ExpressiveMotion.TapDurationMillis)
            tapped = false
        }
    }
    val visualPressed = pressed || tapped
    val reducedMotion = LocalReducedMotion.current
    val loadingEnter = if (reducedMotion) EnterTransition.None else androidx.compose.animation.fadeIn(animationSpec = softSpring())
    val loadingExit = if (reducedMotion) ExitTransition.None else androidx.compose.animation.fadeOut(animationSpec = softSpring())
    val cornerRadius by animateDpAsState(
        targetValue = if (visualPressed && enabled) 24.dp else 32.dp,
        animationSpec = standardSpring(),
        label = "expressive button corner radius",
    )
    val contentScale by animateFloatAsState(
        targetValue = ExpressiveMotion.pressScale(visualPressed, enabled),
        animationSpec = pressSpring(),
        label = "expressive button content scale",
    )
    val extraPadding by animateDpAsState(
        targetValue = if (visualPressed) 16.dp else 0.dp,
        animationSpec = standardSpring(),
        label = "expressive button horizontal padding",
    )
    val containerColor by animateColorAsState(
        targetValue = if (loading) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
        animationSpec = standardSpring(),
        label = "expressive button container color",
    )
    val contentColor by animateColorAsState(
        targetValue = if (loading) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
        animationSpec = standardSpring(),
        label = "expressive button content color",
    )
    Button(
        onClick = {
            tapGeneration++
            onClick()
        },
        enabled = enabled && !loading,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        interactionSource = interactionSource,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp + extraPadding),
        modifier = modifier
            .height(64.dp)
            .semantics { if (loading) stateDescription = "Loading" },
    ) {
        AnimatedContent(
            targetState = loading,
            transitionSpec = { loadingEnter togetherWith loadingExit },
            label = "button loading content",
        ) { isLoading ->
            if (isLoading) {
                LoadingIndicator(modifier = Modifier.size(64.dp * 0.6f))
            } else {
                Text(
                    text,
                    modifier = Modifier.graphicsLayer {
                        scaleX = contentScale
                        scaleY = contentScale
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HoldToConfirmButton(
    text: String,
    onHoldComplete: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    holdDurationMillis: Long = ExpressiveMotion.HoldDurationMillis,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val stillPressed by rememberUpdatedState(pressed)
    var rawProgress by remember { mutableFloatStateOf(0f) }
    val reducedMotion = LocalReducedMotion.current
    val haptics = LocalHapticFeedback.current
    val currentOnHoldComplete by rememberUpdatedState(onHoldComplete)
    val animatedProgress by animateFloatAsState(
        targetValue = rawProgress,
        animationSpec = if (rawProgress < 0.05f) softSpring() else progressSpring(),
        label = "hold progress",
    )
    val cornerRadius by animateDpAsState(
        targetValue = if (pressed && enabled) 24.dp else 32.dp,
        animationSpec = standardSpring(),
        label = "hold button corner radius",
    )
    val contentScale by animateFloatAsState(
        targetValue = ExpressiveMotion.pressScale(pressed, enabled),
        animationSpec = pressSpring(),
        label = "hold button content scale",
    )
    val extraPadding by animateDpAsState(
        targetValue = if (pressed && enabled) 16.dp else 0.dp,
        animationSpec = standardSpring(),
        label = "hold button horizontal padding",
    )
    val safeDuration = holdDurationMillis.coerceAtLeast(1L)

    LaunchedEffect(pressed, enabled, holdDurationMillis) {
        try {
            rawProgress = 0f
            if (pressed && enabled) {
                runCatching { haptics.performHapticFeedback(HapticFeedbackType.LongPress) }
                if (reducedMotion) {
                    delay(safeDuration)
                } else {
                    val startedAt = withFrameMillis { it }
                    while (stillPressed && rawProgress < 1f) {
                        val now = withFrameMillis { it }
                        rawProgress = ExpressiveMotion.holdProgress(now - startedAt, safeDuration)
                    }
                }
                if (stillPressed) {
                    runCatching { haptics.performHapticFeedback(HapticFeedbackType.LongPress) }
                    currentOnHoldComplete()
                }
            }
        } finally {
            rawProgress = 0f
        }
    }

    Surface(
            onClick = {},
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(cornerRadius),
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = modifier
                .height(64.dp)
                .semantics {
                    stateDescription = if (rawProgress > 0f) {
                        "Holding ${(rawProgress * 100).toInt()} percent"
                    } else {
                        "Hold to confirm"
                    }
                    onLongClick(label = "Confirm") {
                        if (enabled) {
                            currentOnHoldComplete()
                            true
                        } else {
                            false
                        }
                    }
                },
        ) {
        Box(contentAlignment = Alignment.Center) {
            if (!reducedMotion && animatedProgress > 0f) {
                val progressBrush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.24f),
                    ),
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .drawBehind {
                            drawRect(
                                brush = progressBrush,
                                size = size.copy(width = size.width * animatedProgress.coerceIn(0f, 1f)),
                            )
                        },
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp + extraPadding)
                    .graphicsLayer {
                        scaleX = contentScale
                        scaleY = contentScale
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) { Text(text) }
        }
    }
}

@Composable
fun FluidToggleButtonGroup(modifier: Modifier = Modifier) {
    val labels = listOf("View", "Edit", "Share")
    var selectedIndex by remember { mutableIntStateOf(0) }
    Row(
        modifier = modifier.fillMaxWidth().height(40.dp).selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        labels.forEachIndexed { index, label ->
            val interactionSource = remember(label) { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            var tapGeneration by remember(label) { mutableIntStateOf(0) }
            var tapped by remember(label) { mutableStateOf(false) }
            LaunchedEffect(label, tapGeneration) {
                if (tapGeneration > 0) {
                    tapped = true
                    delay(ExpressiveMotion.TapDurationMillis)
                    tapped = false
                }
            }
            val visualPressed = pressed || tapped
            val selected = selectedIndex == index
            val animatedWeight by animateFloatAsState(
                targetValue = when {
                    visualPressed -> 1.4f
                    selected -> 1.2f
                    else -> 1f
                },
                animationSpec = expressiveSpring(),
                label = "$label dynamic weight",
            )
            val outerRadius = 20.dp
            val innerRadius = 8.dp
            val pressedRadius = 12.dp
            val startRadius by animateDpAsState(
                targetValue = if (visualPressed) pressedRadius else if (selected || index == 0) outerRadius else innerRadius,
                animationSpec = standardSpring(),
                label = "$label start radius",
            )
            val endRadius by animateDpAsState(
                targetValue = if (visualPressed) pressedRadius else if (selected || index == labels.lastIndex) outerRadius else innerRadius,
                animationSpec = standardSpring(),
                label = "$label end radius",
            )
            Button(
                onClick = {
                    tapGeneration++
                    selectedIndex = index
                },
                interactionSource = interactionSource,
                shape = RoundedCornerShape(
                    topStart = startRadius,
                    bottomStart = startRadius,
                    topEnd = endRadius,
                    bottomEnd = endRadius,
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow,
                    contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier
                    .weight(animatedWeight)
                    .fillMaxHeight()
                    .semantics { this.selected = selected },
            ) { Text(label, maxLines = 1) }
        }
    }
}

@Composable
fun RejectedActionButton(modifier: Modifier = Modifier) {
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val reducedMotion = LocalReducedMotion.current
    val shakeSpec = shakeSpring<Float>()
    var shakeJob by remember { mutableStateOf<Job?>(null) }
    OutlinedButton(
        onClick = {
            runCatching { haptics.performHapticFeedback(HapticFeedbackType.LongPress) }
            shakeJob?.cancel()
            if (!reducedMotion) {
                shakeJob = scope.launch {
                    offset.snapTo(0f)
                    listOf(6f, -6f, 3f, -3f, 0f).forEach { target ->
                        offset.animateTo(target, animationSpec = shakeSpec)
                    }
                }
            }
        },
        modifier = modifier.graphicsLayer { translationX = offset.value.dp.toPx() },
    ) { Text("Preview rejected action") }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplitButtonsSample(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier) {
        SplitButtonLayout(
            leadingButton = {
                SplitButtonDefaults.LeadingButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Share")
                }
            },
            trailingButton = {
                SplitButtonDefaults.TrailingButton(
                    checked = expanded,
                    onCheckedChange = { expanded = it },
                ) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Related share actions",
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Copy link") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text("Share as image") }, onClick = { expanded = false })
        }
    }
}

@Composable
fun FloatingActionButtonsSample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallFloatingActionButton(
                onClick = {},
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
            FloatingActionButton(
                onClick = {},
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            LargeFloatingActionButton(
                onClick = {},
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
        }
        ExtendedFloatingActionButton(
            onClick = {},
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            text = { Text("Create note") },
            modifier = Modifier.align(Alignment.End),
        )
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun IconButtonsSample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite this reference")
            }
            FilledIconButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add a reference")
            }
            FilledTonalIconButton(onClick = {}) {
                Icon(Icons.Default.Edit, contentDescription = "Edit the reference")
            }
            OutlinedIconButton(onClick = {}) {
                Icon(Icons.Default.Share, contentDescription = "Share the reference")
            }
        }
        Text("Icon toggles", style = MaterialTheme.typography.titleMedium)
        var filledChecked by remember { mutableStateOf(false) }
        var outlinedChecked by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledIconToggleButton(
                checked = filledChecked,
                onCheckedChange = { filledChecked = it },
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = if (filledChecked) "Remove from favorites" else "Add to favorites",
                )
            }
            OutlinedIconToggleButton(
                checked = outlinedChecked,
                onCheckedChange = { outlinedChecked = it },
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = if (outlinedChecked) "Marked as done" else "Mark as done",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenusSample(modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }
    val reducedMotion = LocalReducedMotion.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("FAB menu", style = MaterialTheme.typography.titleMedium)
        Box(
            Modifier
                .fillMaxWidth()
                .height(280.dp),
        ) {
            FloatingActionButtonMenu(
                expanded = menuExpanded,
                button = {
                    ToggleFloatingActionButton(
                        checked = menuExpanded,
                        onCheckedChange = { menuExpanded = it },
                    ) {
                        val rotation by animateFloatAsState(
                            targetValue = if (menuExpanded) 45f else 0f,
                            label = "fab menu toggle rotation",
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = if (menuExpanded) "Close menu" else "Open menu",
                            modifier = Modifier.graphicsLayer { rotationZ = rotation },
                        )
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = menuExpanded,
                    enter = if (reducedMotion) EnterTransition.None else fadeIn() + expandVertically(),
                    exit = if (reducedMotion) ExitTransition.None else fadeOut() + shrinkVertically(),
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {},
                        icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        text = { Text("New note") },
                        modifier = Modifier.padding(4.dp),
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = menuExpanded,
                    enter = if (reducedMotion) EnterTransition.None else fadeIn() + expandVertically(),
                    exit = if (reducedMotion) ExitTransition.None else fadeOut() + shrinkVertically(),
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {},
                        icon = { Icon(Icons.Default.Share, contentDescription = null) },
                        text = { Text("Share note") },
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        }
    }
}
