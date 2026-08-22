package dev.qtremors.material.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.ImplementationKind

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CatalogScreen(
    entries: List<CatalogEntry>,
    selectedCategory: String?,
    bookmarkedIds: Set<String>,
    onCategorySelected: (String?) -> Unit,
    onEntryClick: (String) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val categories = entries.map { it.category }.distinct().sorted()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        androidx.compose.foundation.lazy.LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { FilterChip(selected = selectedCategory == null, onClick = { onCategorySelected(null) }, label = { Text("All") }) }
            items(categories.size) { index ->
                val category = categories[index]
                FilterChip(selected = selectedCategory == category, onClick = { onCategorySelected(category) }, label = { Text(category) })
            }
        }
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = contentPadding.calculateBottomPadding()),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "No matches found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Try an official component name, category, or alias.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 4.dp,
                    bottom = contentPadding.calculateBottomPadding() + 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(entries, key = CatalogEntry::id) { entry ->
                    val bookmarked = entry.id in bookmarkedIds
                    val isExpressive = entry.collections.contains("expressive") || entry.apiReferences.any { it.optInAnnotation?.contains("Expressive") == true }
                    val isExperimental = entry.apiReferences.any { it.stability == ApiStability.EXPERIMENTAL }
                    val isCustom = entry.implementation == ImplementationKind.PROJECT_IMPLEMENTATION

                    Card(
                        onClick = { onEntryClick(entry.id) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth()) {
                                Column(Modifier.weight(1f)) {
                                    Text(entry.officialName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text(entry.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { onBookmarkClick(entry.id, !bookmarked) }) {
                                    Icon(
                                        if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = if (bookmarked) "Remove bookmark" else "Bookmark",
                                    )
                                }
                            }

                            // Badge Row for Expressive, Experimental, and Custom components
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                if (isCustom) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Custom Component", style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    )
                                } else {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Official API", style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                    )
                                }

                                if (isExpressive) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("M3 Expressive", style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    )
                                }

                                if (isExperimental) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Experimental", style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            labelColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    )
                                }
                            }

                            HorizontalDivider()
                            Text(entry.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

