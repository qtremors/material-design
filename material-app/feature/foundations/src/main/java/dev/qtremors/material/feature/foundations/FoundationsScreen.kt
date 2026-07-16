package dev.qtremors.material.feature.foundations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry

@Composable
fun FoundationsScreen(
    entries: List<CatalogEntry>,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("Foundations", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Inspectable foundations appear here only when a working reference exists.",
                modifier = Modifier.padding(top = 6.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(entries, key = CatalogEntry::id) { entry ->
            Card(
                onClick = { onEntryClick(entry.id) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                androidx.compose.foundation.layout.Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(entry.officialName, style = MaterialTheme.typography.titleLarge)
                    Text(entry.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
