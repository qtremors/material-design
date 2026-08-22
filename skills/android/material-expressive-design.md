# Material 3 Expressive Design for Android

Use this skill when designing or implementing Material 3 Expressive and Adaptive UI in `material-app/`.

## Sources of Truth

Use these sources in order:

1. The repository's pinned dependency catalog and compiling samples.
2. The app catalog's reviewed API metadata and source paths.
3. Current [Material Design guidance](https://m3.material.io/).
4. Current [Compose Material 3 release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3).
5. Current [Android adaptive layout guidance](https://developer.android.com/develop/adaptive-apps/guides/use-window-size-classes).

The project pins `androidx.compose.material3:material3:1.5.0-alpha23`. Alpha signatures and opt-in requirements can change. Inspect the pinned sources or let the compiler determine required opt-ins; do not copy signatures from a different release.

Label custom behavior **Project implementation**. Never present a project experiment as an official Material API.

## Dynamic Typography

- Use variable-font axes only when the bundled font supports them.
- Clamp each axis to the font's supported range.
- Treat axis animation as progressive enhancement.
- Preserve readable line breaks, text scaling, and reduced-motion behavior.
- Keep semantic hierarchy stable even when visual weight, width, optical size, grade, slant, or roundness changes.

Maintained reference:

`material-app/samples/foundations/src/main/java/dev/qtremors/material/samples/foundations/FoundationSamples.kt`

## Wavy Progress Indicators

Use `LinearWavyProgressIndicator` and `CircularWavyProgressIndicator` for progress, not decoration.

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressReference(progress: Float) {
    LinearWavyProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier.fillMaxWidth(),
    )

    CircularWavyProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
    )
}
```

- Keep determinate values in `0f..1f`.
- Distinguish determinate and indeterminate behavior.
- Preserve progress semantics and meaningful status text.
- Reduce decorative wave motion when reduced motion is enabled.
- Do not recreate an official indicator on `Canvas` unless a documented requirement cannot be met by the official API.

Maintained reference:

`material-app/samples/communication/src/main/java/dev/qtremors/material/samples/communication/ProgressSamples.kt`

## Split Buttons

Use `SplitButtonLayout` for one primary action and a separate trigger for closely related alternatives.

- Both segments must be independently focusable and operable.
- Give an icon-only trailing action an accessible description.
- Keep menu expanded state hoisted.
- Do not make unrelated actions look like one split button.

Maintained reference:

`material-app/samples/actions/src/main/java/dev/qtremors/material/samples/actions/ActionSamples.kt`

## Morphing and Hold Confirmation

These are project behaviors, not standalone official APIs.

- Source explicit spring values from `ExpressiveMotion.kt`.
- Keep press feedback brief and reversible.
- Cancel hold progress on release, disposal, disablement, or competing input.
- Announce completion and expose progress when meaningful.
- Preserve the safety interval under reduced motion.
- Provide a non-hold alternative when the task or accessibility context requires one.

Maintained references:

- `material-app/samples/actions/src/main/java/dev/qtremors/material/samples/actions/ActionSamples.kt`
- `material-app/core/designsystem/src/main/java/dev/qtremors/material/core/designsystem/ExpressiveMotion.kt`

## Segmented Lists and Grouped Shapes

Use the official **Segmented list items** name for the gallery entry.

- Keep single-select and multi-select semantics distinct.
- Maintain focus visibility across joined shapes.
- Do not communicate grouping or selection by shape or color alone.
- Keep list content readable at increased font sizes.

Maintained reference:

`material-app/samples/containment/src/main/java/dev/qtremors/material/samples/containment/CardAndListSamples.kt`

## Floating Toolbars and Adaptive Navigation

- Choose horizontal or vertical floating toolbars from available space and task flow.
- Keep every action reachable by touch, keyboard, and accessibility services.
- Avoid covering focused or primary content.
- Respect safe areas and compact-height windows.
- Preserve user state when navigation presentation changes.
- Base adaptive choices on the current window, not device labels.

Maintained references:

- `material-app/samples/navigation/src/main/java/dev/qtremors/material/samples/navigation/NavigationSamples.kt`
- `material-app/app/src/main/java/dev/qtremors/materialdesign/MaterialGalleryApp.kt`

## Expressive Theming and Motion Scheme

The app shell themes through `MaterialExpressiveTheme` with `expressiveLightColorScheme()` for the static light fallback, `darkColorScheme()` for dark, and dynamic color on Android 12+.

- Resolve motion through `ExpressiveMotion.motionScheme(reducedMotion)`: the official `MotionScheme.expressive()` by default, a snap-based scheme when reduced motion is on.
- Do not construct ad-hoc `MaterialTheme` wrappers in feature or sample modules; theme once at the shell.
- Dynamic color has no expressive variant; keep the official dynamic schemes as-is and preserve role relationships.
- The pinned library already carries the expressive type scale, including emphasized styles; do not override typography without a documented reason.

Maintained references:

- `material-app/core/designsystem/src/main/java/dev/qtremors/material/core/designsystem/MaterialDesignTheme.kt`
- `material-app/core/designsystem/src/main/java/dev/qtremors/material/core/designsystem/ExpressiveMotion.kt`

## Review Checklist

- Official and project APIs are clearly distinguished.
- Code matches the pinned dependency version.
- State is hoisted and survives relevant adaptive changes.
- Semantics describe name, role, value, state, and action.
- Touch targets, focus order, and increased-font layouts remain usable.
- Motion is interruptible and has a reduced-motion treatment.
- Compact, medium, expanded, large, extra-large, and compact-height cases are considered where relevant.
- The corresponding catalog record, tests, HTML skill, Markdown skill, and changelog stay aligned.
