# Material Design Tasks

> **Project:** Material Design
>
> **Version:** 2.0.5
>
> **Last Updated:** 2026-08-22

---

## Android Reference App

- [x] Replace the simulated Play Store content with Explore, Catalog, APIs, and Foundations surfaces.
- [x] Add validated offline catalog metadata, canonical aliases, deterministic search, and source-path references.
- [x] Split the app into core, feature, and domain-grouped sample modules.
- [x] Add local bookmarks, recent history, theme, dynamic-color, and reduced-motion settings.
- [x] Add working Buttons, Split buttons, Floating action buttons, Progress indicators, Segmented list items, Carousels, Floating toolbars, and Typography references.
- [x] Add complete searchable design guidance for every working reference: purpose, use and avoid rules, behavior, accessibility, and adaptation.
- [x] Add Button groups, Toggle buttons, Cards, Lists, Chips, Segmented buttons, and Selection controls as independently implemented references.
- [x] Restore the standalone app's applicable expressive motion language through shared, reduced-motion-aware spring specifications.
- [x] Add unit, UI, accessibility, and adaptive layout checks.
- [x] Adopt official Material 3 Expressive theming in the app shell: `MaterialExpressiveTheme`, `expressiveLightColorScheme`, and `MotionScheme` with a snap-based reduced-motion scheme sourced from `ExpressiveMotion.kt`.
- [x] Replace the shell search field with a docked M3 SearchBar that expands into deterministic results, base adaptive navigation on window width size classes, and measure compact bottom padding instead of hardcoding it.

## Component Backlog

Add an item to the app only after the implementation, interactive demo, official API metadata, search aliases, adaptive behavior, accessibility checks, and source validation are complete.

- [x] Button groups and toggle buttons
- [x] Cards and standard list items
- [x] Chips and segmented buttons
- [x] Checkboxes, radio buttons, switches, and parent selection state
- [x] Text fields and search
- [x] Navigation bars, rails, drawers, and tabs
- [x] Sliders and Range sliders
- [x] Date pickers and Time pickers
- [x] Badges and BadgedBox indicators
- [x] Visual badges for M3 Expressive, Experimental APIs, and Custom Components
- [x] Dialogs, sheets, snackbars, tooltips, and menus
- [x] Color, shape, motion, elevation, layout, and accessibility foundation inspectors
- [x] Icon buttons and icon toggles (standard, filled, tonal, outlined)
- [x] FAB menu with ToggleFloatingActionButton anchor
- [x] Loading indicators (`LoadingIndicator`, `ContainedLoadingIndicator`)
- [x] Pull to refresh (`PullToRefreshBox`)
- [x] Bottom app bars

Remaining pinned-artifact gaps from the 2.0.5 coverage audit (~320 of ~370 public material3 `1.5.0-alpha23` symbols still uncatalogued), highest value first:

- [ ] Side sheets (`ModalSideSheet` is not available in `1.5.0-alpha23`; track the artifact before implementing)
- [ ] Flexible bottom app bars (`FlexibleBottomAppBar`)
- [ ] Wide navigation rail family (`WideNavigationRail`, items, colors, state)
- [ ] Swipe-to-dismiss (`SwipeToDismissBox` and state/value APIs)
- [ ] Short navigation bar variants (`ShortNavigationBar`, defaults)
- [ ] Vertical sliders (`VerticalSlider`)
- [ ] Full-screen and top search bar variants (`TopSearchBar`, expanded full-screen search bars)
- [ ] Time picker dialogs (`TimePickerDialog`, `RichTimePickerDialog`) as distinct references
- [ ] Scrollable tab row variants (`PrimaryScrollableTabRow`, `SecondaryScrollableTabRow`)
- [ ] Grouped dropdown menus (`DropdownMenuGroup`, group labels)
- [ ] Basic alert dialogs (`BasicAlertDialog`) and dividers (horizontal/vertical)
- [ ] Hero carousel strategy (`HorizontalCenteredHeroCarousel`)
- [ ] Scaffold as a foundations-layout reference and remaining Defaults/state objects per covered component

## Web

- [x] Keep the responsive 1.5.0 showcase available as the browser implementation.
- [ ] Develop the independent Material Web implementation when explicitly prioritized.
