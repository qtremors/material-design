package dev.qtremors.material.samples.containment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private data class SwipeTask(val id: Int, val title: String)

@Composable
fun SwipeToDismissSample(modifier: Modifier = Modifier) {
    val tasks = remember {
        mutableStateListOf(
            SwipeTask(0, "Archive old references"),
            SwipeTask(1, "Review expressive shapes"),
            SwipeTask(2, "Sync catalog metadata"),
        )
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Swipe a row toward either edge to dismiss it.", style = MaterialTheme.typography.bodyMedium)
        if (tasks.isEmpty()) {
            Card(Modifier.fillMaxWidth()) {
                Box(Modifier.padding(16.dp)) {
                    Text("Everything dismissed. Nothing left to swipe.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        tasks.forEach { task ->
            val state = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value != SwipeToDismissBoxValue.Settled) {
                        tasks.removeAll { it.id == task.id }
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(
                state = state,
                backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete ${task.title}",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            ) {
                ListItem(
                    headlineContent = { Text(task.title) },
                    supportingContent = { Text("Swipe to dismiss") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroCarouselsSample(modifier: Modifier = Modifier) {
    val items = listOf(0, 1, 2, 3, 4)
    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.errorContainer,
        MaterialTheme.colorScheme.surfaceVariant,
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Horizontal centered hero carousel", style = MaterialTheme.typography.titleMedium)
        HorizontalCenteredHeroCarousel(
            state = rememberCarouselState(initialItem = 0) { items.size },
            modifier = Modifier.fillMaxWidth().height(220.dp)
        ) { index ->
            val item = items[index]
            Box(
                Modifier
                    .fillMaxSize()
                    .background(colors[item], RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Item $item", color = Color.White, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
