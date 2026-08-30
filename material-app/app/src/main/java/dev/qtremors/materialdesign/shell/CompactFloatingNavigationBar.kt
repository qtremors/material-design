package dev.qtremors.materialdesign.shell

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.expressiveSpring
import dev.qtremors.materialdesign.R
import dev.qtremors.materialdesign.RootDestination

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun CompactFloatingNavigationBar(
    selected: RootDestination,
    onSelected: (RootDestination) -> Unit,
    onToolbarHeightChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reducedMotion = LocalReducedMotion.current

    HorizontalFloatingToolbar(
        expanded = true,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
            toolbarContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            toolbarContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier
            .onSizeChanged { size -> onToolbarHeightChanged(size.height.toFloat()) }
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            rootDestinations.forEach { item ->
                CompactExpressiveDestinationPill(
                    item = item,
                    selected = selected == item.destination,
                    reducedMotion = reducedMotion,
                    onClick = { onSelected(item.destination) },
                )
            }
        }
    }
}

@Composable
private fun CompactExpressiveDestinationPill(
    item: RootDestinationItem,
    selected: Boolean,
    reducedMotion: Boolean,
    onClick: () -> Unit,
) {
    val targetRadius = if (selected) 28.dp else 12.dp
    val animatedCornerRadius by animateDpAsState(
        targetValue = targetRadius,
        animationSpec = expressiveSpring(),
        label = "navPillCornerRadius",
    )
    val label = stringResource(item.labelRes)

    Surface(
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(animatedCornerRadius),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        // Always expose the destination label to accessibility services, even while
        // the visible label is hidden behind AnimatedVisibility for unselected pills.
        modifier = Modifier
            .height(48.dp)
            .semantics { contentDescription = label },
    ) {
        Row(
            modifier = Modifier
                .animateContentSize(animationSpec = expressiveSpring())
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            AnimatedVisibility(
                visible = selected,
                enter = if (reducedMotion) EnterTransition.None else fadeIn(expressiveSpring()) + expandHorizontally(expressiveSpring()),
                exit = if (reducedMotion) ExitTransition.None else fadeOut(expressiveSpring()) + shrinkHorizontally(expressiveSpring()),
            ) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

internal val rootDestinations = listOf(
    RootDestinationItem(RootDestination.EXPLORE, R.string.app_destination_explore, Icons.Default.Explore),
    RootDestinationItem(RootDestination.CATALOG, R.string.app_destination_catalog, Icons.Default.Apps),
    RootDestinationItem(RootDestination.APIS, R.string.app_destination_apis, Icons.Default.Api),
    RootDestinationItem(RootDestination.FOUNDATIONS, R.string.app_destination_foundations, Icons.Default.Palette),
)

internal data class RootDestinationItem(
    val destination: RootDestination,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
)
