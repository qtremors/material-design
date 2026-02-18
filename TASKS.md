# Tasks

> **Project:** Material Design  
> **Version:** 1.5.0  
> **Audit Date:** 2026-02-18

---

## Bugs

- [ ] **[BUG-01] `settings.html` — Missing closing tags**: The Appearance Card section (line 116) has an early-closed `</div>` that leaves the `control-row.accent-color-row` unclosed. The `settings-card` div is never properly closed, and the Radius control section present in the Playground is missing from this page entirely. This results in a malformed DOM tree.
  - **File:** `src/settings.html` (lines 74–116)

- [ ] **[BUG-02] `widgets.html` — Extra closing `</div>`**: Line 291 has a stray `</div>` that has no matching opening tag. This creates an invalid DOM structure.
  - **File:** `src/widgets.html` (line 291)

- [ ] **[BUG-03] Slider `--md-sys-color-primary-rgb` reference may be undefined**: The slider hover halo effect in `inputs.css` references `rgba(var(--md-sys-color-primary-rgb), 0.1)`, but this RGB token is only defined for some seeds/themes in `variables.css`. If a seed is missing its RGB definition, the halo will silently fail.
  - **File:** `src/css/components/inputs.css` (lines 149, 161)

- [ ] **[BUG-04] `feedback.css` references undefined color tokens**: Toast variants reference `--md-sys-color-success`, `--md-sys-color-on-success`, `--md-sys-color-warning`, and `--md-sys-color-on-warning`. These tokens are defined in some theme overrides but not in the base `:root` — they will evaluate to unset in the default (light) theme unless the seed provides them.
  - **File:** `src/css/components/feedback.css` (lines 35–38), `src/css/components/cards.css` (lines 49–51)

- [ ] **[BUG-05] `playground.js` — `loadCSSVars()` fetches relative path**: The fetch call `fetch('css/variables.css')` uses a relative URL, which only works when the page is served from the `src/` directory. This will break if the playground is ever served from a different path or nested route.
  - **File:** `src/js/components/playground.js`

---

## Security

- [ ] **[SEC-01] Residual inline `onclick` handlers in `feedback.html`**: Despite the CHANGELOG (v1.1.0) documenting removal of all inline `onclick` attributes for CSP compliance, `feedback.html` still has 5 inline `onclick` handlers — 4 on toast buttons (lines 82–85) and 1 on the snackbar close button (line 156). These should be migrated to the `data-action` event delegation system.
  - **File:** `src/feedback.html` (lines 82–85, 156)

- [ ] **[SEC-02] Residual inline `onclick` handlers in `widgets.html`**: The "Show Labels" and "Show Grid" toggle labels use inline `onclick` to programmatically click their associated checkboxes (lines 55, 67). These should use `data-action` or a `<label for="…">` association instead.
  - **File:** `src/widgets.html` (lines 55, 67)

- [ ] **[SEC-03] `theme.js` — `window.name` persistence is a known vector**: `window.name` is accessible cross-origin and persists across navigations. While currently used as a fallback for `file://` protocol, the parsed JSON is applied directly to `this.state` without validation. A malicious page previously visited could inject arbitrary values.
  - **File:** `src/js/theme.js` (lines 42–50)

---

## Code Quality

- [ ] **[CQ-01] Hardcoded hex colors in `buttons.html`**: The "Favorite" icon button uses `style="background: #DDE2F0; color: #1B1B1F;"` and the "Bookmark" button uses `style="color: #006C51;"` — direct violations of the project's "No Hardcoded Colors" standard documented in `DEVELOPMENT.md`.
  - **File:** `src/buttons.html` (lines 146–147)

- [ ] **[CQ-02] Color picker hex values hardcoded across 3 files**: The 10 accent color swatches in `settings.html`, `playground.html`, and the navigation.js mobile sheet all have their swatch colors hardcoded as inline `style="background: #hex"`. These are static preview colors and are intentional, but they are duplicated across files. Consider centralizing.
  - **Files:** `src/settings.html`, `src/playground.html`, `src/js/navigation.js`

- [ ] **[CQ-03] Excessive inline styles across HTML pages**: Nearly every HTML page uses extensive inline `style` attributes for layout, spacing, and sizing. This defeats the purpose of the component CSS system and makes maintenance difficult. Key offenders:
  - `buttons.html` — speed dial containers, grid layouts (~20 inline styles)
  - `widgets.html` — toggle wrapper, dividers (~10 inline styles)
  - `cards.html` — card demos (~15 inline styles)
  - `navigation.html` — app bar demo (~8 inline styles)
  - `feedback.html` — dialog layouts (~6 inline styles)

- [ ] **[CQ-04] Duplicated CSS selector patterns**: `buttons.css` uses dual selectors like `.md-btn, .btn` and `.md-btn-filled, .btn-filled` throughout, maintaining a legacy `.btn` alias alongside the MD3-standard `.md-btn`. If the legacy alias is no longer used in any HTML, it should be removed to reduce CSS payload.
  - **File:** `src/css/components/buttons.css`

- [ ] **[CQ-05] Unused CSS class — `.icon-btn-filled` and `.icon-btn-tonal`**: These classes are used in `buttons.html` (line 150–151) but have no corresponding CSS definitions in `buttons.css`. The buttons render with default icon-btn styling only.
  - **File:** `src/css/components/buttons.css`, `src/buttons.html`

- [ ] **[CQ-06] `cards.css` — `.card-colorful` and `.dialog` grouped together**: Line 77 applies `border-radius: 28px` to both `.card-colorful` and `.dialog`. Grouping these unrelated selectors is confusing and fragile.
  - **File:** `src/css/components/cards.css` (line 77)

- [ ] **[CQ-07] `buttons.css` — Hardcoded `rgba` in elevation shadow**: The `.md-btn-elevated` class uses `box-shadow: 0 1px 2px rgba(0,0,0,0.2)` instead of using the project's `--md-sys-elevation-*` tokens.
  - **File:** `src/css/components/buttons.css` (line 33)

- [ ] **[CQ-08] `buttons.css` — Animation hover shadows use hardcoded `rgba`**: `.btn-anim-scale:hover` and `.btn-anim-glow:hover` (lines 47–48) use `rgba(0,0,0,0.15)` instead of elevation tokens.
  - **File:** `src/css/components/buttons.css` (lines 47–48)

- [ ] **[CQ-09] `navigation.css` — Header logo has conflicting hover styles**: Lines 27–30 define a hover with `background-color: currentcolor`, but lines 49–51 immediately override it with `background-color: transparent !important`. The first rule is dead code.
  - **File:** `src/css/components/navigation.css` (lines 27–30, 49–51)

- [ ] **[CQ-10] Extra blank lines in `cards.css`**: Lines 29–34 contain 6 consecutive blank lines, suggesting incomplete refactoring or removed code.
  - **File:** `src/css/components/cards.css` (lines 29–34)

---

## Accessibility

- [ ] **[A11Y-01] Missing `aria-label` on icon-only buttons**: Multiple icon-only buttons across all pages (e.g., `buttons.html` lines 92–95, 149–151; `widgets.html` control buttons) lack `aria-label` attributes, making them inaccessible to screen readers.

- [ ] **[A11Y-02] Tabs use `<div>` instead of semantic tab roles**: The tab implementation in `navigation.html` and `playground.html` uses `<div class="md-tab">` without `role="tab"`, `role="tablist"`, or `role="tabpanel"` attributes. While `tabs.js` manages `aria-selected`, the missing roles render the tabs invisible to assistive technology.
  - **Files:** All HTML files with tabs, `src/js/components/tabs.js`

- [ ] **[A11Y-03] Color picker swatches lack accessible labels**: The accent color swatches in `settings.html` and `playground.html` are `<div>` elements with no text, `aria-label`, or `role="radio"` grouping. Screen readers cannot identify what these controls do. They should use `role="radiogroup"` with `role="radio"` and `aria-label="[color name]"`.
  - **Files:** `src/settings.html`, `src/playground.html`

- [ ] **[A11Y-04] Dialogs lack focus trapping and `role="dialog"`**: The dialog implementation in `dialogs.js` toggles visibility classes but does not trap focus, manage `aria-modal="true"`, or set `role="dialog"` on the dialog container. Keyboard users can tab behind the backdrop.
  - **Files:** `src/js/components/dialogs.js`, `src/feedback.html`

- [ ] **[A11Y-05] Keyboard navigation gaps**: The navigation rail items and drawer items are `<a>` elements (accessible), but the speed dial, icon toolbar, and horizontal speed dial components lack keyboard support (Enter/Space to activate, Escape to close).
  - **File:** `src/js/components/interactions.js`

---

## Performance

- [ ] **[PERF-01] All pages load all component CSS regardless of usage**: Every HTML page loads the full set of 7 component CSS files (`buttons.css`, `cards.css`, `inputs.css`, `feedback.css`, `chips.css`, `fab.css`, `navigation.css`) even when the page only uses a subset. For example, `typography.html` loads chips, fab, inputs, and feedback CSS unnecessarily.
  - **Files:** All HTML pages

- [ ] **[PERF-02] Google Fonts loaded synchronously**: All pages load 2–4 Google Fonts stylesheets via blocking `<link>` tags. These cause render-blocking requests. Consider `font-display: swap` (already set) plus `media="print" onload="this.media='all'"` or preloading strategies.
  - **Files:** All HTML pages

- [ ] **[PERF-03] `playground.js` — Synchronous CSS variable parsing**: `loadCSSVars()` fetches and parses the entire `variables.css` file as text on page load, then iterates through every line. For a 434-line file this is acceptable, but the approach won't scale and could be replaced with `getComputedStyle` for active tokens.
  - **File:** `src/js/components/playground.js`

- [ ] **[PERF-04] `widgets.js` — `packWidgets()` runs on every resize without debounce**: The bin-packing layout engine recalculates on every `resize` event frame. This should use `requestAnimationFrame` or a debounce wrapper to prevent layout thrashing.
  - **File:** `src/js/components/widgets.js`

---

## Technical Debt

- [ ] **[TD-01] Stopwatch `setInterval` is never cleared on page navigation**: The stopwatch in `widgets.js` starts a `setInterval` that is never cleaned up when navigating away from the page (no SPA teardown). In a multi-page app this is fine, but it's worth noting.
  - **File:** `src/js/components/widgets.js`

- [ ] **[TD-02] `scripts.js` — `initSettings()` called on every page**: The orchestrator calls `initSettings()` on DOMContentLoaded for every page, but the function targets elements specific to `settings.html`. The guards inside prevent errors, but the function shouldn't be invoked at all on non-settings pages.
  - **File:** `src/js/scripts.js`

- [ ] **[TD-03] `inputs.js` — Only handles chip toggles**: Despite its name suggesting broad input interactivity, `inputs.js` only handles chip toggle logic. Consider renaming to `chips.js` or expanding to cover text field clearing, validation, etc.
  - **File:** `src/js/components/inputs.js`

- [ ] **[TD-04] Global function pollution**: Several scripts define global functions (`showToast`, `openDialog`, `closeDialog`, `openSheet`, `closeSheet`, `switchTab`) attached to `window`. Consider using a namespace (e.g., `window.MD = { ... }`) to avoid collisions.
  - **Files:** `src/js/components/dialogs.js`, `src/js/components/sheets.js`, `src/js/components/tabs.js`

- [ ] **[TD-05] `CHANGELOG.md` version mismatch**: The header says `Version: 1.5.0` and `Last Updated: 2026-02-15`, but the first entry is `[1.5.0] - 2026-02-16`. The "Last Updated" date is stale.
  - **File:** `CHANGELOG.md` (lines 4–5, 9)

- [ ] **[TD-06] `settings.css` — `toggle-btn:hover` uses undefined token**: Line 138 references `--md-sys-state-hover-on-surface` with a fallback; this token is not defined anywhere in `variables.css`. The fallback `rgba(0, 0, 0, 0.05)` works but won't adapt to dark themes.
  - **File:** `src/css/settings.css` (line 138)

---

## Documentation

- [ ] **[DOC-01] `DEVELOPMENT.md` — Font loading section incomplete**: The development guide mentions `Roboto Flex` and `Open Sans` and `Space Mono`, but only `settings.html` loads `Open Sans` and `Space Mono`. The other pages don't include these fonts despite them being documented as part of the type system.
  - **File:** `DEVELOPMENT.md`, all HTML pages

- [ ] **[DOC-02] `README.md` — Project structure is outdated**: The project structure tree in the README mentions `src/css/styles.css` (singular), but the CSS was modularized in v1.3.2 into `base.css`, `variables.css`, and `components/`. The structure tree should be updated to reflect the current modular layout.
  - **File:** `README.md`
