package dev.qtremors.material.samples.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarArrangement
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleBottomAppBarSample(modifier: Modifier = Modifier) {
    var actionResult by remember { mutableStateOf("Choose a flexible bar action") }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(actionResult, style = MaterialTheme.typography.bodyMedium)
        Card(Modifier.fillMaxWidth()) {
            FlexibleBottomAppBar {
                IconButton(onClick = { actionResult = "Reference marked complete" }) {
                    Icon(Icons.Default.Check, contentDescription = "Mark complete")
                }
                IconButton(onClick = { actionResult = "Reference opened for editing" }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { actionResult = "Dictation started" }) {
                    Icon(Icons.Default.Mic, contentDescription = "Dictate")
                }
            }
        }
    }
}

@Composable
fun WideNavigationRailSample(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableIntStateOf(0) }
    var modalVisible by remember { mutableStateOf(false) }
    val railState = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val items = listOf("Home", "Search", "Profile")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Person)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Person)
    val railExpanded = railState.targetValue == WideNavigationRailValue.Expanded

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Wide Navigation Rail", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth().height(300.dp)) {
            Row(Modifier.fillMaxSize()) {
                WideNavigationRail(
                    state = railState,
                    header = {
                        IconButton(onClick = { scope.launch { railState.toggle() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Toggle rail")
                        }
                    }
                ) {
                    items.forEachIndexed { index, item ->
                        WideNavigationRailItem(
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            icon = {
                                Icon(
                                    if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            railExpanded = railExpanded,
                        )
                    }
                }
                Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Destination: ${items[selectedItem]}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            if (railExpanded) "Rail expanded" else "Rail collapsed",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Text("Modal Wide Navigation Rail", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Opens as an overlay over content.", style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { modalVisible = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Open modal rail")
                }
            }
        }
    }

    if (modalVisible) {
        ModalWideNavigationRail(
            state = rememberModalRailState(),
            header = {
                IconButton(onClick = { modalVisible = false }) {
                    Icon(Icons.Default.Menu, contentDescription = "Close modal rail")
                }
            }
        ) {
            items.forEachIndexed { index, item ->
                WideNavigationRailItem(
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        modalVisible = false
                    },
                    icon = {
                        Icon(
                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                            contentDescription = item
                        )
                    },
                    label = { Text(item) },
                    railExpanded = true,
                )
            }
        }
    }
}

@Composable
private fun rememberModalRailState() = rememberWideNavigationRailState(WideNavigationRailValue.Expanded)

@Composable
fun ShortNavigationBarSample(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Search", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Settings)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Short Navigation Bar", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Box(Modifier.padding(16.dp)) {
                Text(
                    "Selected screen: ${items[selectedIndex]}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            ShortNavigationBar(arrangement = ShortNavigationBarArrangement.EqualWeight) {
                items.forEachIndexed { index, item ->
                    ShortNavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                if (selectedIndex == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTabRowsSample(modifier: Modifier = Modifier) {
    var primaryIndex by remember { mutableIntStateOf(0) }
    var secondaryIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("All", "News", "Videos", "Images", "Shopping", "Maps", "Books", "Flights")

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Primary scrollable tab row", style = MaterialTheme.typography.titleMedium)
        PrimaryScrollableTabRow(selectedTabIndex = primaryIndex, edgePadding = 16.dp) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = primaryIndex == index,
                    onClick = { primaryIndex = index },
                    text = { Text(title) }
                )
            }
        }
        Text("Secondary scrollable tab row", style = MaterialTheme.typography.titleMedium)
        SecondaryScrollableTabRow(selectedTabIndex = secondaryIndex, edgePadding = 16.dp) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = secondaryIndex == index,
                    onClick = { secondaryIndex = index },
                    text = { Text(title) }
                )
            }
        }
    }
}
