package dev.qtremors.material.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.qtremors.material.core.catalog.ApiAvailability
import dev.qtremors.material.core.catalog.ApiReference
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.ImplementationKind
import dev.qtremors.material.core.designsystem.components.EntryRow
import dev.qtremors.material.core.designsystem.components.Tag
import dev.qtremors.material.core.designsystem.components.TagTone

/** True when the entry belongs to the Material 3 Expressive collection. */
val CatalogEntry.isExpressive: Boolean get() = "expressive" in collections

/** True when any cited API needs an opt-in. */
val CatalogEntry.isExperimental: Boolean get() = apiReferences.any { it.stability == ApiStability.EXPERIMENTAL }

/**
 * The tags an entry shows in lists and headers: what it is (official API or project
 * implementation) only when that is notable, then whether it is Expressive and experimental.
 */
@Composable
fun entryTags(entry: CatalogEntry): List<Tag> = buildList {
    if (entry.implementation == ImplementationKind.PROJECT_IMPLEMENTATION) {
        add(Tag(stringResource(R.string.tag_project), TagTone.TERTIARY))
    }
    if (entry.isExpressive) add(Tag(stringResource(R.string.tag_expressive), TagTone.PRIMARY))
    if (entry.isExperimental) add(Tag(stringResource(R.string.tag_experimental), TagTone.ERROR))
}

/** Stability and availability of one API symbol, always both, so neither is inferred from the other. */
@Composable
fun apiTags(api: ApiReference): List<Tag> = listOf(
    if (api.stability == ApiStability.EXPERIMENTAL) {
        Tag(stringResource(R.string.tag_experimental), TagTone.ERROR)
    } else {
        Tag(stringResource(R.string.tag_stable), TagTone.NEUTRAL)
    },
    if (api.availability == ApiAvailability.ALPHA_ONLY) {
        Tag(stringResource(R.string.tag_pre_release), TagTone.TERTIARY)
    } else {
        Tag(stringResource(R.string.tag_stable_release), TagTone.NEUTRAL)
    },
)

/** A catalog entry as a connected row, with its category badge, summary, and tags. */
@Composable
fun CatalogEntryRow(
    entry: CatalogEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    showSummary: Boolean = true,
) {
    EntryRow(
        title = entry.officialName,
        category = entry.category,
        supporting = if (showSummary) entry.summary else null,
        tags = entryTags(entry),
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
    )
}
