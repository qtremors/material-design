package dev.qtremors.material.samples.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolbarsSample(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }
    var vertical by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val reducedMotion = LocalReducedMotion.current
    val actions = listOf(
        Icons.Default.Search to "Search",
        Icons.Default.Favorite to "Favorite",
        Icons.Default.Edit to "Edit",
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Expanded")
            Switch(checked = expanded, onCheckedChange = { expanded = it })
        }
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Vertical")
            Switch(checked = vertical, onCheckedChange = { vertical = it })
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp).padding(16.dp),
            contentAlignment = if (vertical) Alignment.CenterEnd else Alignment.BottomCenter,
        ) {
            if (vertical) {
                VerticalFloatingToolbar(
                    expanded = expanded,
                    modifier = (if (reducedMotion) Modifier else Modifier.animateContentSize()).width(64.dp),
                    shape = RoundedCornerShape(100),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                        toolbarContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    ToolbarActions(actions, selectedIndex) { selectedIndex = it }
                }
            } else {
                HorizontalFloatingToolbar(
                    expanded = expanded,
                    modifier = (if (reducedMotion) Modifier else Modifier.animateContentSize()).height(64.dp),
                    shape = RoundedCornerShape(100),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                        toolbarContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    ToolbarActions(actions, selectedIndex) { selectedIndex = it }
                }
            }
        }
    }
}

@Composable
private fun ToolbarActions(
    actions: List<Pair<androidx.compose.ui.graphics.vector.ImageVector, String>>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    actions.forEachIndexed { index, (icon, label) ->
        val tint = if (selectedIndex == index) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            }
        IconButton(onClick = { onSelected(index) }) {
            Icon(icon, contentDescription = label, tint = tint)
        }
    }
}
