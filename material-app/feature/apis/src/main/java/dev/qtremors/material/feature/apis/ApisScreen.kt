package dev.qtremors.material.feature.apis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.ApiReference
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.CodeTextStyle
import dev.qtremors.material.core.designsystem.components.ConnectedChoices
import dev.qtremors.material.core.designsystem.components.ConnectedGroup
import dev.qtremors.material.core.designsystem.components.ConnectedItem
import dev.qtremors.material.core.designsystem.components.ConnectedRow
import dev.qtremors.material.core.designsystem.components.EmptyState
import dev.qtremors.material.core.designsystem.components.IconBadge
import dev.qtremors.material.core.designsystem.components.InlineSearchField
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.TagRow
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.apiTags

/**
 * Every official symbol the catalog cites, split by stability and grouped by the entry that
 * demonstrates it. Tapping a symbol opens that entry.
 */
@Composable
fun ApisScreen(
    entries: List<CatalogEntry>,
    selectedStability: ApiStability,
    onStabilitySelected: (ApiStability) -> Unit,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    material3Version: String = "",
    composeUiVersion: String = "",
    stableBaseline: String = "",
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var filter by rememberSaveable { mutableStateOf("") }
    val counts = remember(entries) {
        val all = entries.flatMap(CatalogEntry::apiReferences)
        ApiStability.entries.associateWith { stability -> all.count { it.stability == stability } }
    }
    val groups = remember(entries, selectedStability, filter) {
        val needle = filter.trim().lowercase()
        entries.mapNotNull { entry ->
            val apis = entry.apiReferences.filter { api ->
                api.stability == selectedStability && (needle.isEmpty() || api.symbol.lowercase().contains(needle))
            }
            if (apis.isEmpty()) null else entry to apis
        }.sortedBy { it.first.officialName }
    }

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
        item(key = "versions-header") { SectionHeader(stringResource(R.string.apis_versions_title)) }
        item(key = "versions") {
            ConnectedGroup {
                ConnectedRow(
                    title = stringResource(R.string.apis_version_material3),
                    supporting = material3Version,
                    leading = { IconBadge(Icons.Rounded.Widgets, key = "material3") },
                )
                ConnectedRow(
                    title = stringResource(R.string.apis_version_compose_ui),
                    supporting = composeUiVersion,
                    leading = { IconBadge(Icons.Rounded.Layers, key = "compose") },
                )
                ConnectedRow(
                    title = stringResource(R.string.apis_version_baseline),
                    supporting = "$stableBaseline · " + stringResource(R.string.apis_version_baseline_supporting),
                    leading = { IconBadge(Icons.Rounded.History, key = "baseline") },
                )
            }
        }
        item(key = "controls") {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(top = MaterialTheme.spacing.sectionGap),
            ) {
                ConnectedChoices(
                    options = ApiStability.entries,
                    selected = selectedStability,
                    label = { stability ->
                        val count = counts[stability] ?: 0
                        stringResource(
                            if (stability == ApiStability.STABLE) R.string.apis_filter_stable else R.string.apis_filter_experimental,
                            count,
                        )
                    },
                    onSelect = onStabilitySelected,
                )
                InlineSearchField(
                    query = filter,
                    onQueryChange = { filter = it },
                    placeholder = stringResource(R.string.apis_filter_placeholder),
                )
            }
        }
        if (groups.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    icon = Icons.Rounded.SearchOff,
                    title = stringResource(R.string.apis_empty_title, filter.trim()),
                    message = stringResource(R.string.apis_empty_body),
                )
            }
        }
        groups.forEach { (entry, apis) ->
            item(key = "header-${entry.id}") { SectionHeader(entry.officialName) }
            apis.forEachIndexed { index, api ->
                item(key = "${entry.id}-${api.symbol}") {
                    ConnectedItem(index, apis.size, modifier = Modifier.animateItem()) {
                        ApiRow(api, onClick = { onEntryClick(entry.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiRow(api: ApiReference, onClick: () -> Unit) {
    ConnectedRow(
        title = api.symbol.substringAfterLast('.'),
        onClick = onClick,
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
                Text(
                    api.symbol.substringBeforeLast('.'),
                    style = CodeTextStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "${api.artifact.substringAfter(':')} ${api.reviewedVersion}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TagRow(apiTags(api), force = true)
            }
        },
    )
}
