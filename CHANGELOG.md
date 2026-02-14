# Material Design Changelog

> **Project:** Material Design  
> **Version:** 1.3.5
> **Last Updated:** 2026-02-14

---

## [1.3.5] - 2026-02-14

### Fixed & Improved
-   **PR Review Fixes:** Implemented 15+ refinements across accessibility (focus indicators), JS stability (null guards, safe lookups), and structural HTML validation.

## [1.3.4] - 2026-02-14

### Fixed & Improved
-   **Universal Alignment:** Removed conflicting `max-width` constraints from cards to ensure all components align perfectly at the container edges.
-   **Fluid Containers:** Optimized the main container to 1400px for a more balanced and professional layout on large screens.
-   **Grid Consistency:** Standardized the `section-grid` across all pages to prevent staggered or misaligned card groups.
-   **Mobile Fluidity:** Resolved horizontal scrolling and zoom issues on mobile (1080x2412) by implementing fluid container logic.
-   **Responsive Hero:** Fixed hero section overflow and implemented a centered watermark backdrop on mobile.
-   **Component Spacing:** Significantly increased margins for headings and paragraphs for better readability and a premium feel.
-   **Navigation Polish:** Fixed "squished" app bar demos and ensured icons don't shrink and titles truncate properly.
-   **Typography:** Updated typography rows for better responsiveness and ensured all elements are contained within properly aligned cards.
-   **Chip Spacing:** Removed redundant margins from `.chip` to fix double-spacing inconsistencies.
-   **Global Balance:** Optimized grids and containers to smartly use dead space on ultra-wide monitors and enforced balanced centering.
-   **Bento Grid Layout:** Implemented a high-fidelity symmetric bento grid for the Inputs page, consolidating standard and expressive fields into a dense, professional dashboard layout.
-   **Edge-to-Edge Typography:** Overhauled typography rows to use negative margins and full-width borders, pinning labels and samples to the absolute card edges for a "filled" aesthetic.
-   **Full-Width Expressive Inputs:** Removed internal `max-width` constraints from expressive inputs and sliders for 100% container utilization.

## [1.3.3] - 2026-02-14

### Refactored
-   **JS Architecture:** Modularized the monolithic `src/js/scripts.js` into component-specific modules in `src/js/components/`.
    -   Extracted: `ripples.js`, `tabs.js`, `dialogs.js`, `sheets.js`, `inputs.js`, `sliders.js`, `motion.js`, `interactions.js`.
    -   `scripts.js` now acts as a lightweight orchestrator.
-   **Documentation:** Updated `README.md` and `DEVELOPMENT.md` to reflect the new modular CSS and JS project structure.

## [1.3.2] - 2026-02-14

### Refactored
-   **CSS Architecture:** Completely modularized the monolithic `styles.css` into a component-based structure.
    -   Created `src/css/base.css` for core foundations (Reset, Typography, Layouts).
    -   Created `src/css/components/` directory for granular styles (`buttons.css`, `cards.css`, `inputs.css`, `navigation.css`, `feedback.css`, `chips.css`, `fab.css`).
    -   Updated all HTML files to link these new modules.
    -   **Zero Regression:** Verified pixel-perfect match with original styles.

## [1.3.1] - 2026-02-13

### Added
- **Settings:** Implemented a new compact **Color Palette Viewer** to visualize active theme tokens and hex codes.

### Fixed & Improved
- **Codebase Health:**
    - **CSS**: Added missing `surface-container-highest-rgb` tokens to `variables.css`.
    - **Styles**: Replaced hardcoded hex values in `.snackbar` with design tokens.
    - **Safety**: Wrapped `localStorage` access in `scripts.js` with try/catch blocks for restricted environments.
    - **Security**: Refactored `showSnackbar` to sanitize inputs and prevent XSS.

## [1.3.0] - 2026-02-13

### Added
- **High-Fidelity Interaction Engine:**
    - **Expressive Slider Fill:** Implemented a dynamic `linear-gradient` active fill for thick sliders.
    - **Squiggly Slider Refinement:** Optimized track geometry to prevent overlapping lines, ensuring the squiggle meets the inactive track precisely at the thumb.
    - **Stagger Animations:** Switched all staggered entrance effects to use `IntersectionObserver` for viewport-based triggering.
- **Mobile Experience Optimizations:**
    - **Horizontal Scroll:** Added `.scroll-x` utility for chips and small components.
    - **Navigation Spacing:** Refined Drawer and Rail padding for better mobile density.
    - **Top Bar Toggle:** Injected a streamlined dark mode toggle for mobile viewports.
- **Component Library Enhancements:**
    - **Banner:** Added `.md-banner` with slide-in motion and responsive action wrapping.
    - **Pill Toasts:** Overhauled snackbars with a modern pill-shaped design and variants (Success, Error, Info, Warning).
    - **Card Refinements:** New expressive card layout with 3-dot action menus.
    - **Icon Buttons:** Added "Favorite" and "Save" variants.
    - **Music Widgets:** Added a premium suite of music players (Card, Circular, Compact, Daily Mix) with high-fidelity "Try Me" metadata.
    - **Layout Reorganization:** Consolidated "Standard Cards" into a cleaner "Cards" section and moved functional widgets to a dedicated "Widgets" area.
- **Legacy Support:**
    - **M2 Switches:** Implemented high-fidelity "Old Material" switches (thin track, large elevated thumb) usable via explicit classes.

### Fixed
- **Settings Audit:** Performed a complete cleanup of `settings.html`, removing dead JS logic and the redundant Visual Style selector for a cleaner UX.
- **Bug Fixes:**
    - **Mobile Theme Toggle:** Resolved duplicate header icons on palette-enabled pages.
    - **Component Alignment:** Fixed vertical centering for toast icons and M2 switch handles (z-index and offset correction).
    - **Visual Polish:** Fixed musical note alignment in the Compact Music Widget (pixel-perfect centering) and restored the high-contrast Daily Mix list style.
    - **Expressive Slider CSS:** Consolidated all slider styles from page-level overrides into the global `styles.css`.

## [1.2.7] - 2026-02-13

### Fixed
-   **PR Review Refinement (Batch 2):**
    -   **Elevation:** Added missing `--md-sys-elevation-4` token for standard FAB hover states.
    -   **Motion:** Extracted `--md-sys-motion-standard-easing` to fix redundant 200ms delays in wavy progress transitions.
    -   **Color Sync:** Synchronized `--md-sys-color-primary-rgb` across all seed theme variations (Teal, Red, Red, Orange, etc.) to ensure accurate state layers.
    -   **Dark Theme:** Fixed `--md-sys-color-secondary-rgb` mismatch in the dark theme.
    -   **JS Stability:** Added parent-null guards in the `drawer-item-pick` handler to prevent runtime crashes.

## [1.2.6] - 2026-02-13

### Added
-   **Deep Polish (Animation & Functionality):**
    -   **Semantic Cards:** Implemented high-fidelity success, warning, and error card styles with staggered entrance animations.
    -   **TextField Polish:** Added functional clear buttons (`close` icon) for search and icon-leading fields.
    -   **Icon Toolbar:** Implemented item selection logic; clicking an icon now marks it as active.
    -   **Settings Reactivity:** Enhanced the settings live preview to be more responsive and "alive" during theme adjustments.
    -   **Expressive Cards:** Standardized expressive card variants with `28px` corner radii and hover "pop" effects.

## [1.2.5] - 2026-02-13

### Fixed (PR Review)
-   **Navigation:** Eliminated implicit global `isInSrc` and simplified path detection logic for better reliability.
-   **Stability:** Added defensive null-guards to the Horizontal Drawer toggle logic to prevent potential TypeErrors.
-   **Performance:** Optimized `initExpressiveAnimations` to only start its loop if target elements are present on the page.
-   **Refactor:** Unified component initialization within the main `DOMContentLoaded` handler.

## [1.2.4] - 2026-02-13

### Refined
-   **Expressive Slider:** Refined the **Squiggly Slider** for 100% MD3 fidelity.
    -   Implemented a **Hybrid Track**: Progress portion is squiggly and animated; inactive portion is a straight line.
    -   Added dynamic CSS variable tracking (`--slider-value`) in `scripts.js` to drive the animation boundary.
    -   Centered the thumb precisely on the hybrid track transition.

## [1.2.3] - 2026-02-13

### Added
-   **Expressive Slider:** Implemented the **Squiggly Slider** on the Inputs page.
    -   Integrated the high-fidelity squiggly mask on the slider track.
    -   Added hover/active animations to give the track a "sliding wave" effect.
    -   Ensured cross-browser support for Webkit and Moz range tracks.

## [1.2.2] - 2026-02-13

### Added
-   **Expressive Progress:** Replaced the legacy wavy progress with the official **MD3 Squiggly Progress Bar**.
    -   Implemented a high-fidelity sine-wave SVG mask.
    -   Added horizontal sliding animations for an "active" feel.
    -   Created dedicated `determinate` and `indeterminate` variants.

## [1.2.1] - 2026-02-13

### Technical Compliance
-   **Tokens:** Standardized the design token system with complete MD3 roles including `surface-variant`, `inverse-surface`, and `inverse-primary`.
-   **RGB Variants:** Added `--md-sys-color-*-rgb` tokens to support high-fidelity interaction effects like slider halos.
-   **States:** Replaced hardcoded interaction layers with a standardized `::before` state layer system using MD3 opacities.
-   **Elevation:** Implemented the "Tonal Elevation" logic where elevated surfaces receive a primary-tinted container overlay.
-   **Motion:** Standardized component transitions (Labels, Drawers, Speed Dials) using MD3-standard cubic-bezier curves.

## [1.2.0] - 2026-02-13

### Added
-   **Expressive Components:** Implemented Speed Dial (Expandable FAB) with staggered entry animations.
-   **Expressive Components:** Added Horizontal Drawer component with smooth right-side expansion.
-   **Expressive Components:** Implemented Wavy and Segmented progress bars for high-fidelity loading states.
-   **Guidelines:** Added strict technical standards and compliance requirements for MD3, Material You, and Expressive traits in `DEVELOPMENT.md`.

### Changed
-   **Layout:** Reorganized the FAB section into a flexible 3-column grid for better demonstration of expressive components.
-   **Theming:** Fully integrated MD3 theme variables across all expressive components, enabling dynamic color switching and dark mode support.
-   **Alignment:** Shifted component demos to left-alignment within columns to prevent collisions during expansion.
-   **Refactor:** Streamlined `buttons.html` by removing redundant standard FAB size demos.

---

## [1.1.1] - 2026-02-13

### Fixed
-   **Security:** Replaced inline `onclick` handlers with `data-action` attributes in `navigation.html`.
-   **Settings:** Fixed duplicated DOM nesting in `settings.html` and improved reset functionality to only clear relevant keys.
-   **Tabs:** Scoped tab panel toggling to the parent container to prevent conflicts.

### Changed
-   **Refactor:** Updated `navigation.js` path detection to be environment-agnostic using script tag inspection.

---

## [1.1.0] - 2026-02-13

### Security
-   **Refactor:** Removed inline `onclick` attributes from all HTML files to improve CSP compliance and security.
-   **Hardening:** Implemented `escapeHtml` and `isValidUrl` in `navigation.js` to prevent XSS and Open Redirect vulnerabilities.

### Added
-   **Mobile Navigation:** Added `.menu-trigger` buttons to all content pages to allow Drawer access on mobile devices.
-   **Interactions:** Centralized event delegation in `scripts.js` for handling Dialogs, Snackbars, Sheets, and Theme Toggles.

### Fixed
-   **UI:** Fixed vertical misalignment between the Hamburger Menu icon and Page Title in the top app bar.
-   **Visual:** Fixed Ripple Effect positioning bug on Navigation Rail items (was appearing in top-left).
-   **Accessibility:** Fixed poor text contrast on "Filled" buttons in Dark Mode by removing forced white text.

### Changed
-   **Architecture:** Moved from inline JavaScript to a global Event Delegation model using `data-action` attributes.

---

## [1.0.0] - 2026-02-13

### Added
-   **Core:** Initial release of Material Design Showcase.
-   **Components:** Buttons, Cards, Inputs, Checkboxes, Switches, Chips, Dialogs, Bottom Sheets.
-   **Theming:** Dynamic Theme Engine with Light/Dark mode and 5+ color seeds.
-   **Navigation:** Responsive Navigation Rail and Drawer.
-   **Documentation:** README, DEVELOPMENT, CHANGELOG, and TASKS.
