package dev.qtremors.material.samples.foundations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFoundationSample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Scaffold as a layout foundation", style = MaterialTheme.typography.titleMedium)
        Text(
            "Scaffold coordinates slots for app bars, content, FABs, and system insets. " +
                "Use it as the structural base of every screen before composing components.",
            style = MaterialTheme.typography.bodyMedium
        )
        Card(Modifier.fillMaxWidth().height(280.dp)) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("Scaffold demo") },
                        navigationIcon = {
                            IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
                contentWindowInsets = WindowInsets(0),
            ) { innerPadding ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(PaddingValues(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Content respects topBar, FAB, and inset padding from one source.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
