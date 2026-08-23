package dev.qtremors.material.feature.apis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.ApiAvailability
import dev.qtremors.material.core.catalog.ApiReference
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry

@Composable
fun ApisScreen(
    entries: List<CatalogEntry>,
    selectedStability: ApiStability,
    onStabilitySelected: (ApiStability) -> Unit,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    query: String = "",
    material3Version: String = "",
    composeUiVersion: String = "",
    stableBaseline: String = "",
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val cleanQuery = query.trim()
    val allReferences = remember(entries) {
        entries.flatMap { entry -> entry.apiReferences.map { entry to it } }
            .distinctBy { it.second.symbol }
            .sortedBy { it.second.symbol }
    }

    val references = remember(allReferences, selectedStability, cleanQuery) {
        allReferences.filter { (entry, api) ->
            val matchesStability = cleanQuery.isNotBlank() || api.stability == selectedStability
            val matchesQuery = cleanQuery.isBlank() ||
                api.symbol.contains(cleanQuery, ignoreCase = true) ||
                entry.officialName.contains(cleanQuery, ignoreCase = true) ||
                (api.optInAnnotation?.contains(cleanQuery, ignoreCase = true) == true)
            matchesStability && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Text(
            text = stringResource(R.string.apis_version_banner, material3Version, composeUiVersion, stableBaseline),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            val options = listOf(
                ApiStability.STABLE to stringResource(R.string.apis_filter_stable_apis),
                ApiStability.EXPERIMENTAL to stringResource(R.string.apis_filter_experimental),
            )
            options.forEachIndexed { index, (stability, label) ->
                SegmentedButton(
                    selected = selectedStability == stability,
                    onClick = { onStabilitySelected(stability) },
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                    modifier = Modifier.weight(1f),
                ) { Text(label) }
            }
        }
        if (cleanQuery.isNotBlank()) {
            Text(
                text = pluralStringResource(R.plurals.apis_search_result_count, references.size, references.size, cleanQuery),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(references, key = { it.second.symbol }) { (entry, api) ->
                ApiCard(entry, api, onEntryClick)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ApiCard(entry: CatalogEntry, api: ApiReference, onEntryClick: (String) -> Unit) {
    Card(
        onClick = { onEntryClick(entry.id) },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = api.symbol.substringAfterLast('.'),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entry.officialName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = if (api.availability == ApiAvailability.STABLE_ARTIFACT) {
                                stringResource(R.string.apis_badge_stable_artifact)
                            } else {
                                stringResource(R.string.apis_badge_alpha_only)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
                api.optInAnnotation?.let { annotation ->
                    AssistChip(
                        onClick = {},
                        label = { Text(annotation, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    )
                }
            }
        }
    }
}
