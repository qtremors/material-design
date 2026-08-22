package dev.qtremors.material.samples.containment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SegmentedListItemsSample(modifier: Modifier = Modifier) {
    val labels = listOf("Color and theme", "Typography", "Shape and motion")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val reducedMotion = LocalReducedMotion.current
    val supportingEnter = if (reducedMotion) EnterTransition.None else
        fadeIn() + expandVertically()
    val supportingExit = if (reducedMotion) ExitTransition.None else
        fadeOut() + shrinkVertically()
    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            SegmentedListItem(
                selected = selected,
                onClick = { selectedIndex = index },
                shapes = ListItemDefaults.segmentedShapes(index, labels.size),
                leadingContent = { RadioButton(selected = selected, onClick = null) },
                trailingContent = {
                    if (selected) Icon(Icons.Default.Check, contentDescription = "Selected")
                    else Icon(Icons.Default.Favorite, contentDescription = null)
                },
                supportingContent = {
                    AnimatedVisibility(
                        visible = selected,
                        enter = supportingEnter,
                        exit = supportingExit,
                    ) { Text("Inspect this related foundation") }
                },
                content = { Text(label) },
            )
        }
    }
}

@Composable
fun CarouselsSample(modifier: Modifier = Modifier) {
    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.inversePrimary,
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("Multi-browse", style = MaterialTheme.typography.titleMedium)
        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { colors.size },
            preferredItemWidth = 180.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) { index ->
            Box(
                Modifier
                    .height(150.dp)
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .background(colors[index])
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                )
            }
        }
        Text("Uncontained", style = MaterialTheme.typography.titleMedium)
        HorizontalUncontainedCarousel(
            state = rememberCarouselState { colors.size },
            itemWidth = 150.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) { index ->
            Box(
                Modifier
                    .height(112.dp)
                    .maskClip(MaterialTheme.shapes.large)
                    .background(colors[index])
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart,
            ) {
                Text(
                    text = "Card ${index + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun PullToRefreshSample(modifier: Modifier = Modifier) {
    var isRefreshing by remember { mutableStateOf(false) }
    var messageCount by remember { mutableIntStateOf(12) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1_200)
            messageCount += 6
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { isRefreshing = true },
        modifier = modifier,
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(messageCount) { index ->
                ListItem(
                    headlineContent = { Text("Inbox message ${index + 1}") },
                    supportingContent = { Text("Pull down to check for new messages.") },
                )
            }
        }
    }
}
