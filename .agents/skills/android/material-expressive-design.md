# Material 3 Expressive Design for Android

How the Material Design app builds a complete Material 3 Expressive product in Jetpack Compose, and how to build one the same way. Every rule here is implemented in `material-app/core/designsystem` and `material-app/core/ui`; read those files for working code.

Use judgment. The goal is a coherent, beautiful app, not a checklist. When a pattern does not suit a screen, adapt it and say why.

## Principles

1. **Expressive, not decorated.** Shape, size, color, and motion carry meaning: a larger shape for the main action, connected shapes for grouped choices, a spring when something responds to touch. Nothing is ornamental.
2. **Tonal surfaces, not shadows.** Pages sit on `surfaceContainer`; rows sit on `surfaceBright`; groups of rows connect with 2 dp gaps. Elevation shadows are rare.
3. **Everything rounded, nothing default.** Rows, groups, fields, buttons, sheets, and menus use the shape tokens. Material's 4 dp default corners never ship.
4. **One design system.** Screens use the shared components. A screen that needs something new adds it to the design system, so every screen gets it.
5. **Readable in every theme.** Check light, dark, and pure black. Text on a colored container uses the matching `on*` color.
6. **Respect the person.** Haptics and motion follow their settings. Nothing flashes, nothing jumps, and loading never looks like empty.
7. **Plain words.** Short labels that fit their buttons; sentences that say what happens.

## 1. Theme

`MaterialDesignTheme` wraps `MaterialExpressiveTheme` with a scheme, typography, shapes, and motion scheme from `ThemeState`.

- **Modes:** System, Light, or Dark, plus a **pure black** option that applies in dark mode. Offer the modes as connected choices and pure black as a toggle row.
- **Color source:** wallpaper colors on Android 12+ (`dynamicLightColorScheme` / `dynamicDarkColorScheme`), a named **preset** seed, or a **custom** seed. Below Android 12, wallpaper falls back to the preset.
- **Generated schemes:** seeds become full schemes through Material Color Utilities (MaterialKolor's `SchemeTonalSpot`, or `SchemeMonochrome` for a neutral preset), so every preset keeps proper tonal palettes and contrast in light, dark, and pure black. Never hand-pick a whole scheme.
- **Pure black (`toPureBlack()`):** background, surface, and every container up to `surfaceContainer` become black; rows (`surfaceBright`) and raised parts step up in small neutral grays so they still read as separate.
- **Motion scheme:** `MotionScheme.expressive()`, or a snapping scheme when Reduce motion is on.
- **Composition locals:** `LocalReducedMotion`, `LocalHapticsEnabled`, `LocalShowBadges`, and `LocalSpacing` (read as `MaterialTheme.spacing`).
- **Splash:** keep the splash screen until stored settings load, with a short ceiling, so the first frame never uses the wrong colors.
- **Edge to edge** everywhere; each screen handles insets through its scaffold.

### Color roles

| Use | Role |
| :--- | :--- |
| Page behind everything | `surfaceContainer` |
| Rows and content blocks | `surfaceBright` |
| Raised parts inside a row (tags, swatch rings) | `surfaceContainerHigh` / `Highest` |
| Main action | `primary` / `onPrimary` |
| Secondary actions, selected states | `secondaryContainer` / `primaryContainer` |
| Section headers | `primary` text, `titleSmall` |
| Supporting text | `onSurfaceVariant` |
| Gentle emphasis, not errors | `tertiary` |
| Errors and destructive actions | `error` / `onError` |

Decorative tints, such as icon badges, come from a fixed pastel palette hashed by key (`BadgePalette`): a pastel circle with a deep icon in light themes, a deep circle with the pastel icon in dark themes. The same key always gets the same color.

## 2. Typography

One variable font, **Google Sans Flex**, with weights as instances of the same file, on the Material type scale (`AppTypography`). Use the scale, never raw sizes.

- `headlineMedium` / `headlineSmall`: hero titles, such as the About header.
- `titleLarge`: a sheet's main line; `titleMedium`: emphasized row titles.
- `bodyLarge`: row titles and paragraphs; `bodyMedium`: supporting text; `bodySmall`: fine print.
- `labelLarge`: buttons and small headers; `labelMedium` / `labelSmall`: metadata and tags.
- `MetricTextStyle` for counts and measurements (tabular figures); `CodeTextStyle` for API symbols and paths.

Large titles come from `LargeFlexibleTopAppBar`; do not add a second big title inside the page. Layouts must survive 200% font scale without clipping.

## 3. Shape, spacing, and grouping

- **Shapes (`AppShapes`):** `small 12`, `medium 16`, `large 20`, `extraLarge 28` dp.
- **Connected groups:** outer corner 24 dp, inner corner 6 dp, 2 dp gap between rows (`connectedRowShape(index, count)`). The page shows through the gap as a hairline.
- **Text fields:** `FieldShape` (16 dp) on every field; search fields are pills (`CircleShape`).
- **Expressive shapes:** `MaterialShapes` (`Cookie9Sided` and others) via `toShape()` for one hero element per screen, and for the selected swatch in a picker.
- **Spacing:** a 4 dp grid through `MaterialTheme.spacing` (`screenGutter`, `rowHorizontal`, `rowVertical`, `sectionGap`, `toolbarClearance`). Screens use these tokens, not literal numbers.

## 4. Components

All of these live in `core/designsystem/components`. Screens use them before writing their own.

- **`AppScaffold`:** every screen. A `LargeFlexibleTopAppBar` (title and optional subtitle) that collapses on scroll, on `surfaceContainer`, with content padding that already includes the side gutter and the clearance under the floating bar. Top-bar actions are `IconButton(shapes = IconButtonDefaults.shapes())`, two at most.
- **`FloatingTabBar`:** a floating pill at the bottom instead of a navigation bar. It shows the tabs on top-level pages and morphs into Back plus the page title on every other page.
- **`ConnectedGroup` / `ConnectedItem` / `ConnectedRow`:** any list of options or items. Use `ConnectedItem(index, count)` per item in a long lazy list.
- **`SectionHeader`:** a `primary` `titleSmall` header above a group, inset to line up with row text.
- **`NavigationRow`:** a row with an `IconBadge` that opens another page.
- **`ToggleRow` with `ExpressiveSwitch`:** every on/off setting; the whole row toggles. Never the plain `Switch`.
- **`ChoiceRow` / `ConnectedChoices`:** two to five short options as connected `ToggleButton`s (`ButtonGroupDefaults` connected shapes). The selected option morphs round.
- **`AppMenu`:** the Expressive menu (`DropdownMenuPopup`, `DropdownMenuGroup` with vibrant colors, `MenuDefaults` item shapes), opened by long-press on the item.
- **`AppBottomSheet`:** quick options for a tool and one item's details; long or important settings go on a full page.
- **`InlineSearchField`:** a pill search field on `surfaceBright` with a clear button.
- **`IconBadge`, `EntryRow`, `TagLabel` / `TagRow`:** leading badges, list rows with summaries, and small maturity tags whose meaning is in the label, never the color alone.
- **`LoadingState` / `EmptyState`:** `LoadingIndicator` while data loads; an icon on a soft Expressive shape with a title, a sentence, and an optional action when there is nothing to show.

Buttons always pass `shapes = ButtonDefaults.shapes()` (or the icon and toggleable variants) so they morph when pressed. Destructive actions confirm in a dialog that names what happens.

## 5. Navigation and adaptive layout

- **Compact and medium windows:** the top-level destinations share the `FloatingTabBar`. Search is its own full-screen page.
- **Expanded windows:** a `WideNavigationRail` (collapsed to icons, or expanded to labels, with the main action in its header) replaces the floating bar at the top level, and `NavigableListDetailPaneScaffold` keeps the list beside the open item with Material's pane motion and predictive back.
- **Window size:** read `currentWindowAdaptiveInfoV2()`, never device names or orientation.
- **Transitions:** destinations slide and fade with `MaterialTheme.motionScheme.defaultSpatialSpec()` and `defaultEffectsSpec()`. Navigation Compose uses the pop transitions for predictive back, so the gesture and the button feel the same. Tabs fade through and keep their scroll state with a saveable state holder.
- **Shared elements:** wrap the navigation graph in `SharedTransitionLayout`. An element that appears in both the list and the opened page (the app uses an item's badge) carries the same key through `sharedBounds`. Keys must be unique on a page, so turn sharing off where one item can appear twice.

## 6. Motion and haptics

- Springs for anything that responds to touch, slightly under-damped for the Expressive bounce. Prefer the motion scheme's specs; project-specific springs live in one place (`ExpressiveMotion.kt`) and are named for their job: press feedback, spatial movement, settling, progress, and rejection.
- Official components keep their own Material physics; project motion complements them and never overrides them.
- **Reduce motion:** decorative movement and fades snap, while state changes, callbacks, meaning, and safety timing (such as hold-to-confirm) stay. Every custom animation checks `LocalReducedMotion`.
- Animations tolerate interruption, rapid input, disposal, and zero duration without stale jobs or transformed content.
- **Haptics** go through `rememberAppHaptics()` (`tap`, `longPress`, `toggle`, `tick`, `confirm`), which does nothing when haptics are off. Rows play `tap` themselves; switches play `toggle`. Never call `performHapticFeedback` from a screen.

## 7. Official API coverage

A reference must cover the official API, not only the components someone remembered. The repository tracks every public symbol of the pinned Material 3 and Material 3 Adaptive artifacts in `core/catalog/api/` (generated inventory, coverage ledger, and `COVERAGE.md`). After a dependency bump, regenerate the inventory, reconcile the ledger, and triage every newly published symbol before the build passes. See `material-design-development.md` for the workflow.

## 8. Quality checklist

- [ ] Every screen uses `AppScaffold`, with a large flexible top bar and the page on `surfaceContainer`.
- [ ] Lists of options are connected groups; navigational rows lead with a colored badge.
- [ ] Toggles are `ToggleRow` with `ExpressiveSwitch`; one-of-a-few choices are connected toggle buttons.
- [ ] Every text field passes `FieldShape`; search fields are pills.
- [ ] Every button and icon button morphs when pressed.
- [ ] Menus are `AppMenu`, opened by long-press.
- [ ] Loading uses `LoadingIndicator`; empty and error states use `EmptyState`; loading never flashes empty.
- [ ] Expanded windows get the rail and list-detail panes; nothing assumes a phone.
- [ ] Haptics through `rememberAppHaptics()`; every animation honors Reduce motion.
- [ ] Strings in resources; content descriptions on icon-only buttons; decorative icons pass `null`.
- [ ] Light, dark, and pure black checked at the smallest and largest font sizes.
