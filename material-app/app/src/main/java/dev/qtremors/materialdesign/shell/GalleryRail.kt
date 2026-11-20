package dev.qtremors.materialdesign.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.materialdesign.R
import dev.qtremors.materialdesign.RootDestination
import kotlinx.coroutines.launch

/**
 * Navigation for expanded windows: Material's wide navigation rail, collapsed to icons with the
 * selected label, or expanded to every label. Search sits in the rail header as its main action.
 */
@Composable
internal fun GalleryRail(
    selected: RootDestination,
    onSelect: (RootDestination) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val haptics = rememberAppHaptics()
    val expanded = state.targetValue == WideNavigationRailValue.Expanded

    WideNavigationRail(
        state = state,
        modifier = modifier,
        header = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12),
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium),
            ) {
                IconButton(
                    onClick = {
                        haptics.tap()
                        scope.launch { state.toggle() }
                    },
                    shapes = IconButtonDefaults.shapes(),
                ) {
                    Icon(
                        if (expanded) Icons.AutoMirrored.Rounded.MenuOpen else Icons.Rounded.Menu,
                        contentDescription = stringResource(if (expanded) R.string.app_rail_collapse else R.string.app_rail_expand),
                    )
                }
                FloatingActionButton(
                    onClick = {
                        haptics.tap()
                        onSearch()
                    },
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                ) {
                    Icon(Icons.Rounded.Search, contentDescription = stringResource(R.string.app_cd_search_components))
                }
            }
        },
    ) {
        RootTabs.forEach { tab ->
            val label = stringResource(tab.labelRes)
            WideNavigationRailItem(
                selected = tab.destination == selected,
                onClick = {
                    haptics.tap()
                    onSelect(tab.destination)
                },
                icon = { Icon(tab.icon, contentDescription = if (expanded) null else label) },
                label = { Text(label) },
                railExpanded = expanded,
            )
        }
    }
}
