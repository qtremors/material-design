package dev.qtremors.material.core.designsystem.components

import androidx.compose.material3.IconButtonDefaults
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import dev.qtremors.material.core.designsystem.R
import dev.qtremors.material.core.designsystem.spacing

/**
 * A search button in the top bar that turns the bar itself into a search field for this page.
 * [onOpenChange] tells the page when the field opens and closes, so it can swap its content.
 */
data class FilterTop(
    val query: String,
    val onQueryChange: (String) -> Unit,
    val placeholder: String,
    val onOpenChange: (Boolean) -> Unit = {}
)

/**
 * The frame of every screen: a large title that collapses on scroll, the tonal page background,
 * and content padding that keeps the last row clear of the floating toolbar.
 *
 * [filter] adds a search button that turns the top bar into a search field for this page.
 * [content] receives padding that already includes the side gutter and toolbar clearance.
 */
@Composable
fun AppScaffold(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    snackbarHost: @Composable () -> Unit = {},
    filter: FilterTop? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val spacing = MaterialTheme.spacing
    val layoutDirection = LocalLayoutDirection.current
    var filtering by rememberSaveable { mutableStateOf(false) }
    // The keyboard opens once, when search opens. Coming back from a result keeps the results
    // in view instead of popping the keyboard up over them again.
    var focusPending by rememberSaveable { mutableStateOf(false) }
    val closeFilter: () -> Unit = {
        filtering = false
        focusPending = false
        filter?.onQueryChange?.invoke("")
        filter?.onOpenChange?.invoke(false)
    }
    BackHandler(enabled = filtering, onBack = closeFilter)
    Scaffold(
        modifier = if (filtering) modifier else modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.surfaceContainer,
        snackbarHost = snackbarHost,
        topBar = {
            when {
                filter != null -> AnimatedContent(targetState = filtering, label = "topBarFilter") { open ->
                    if (open) FilterTopBar(filter, onClose = closeFilter, requestFocus = focusPending, onFocused = { focusPending = false })
                    else TitleTopBar(title, subtitle, scrollBehavior) {
                        IconButton(shapes = IconButtonDefaults.shapes(), onClick = {
                            filtering = true
                            focusPending = true
                            filter.onOpenChange(true)
                        }) {
                            Icon(Icons.Rounded.Search, contentDescription = stringResource(R.string.action_search))
                        }
                        actions()
                    }
                }
                else -> TitleTopBar(title, subtitle, scrollBehavior, actions)
            }
        }
    ) { inner ->
        content(
            PaddingValues(
                start = inner.calculateStartPadding(layoutDirection) + spacing.screenGutter,
                end = inner.calculateEndPadding(layoutDirection) + spacing.screenGutter,
                top = inner.calculateTopPadding(),
                bottom = inner.calculateBottomPadding() + spacing.toolbarClearance
            )
        )
    }
}

@Composable
private fun TitleTopBar(
    title: String,
    subtitle: String?,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: @Composable RowScope.() -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    // The Expressive large bar: a bigger title that settles into the small bar as the page scrolls.
    LargeFlexibleTopAppBar(
        title = { Text(title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        subtitle = subtitle?.let { { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) } },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.surfaceContainer,
            scrolledContainerColor = colors.surfaceContainer
        )
    )
}


/** The top bar as a search field: back closes it and clears the query. */
@Composable
private fun FilterTopBar(filter: FilterTop, onClose: () -> Unit, requestFocus: Boolean, onFocused: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val focus = remember { FocusRequester() }
    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            focus.requestFocus()
            onFocused()
        }
    }
    TopAppBar(
        navigationIcon = {
            IconButton(shapes = IconButtonDefaults.shapes(), onClick = onClose) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.action_close_search))
            }
        },
        title = {
            TextField(
                value = filter.query,
                onValueChange = filter.onQueryChange,
                singleLine = true,
                placeholder = { Text(filter.placeholder, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                trailingIcon = {
                    if (filter.query.isNotEmpty()) {
                        IconButton(shapes = IconButtonDefaults.shapes(), onClick = { filter.onQueryChange("") }) {
                            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.action_clear_search))
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colors.surfaceContainer,
                    unfocusedContainerColor = colors.surfaceContainer,
                    focusedIndicatorColor = colors.surfaceContainer,
                    unfocusedIndicatorColor = colors.surfaceContainer
                ),
                modifier = Modifier.fillMaxWidth().focusRequester(focus)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surfaceContainer)
    )
}
