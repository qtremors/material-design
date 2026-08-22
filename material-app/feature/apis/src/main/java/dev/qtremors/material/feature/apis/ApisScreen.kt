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
import androidx.compose.ui.Modifier
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
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val references = entries.flatMap { entry -> entry.apiReferences.map { entry to it } }
        .filter { it.second.stability == selectedStability }
        .distinctBy { it.second.symbol }
        .sortedBy { it.second.symbol }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        Text(
            "Compose Material 3 · 1.5.0-alpha23\nCompose UI · 1.12.0-alpha03 · stable Material baseline · 1.4.0",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        ) {
            val options = listOf(ApiStability.STABLE to "Stable APIs", ApiStability.EXPERIMENTAL to "Experimental")
            options.forEachIndexed { index, (stability, label) ->
                SegmentedButton(
                    selected = selectedStability == stability,
                    onClick = { onStabilitySelected(stability) },
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                    modifier = Modifier.weight(1f),
                ) { Text(label) }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
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
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(api.symbol.substringAfterLast('.'), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Text(entry.officialName, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(if (api.availability == ApiAvailability.STABLE_ARTIFACT) "Stable artifact" else "Alpha only", maxLines = 1, overflow = TextOverflow.Ellipsis) },
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
