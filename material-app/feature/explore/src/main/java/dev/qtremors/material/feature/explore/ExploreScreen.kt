package dev.qtremors.material.feature.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    entries: List<CatalogEntry>,
    bookmarkedIds: Set<String>,
    recentIds: List<String>,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sections = buildList {
        add("Featured" to entries.filter { "featured" in it.collections })
        add("Recently added" to entries.filter { "recent" in it.collections })
        add("Expressive" to entries.filter { "expressive" in it.collections })
        val bookmarked = entries.filter { it.id in bookmarkedIds }
        if (bookmarked.isNotEmpty()) add("Bookmarks" to bookmarked)
        val recent = recentIds.mapNotNull { id -> entries.firstOrNull { it.id == id } }
        if (recent.isNotEmpty()) add("Recently viewed" to recent)
        entries.groupBy { it.category }.forEach { (category, categoryEntries) ->
            add(category to categoryEntries)
        }
    }.filter { it.second.isNotEmpty() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Explore Material 3", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Working reference implementations you can inspect, feel, and locate in source.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        sections.forEach { (title, items) ->
            item(key = title) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(title, modifier = Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalMultiBrowseCarousel(
                        state = rememberCarouselState { items.size },
                        preferredItemWidth = 220.dp,
                        itemSpacing = 10.dp,
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) { index ->
                        val entry = items[index]
                        Card(
                            onClick = { onEntryClick(entry.id) },
                            modifier = Modifier
                                .height(170.dp)
                                .maskClip(MaterialTheme.shapes.extraLarge),
                            colors = CardDefaults.cardColors(
                                containerColor = when (title) {
                                    "Expressive" -> MaterialTheme.colorScheme.tertiaryContainer
                                    "Bookmarks" -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                },
                            ),
                        ) {
                            Box(Modifier.fillMaxSize().padding(18.dp)) {
                                Icon(
                                    imageVector = when (title) {
                                        "Expressive" -> Icons.Default.AutoAwesome
                                        "Bookmarks" -> Icons.Default.Bookmark
                                        "Recently viewed" -> Icons.Default.History
                                        else -> Icons.Default.Category
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.TopStart),
                                )
                                IconButton(onClick = { onEntryClick(entry.id) }, modifier = Modifier.align(Alignment.TopEnd)) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Open ${entry.officialName}")
                                }
                                Column(Modifier.align(Alignment.BottomStart)) {
                                    Text(entry.officialName, style = MaterialTheme.typography.titleLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text(entry.category, style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
