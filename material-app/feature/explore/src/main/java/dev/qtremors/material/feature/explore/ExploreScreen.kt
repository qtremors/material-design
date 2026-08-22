package dev.qtremors.material.feature.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    entries: List<CatalogEntry>,
    bookmarkedIds: Set<String>,
    recentIds: List<String>,
    onEntryClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val featuredEntries = entries.filter { "featured" in it.collections }
    val expressiveHighlights = entries.filter { "expressive" in it.collections && "featured" !in it.collections }
    val bookmarked = entries.filter { it.id in bookmarkedIds }
    val recent = recentIds.mapNotNull { id -> entries.firstOrNull { it.id == id } }
    val categories = entries.groupBy { it.category }.toList().sortedBy { it.first }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Working reference implementations you can inspect, feel, and locate in source.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text("${entries.size} Components") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text("${entries.count { "expressive" in it.collections }} Expressive") },
                        leadingIcon = {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                    )
                }
            }
        }

        if (featuredEntries.isNotEmpty()) {
            item(key = "featured") {
                ExploreCarouselSection(
                    title = "Featured",
                    items = featuredEntries,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    icon = Icons.Default.Category,
                    onEntryClick = onEntryClick,
                )
            }
        }

        if (categories.isNotEmpty()) {
            item(key = "categories") {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    FlowRow(
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        categories.forEach { (category, categoryEntries) ->
                            CategoryCard(
                                category = category,
                                count = categoryEntries.size,
                                onClick = { onCategoryClick(category) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }

        if (expressiveHighlights.isNotEmpty()) {
            item(key = "expressive") {
                ExploreCarouselSection(
                    title = "Expressive highlights",
                    items = expressiveHighlights,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    icon = Icons.Default.AutoAwesome,
                    onEntryClick = onEntryClick,
                )
            }
        }

        if (bookmarked.isNotEmpty()) {
            item(key = "bookmarks") {
                ExploreCarouselSection(
                    title = "Bookmarks",
                    items = bookmarked,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    icon = Icons.Default.Bookmark,
                    onEntryClick = onEntryClick,
                )
            }
        }

        if (recent.isNotEmpty()) {
            item(key = "recent") {
                ExploreCarouselSection(
                    title = "Recently viewed",
                    items = recent,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    icon = Icons.Default.History,
                    onEntryClick = onEntryClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreCarouselSection(
    title: String,
    items: List<CatalogEntry>,
    containerColor: androidx.compose.ui.graphics.Color,
    icon: ImageVector,
    onEntryClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
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
                colors = CardDefaults.cardColors(containerColor = containerColor),
            ) {
                Box(Modifier.fillMaxSize().padding(18.dp)) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.TopStart),
                    )
                    IconButton(
                        onClick = { onEntryClick(entry.id) },
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open ${entry.officialName}",
                        )
                    }
                    Column(Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = entry.officialName,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = entry.category,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoryIcon = when (category.lowercase()) {
        "actions" -> Icons.Default.TouchApp
        "containment" -> Icons.Default.Inbox
        "navigation" -> Icons.Default.Explore
        "selection" -> Icons.Default.Tune
        "communication" -> Icons.Default.Notifications
        "foundations", "foundation" -> Icons.Default.Palette
        else -> Icons.Default.Category
    }

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier.height(84.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "$count components",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
