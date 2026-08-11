package dev.qtremors.material.samples.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
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

@Composable
fun NavigationBarSample(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Search", "Profile", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Person, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Person, Icons.Outlined.Settings)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Navigation Bar (Bottom Navigation)", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Box(Modifier.padding(16.dp)) {
                Text("Selected screen: ${items[selectedItem]}", style = MaterialTheme.typography.bodyMedium)
            }
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            if (index == 1) {
                                BadgedBox(badge = { Badge { Text("3") } }) {
                                    Icon(
                                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                        contentDescription = item
                                    )
                                }
                            } else {
                                Icon(
                                    if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                    contentDescription = item
                                )
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationRailSample(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Search", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Settings)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Navigation Rail", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth().height(260.dp)) {
            Row(Modifier.fillMaxSize()) {
                NavigationRail(
                    header = {
                        FloatingActionButton(onClick = {}) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) {
                    Spacer(Modifier.height(8.dp))
                    items.forEachIndexed { index, item ->
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
                Box(
                    Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Rail Destination: ${items[selectedItem]}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun NavigationDrawerSample(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf("Home", "Search", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Settings)

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Modal Navigation Drawer", style = MaterialTheme.typography.titleMedium)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(Modifier.width(280.dp)) {
                    Text(
                        "App Navigation",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = { Icon(icons[index], contentDescription = null) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        ) {
            Card(Modifier.fillMaxWidth().height(180.dp)) {
                Column(
                    Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Selected: ${items[selectedItem]}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                    }
                }
            }
        }
    }
}

@Composable
fun TabsSample(modifier: Modifier = Modifier) {
    var primaryIndex by remember { mutableIntStateOf(0) }
    var secondaryIndex by remember { mutableIntStateOf(0) }
    var scrollableIndex by remember { mutableIntStateOf(0) }

    val primaryTabs = listOf("Overview", "Details", "Reviews")
    val secondaryTabs = listOf("Tab 1", "Tab 2", "Tab 3")
    val scrollableTabs = listOf("All", "News", "Videos", "Images", "Shopping", "Maps", "Books", "Flights")

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Primary Tabs", style = MaterialTheme.typography.titleMedium)
        PrimaryTabRow(selectedTabIndex = primaryIndex) {
            primaryTabs.forEachIndexed { index, title ->
                Tab(
                    selected = primaryIndex == index,
                    onClick = { primaryIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Text("Secondary Tabs", style = MaterialTheme.typography.titleMedium)
        SecondaryTabRow(selectedTabIndex = secondaryIndex) {
            secondaryTabs.forEachIndexed { index, title ->
                Tab(
                    selected = secondaryIndex == index,
                    onClick = { secondaryIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Text("Scrollable Tabs", style = MaterialTheme.typography.titleMedium)
        ScrollableTabRow(selectedTabIndex = scrollableIndex, edgePadding = 16.dp) {
            scrollableTabs.forEachIndexed { index, title ->
                Tab(
                    selected = scrollableIndex == index,
                    onClick = { scrollableIndex = index },
                    text = { Text(title) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarsSample(modifier: Modifier = Modifier) {
    var selectedBar by remember { mutableIntStateOf(0) }
    val barTypes = listOf("Small", "Center", "Medium", "Large")

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Top App Bars", style = MaterialTheme.typography.titleMedium)
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            barTypes.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = selectedBar == index,
                    onClick = { selectedBar = index },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = barTypes.size)
                ) {
                    Text(label)
                }
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Box(Modifier.padding(4.dp)) {
                when (selectedBar) {
                    0 -> TopAppBar(
                        title = { Text("Small TopAppBar") },
                        navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu") } },
                        actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") } }
                    )
                    1 -> CenterAlignedTopAppBar(
                        title = { Text("Center Aligned") },
                        navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu") } },
                        actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") } }
                    )
                    2 -> MediumTopAppBar(
                        title = { Text("Medium TopAppBar") },
                        navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu") } },
                        actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") } }
                    )
                    3 -> LargeTopAppBar(
                        title = { Text("Large TopAppBar") },
                        navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu") } },
                        actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") } }
                    )
                }
            }
        }
    }
}
