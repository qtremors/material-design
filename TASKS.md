# Material Design Tasks

> **Project:** Material Design
>
> **Last Updated:** 2026-08-30

---

## Status

| Category | Remaining |
| :--- | ---: |
| Android Audit Backlog | 0 |
| Component Backlog | 1 (blocked on artifact) |
| **Total Open Tasks** | **1** |

---

## Remaining Work

### Android Audit Backlog (`material-app`)

- [x] **I18N-0001 - Extract all hardcoded UI strings into string resources with plurals** `[High]` `[I18N / Accessibility]`
  - **Location:** `material-app/app/src/main/java/dev/qtremors/materialdesign/shell/*.kt`; `material-app/feature/explore/src/main/java/dev/qtremors/material/feature/explore/ExploreScreen.kt`; `material-app/feature/settings/src/main/java/dev/qtremors/material/feature/settings/SettingsScreen.kt`
  - **Problem:** Zero string resources exist—no `values/strings.xml`, no plurals; every user-visible string is inline English, including interpolated counts where English plural rules are baked in, despite `android:supportsRtl="true"` signaling i18n intent.
  - **Impact:** Localization impossible; plural rules wrong for non-English locales; copy inconsistencies harder to audit.
  - **Fix:** Create per-module string resources, convert count strings to plurals, replace literal titles/labels/descriptions with `stringResource` calls; document that catalog-content strings sourced from JSON are out of scope.
  - **Verification:** Lint hardcoded-text check passes on app and feature modules; a pseudo-locale build renders all shell/settings/search surfaces without literals.
  - **Resolution (2.0.8):** Added per-module `strings.xml` for app, explore, catalog, apis, foundations, detail, and settings; search-result and catalog-count strings converted to plurals; shell, search overlay, gallery screens, detail tabs/badges, and settings/about/theme selectors now resolve via `stringResource`/`pluralStringResource`. Catalog JSON content remains out of scope by design.

- [x] **BUILD-0002 - Sixteen modules repeat identical Gradle boilerplate with unused declared tooling** `[Low]` `[Build / Release]`
  - **Location:** `material-app/*/build.gradle.kts` (all modules); `material-app/gradle/libs.versions.toml`
  - **Problem:** Every module re-declares namespace/compileSdk 37/minSdk 24/JVM 11 with no convention plugins; compile options and dependencies are duplicated across submodules.
  - **Impact:** Configuration drift risk on SDK/version bumps across 16 modules.
  - **Fix:** Introduce convention plugins (build-logic) for android library/compose/serialization setup and standardize compile options centrally.
  - **Verification:** A version bump touches one central location; `./gradlew build` succeeds across all modules with identical effective config verified via dependency insight.
  - **Resolution (2.0.8):** Introduced `build-logic` convention plugins (`materialdesign.android.application`, `.library`, `.compose`, `.kotlin.serialization`) centralizing compileSdk 37, minSdk 24, JVM 11 options, derived namespaces, and Compose enablement; all 15 library modules plus the app now consume the conventions.

- [x] **SEC-0001 - Backup and data-extraction rules left as template defaults while allowBackup is enabled** `[Low]` `[Security / Privacy]`
  - **Location:** `material-app/app/src/main/AndroidManifest.xml`; `material-app/app/src/main/res/xml/backup_rules.xml`; `material-app/app/src/main/res/xml/data_extraction_rules.xml`
  - **Problem:** Both rule XMLs remain uncommented templates while `allowBackup="true"` ships the DataStore preferences file (bookmarks, recents, theme settings) to device-transfer/cloud backup by default.
  - **Impact:** Low sensitivity today, but backup behavior is undefined-by-default and will silently include future sensitive keys; templates mask intent.
  - **Fix:** Declare explicit include/exclude rules for preferences files, decide and document transfer behavior, and add a test asserting the rules parse and cover expected keys.
  - **Verification:** `adb backup`/device-to-device migration test shows only intended keys restored; XML lint validates the rule files.
  - **Resolution (2.0.8):** Both rule files now explicitly exclude `datastore/material_reference.preferences_pb` from cloud backup and device transfer with documented policy; `BackupRulesTest` asserts the XMLs parse, cover the preference file, and match the manifest wiring.

---

### Component Backlog (Material 3 Pinned-Artifact Gaps)

Add an item to the app only after the implementation, interactive demo, official API metadata, search aliases, adaptive behavior, accessibility checks, and source validation are complete:

- [ ] **UI-0021 - Side sheets** `[Medium]`
  - `ModalSideSheet` is not available in `1.5.0-alpha23` (re-verified against the pinned artifact on 2026-08-23); keep tracking upstream until it lands, then implement.
- [x] **UI-0022 - Flexible bottom app bars** `[Medium]`
  - Add `FlexibleBottomAppBar` as a working reference.
  - **Resolution (2.0.8):** `FlexibleBottomAppBarSample` registered as `navigation.flexible-bottom-app-bars` with full catalog metadata.
- [x] **UI-0023 - Wide navigation rail family** `[Medium]`
  - Add `WideNavigationRail`, its items, colors, and state.
  - **Resolution (2.0.8):** Docked + modal rails with shared state and items in `WideNavigationRailSample`; state/defaults APIs recorded.
- [x] **UI-0024 - Swipe-to-dismiss** `[Medium]`
  - Add `SwipeToDismissBox` and state/value APIs.
  - **Resolution (2.0.8):** `SwipeToDismissSample` with `SwipeToDismissBox`, `rememberSwipeToDismissBoxState`, and value APIs.
- [x] **UI-0025 - Short navigation bar variants** `[Low]`
  - Add `ShortNavigationBar` and defaults.
  - **Resolution (2.0.8):** `ShortNavigationBarSample` covering items and arrangement defaults.
- [x] **UI-0026 - Vertical sliders** `[Medium]`
  - Add `VerticalSlider`.
  - **Resolution (2.0.8):** `VerticalSlidersSample` with both directions via `SliderState`.
- [x] **UI-0027 - Full-screen and top search bar variants** `[Medium]`
  - Add `TopSearchBar` and expanded full-screen search bars.
  - **Resolution (2.0.8):** `TopSearchBarsSample` pairing `TopSearchBar` with `ExpandedFullScreenSearchBar` over `SearchBarState`.
- [x] **UI-0028 - Time picker dialogs as distinct references** `[Low]`
  - Add `TimePickerDialog` and `RichTimePickerDialog` as distinct references.
  - **Resolution (2.0.8):** `TimePickerDialogsSample` presents both dialog styles distinctly.
- [x] **UI-0029 - Scrollable tab row variants** `[Low]`
  - Add `PrimaryScrollableTabRow` and `SecondaryScrollableTabRow`.
  - **Resolution (2.0.8):** `ScrollableTabRowsSample` with primary/secondary scrollable rows.
- [x] **UI-0030 - Grouped dropdown menus** `[Low]`
  - Add `DropdownMenuGroup` and group labels.
  - **Resolution (2.0.8):** `GroupedDropdownMenusSample` using `MenuDefaults.groupShape` standard/vibrant groups.
- [x] **UI-0031 - Basic alert dialogs and dividers** `[Low]`
  - Add `BasicAlertDialog` plus horizontal/vertical dividers.
  - **Resolution (2.0.8):** `BasicAlertDialogsSample` and `DividersSample` as separate references.
- [x] **UI-0032 - Hero carousel strategy** `[Low]`
  - Add `HorizontalCenteredHeroCarousel`.
  - **Resolution (2.0.8):** `HeroCarouselsSample` with `rememberCarouselState`.
- [x] **UI-0033 - Scaffold foundations reference and remaining Defaults/state objects** `[Low]`
  - Add Scaffold as a foundations-layout reference and remaining Defaults/state objects per covered component.
  - **Resolution (2.0.8):** `ScaffoldFoundationSample` under Layout plus state/defaults API references (`ScaffoldDefaults`, carousel/rail/swipe states) across new entries.

---
