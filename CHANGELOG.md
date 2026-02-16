# Material Design Changelog

> **Project:** Material Design  
> **Version:** 1.5.0
> **Last Updated:** 2026-02-15

---

## [1.5.0] - 2026-02-16

### Added & Improved
-   **Settings Layout Overhaul**:
    -   **Responsive Design**: Implemented a robust side-by-side layout for desktop and a centered, vertical stack for mobile (< 600px).
    -   **Color Grid**: Standardized the color palette to a clean 5-column grid.
    -   **Mobile Experience**: Enhanced the mobile color selection sheet with centered headers and subtle borders for visibility in dark modes.
-   **Navigation Polish**:
    -   **Swatch Consistency**: Centralized header color swatches in `navigation.js` and enforced "Monochrome First" ordering across Dashboard, Settings, and Playground.
    -   **OLED Logic**: Fixed the top-bar theme toggle to correctly cycle through Light -> Dark -> OLED -> Dark, displaying the correct 'contrast' icon for OLED availability.
-   **Playground Enhancements**:
    -   **Dynamic Sorting**: Implemented semantic visual sorting for active tokens (Matrix & Sidebar) to prioritize Neutrals, then Hue, then Lightness, replacing arbitrary CSS order.
    -   **Navigation Fix**: Removed a conflicting global `.menu-trigger` style that was hiding the navigation rail's menu button and shifting items up on the Playground page.
    -   **Global Z-Index**: Lowered Navigation Rail `z-index` to `90` to ensure it sits cleanly behind the Top App Bar, hiding the internal rail menu trigger on desktop.
    -   **Ripple Logic**: Implemented support for `data-ripple-color` to allow custom ripple colors (e.g., on-primary) defined in HTML.
    -   **Semantic Contrast**: Added missing Dark/OLED theme overrides for the "Warning" color role and optimized "Success" tokens to ensure semantic cards have accessible text contrast in dark modes.
    -   **OLED Borders**: Refined the global OLED border rule in `variables.css` to exclude semantic, filled, and colorful cards, ensuring their native styles (backgroundColor, colored borders) are preserved without interference.
    -   **Token Parity**: Audited and fixed `variables.css` to ensure full parity across all 3 theme modes. Added missing `inverse-primary` and `RGB` tokens to all Dark/OLED seeds.

### Fixed (PR & Stability)
-   **Critical Interaction Fixes**:
    -   **Mobile Sheet**: Fixed a bug where the color palette wouldn't close when clicking the background scrim. Implemented a singleton scrim pattern (`#sheet-scrim`) to prevent DOM duplication.
    -   **Theme State**: Exposed `ThemeEngine` globally to ensure the mobile palette correctly highlights the active color upon opening.
-   **Code Quality (PR Review)**:
    -   **CSS**: Renamed camelCase keyframes (`fadeUp`, `linearIndeterminate`) to kebab-case for standard compliance.
    -   **Accessibility**: Added `:focus-visible` styles to hidden input siblings and improved logo visibility on dark themes.
    -   **Cleanup**: Removed duplicate `</head>` tags and hardcoded hex values in Settings/Widgets.
    -   **Logic**: Guarded `window.setThemeConfig` calls and optimized `syncSelectionState` in the Playground to prevent race conditions.
    -   **Refactor**: Extracted repeated swatch row HTML into a shared generator in `navigation.js`, removing hardcoded duplication from all HTML pages.
    -   **Playground Polish**: 
        -   **Token Cards**: Redesigned the token card layout to remove redundant role names and reordered fields (Color Name -> Hex -> Variable) for better scanning.
        -   **Visuals**: Updated accent badges to a pill-shaped design with consistent theming and fixed square-corner artifacts on inactive card swatches.
    -   **Matrix Refactor**:
        -   **Logic**: Split the color matrix into distinct "Active System Roles" and "Inactive Color Palettes" to eliminate redundancy.
        -   **Blue Seed**: Implemented custom logic to extract Default Blue tokens from `:root` variables to ensure accurate palette visualization when inactive.
    -   **Codebase Polish**:
        -   **Cards**: Removed conflicting `overflow: hidden` from list items to fix border-radius clipping on rounded lists.
        -   **Music Widgets**: Explicitly matched child border-radii for album art and daily mix headers to their parent containers to prevent background bleeding during transitions.
        -   **Naming Audit**: 
            -   Renamed `.spinner` to `.circular-progress` and `.typing-dots` to `.dots-loader` across HTML/CSS for consistency.
            -   Renamed "Horizontal Drawer" to "Horizontal Speed Dial" in JS/HTML to avoid ambiguity with Navigation Drawers.
            -   Added missing `.card-colorful` demo to `cards.html`.
    -   **Navigation Bugs**:
        -   **Z-Index Layering**: Lowered Navigation Rail z-index to `90` to ensure it sits cleanly behind the Top App Bar, allowing the header logo to be fully visible and hiding the internal rail menu trigger on desktop.
        -   **Color Swatch Initialization**: Fixed a race condition where the active color swatch wasn't highlighted on page load by optimizing the initialization order in `navigation.js`.
    -   **Navigation Rail Improvements**:
        -   **Scrollable Layout**: Implemented a Flexbox-based scrollable container for navigation items, ensuring accessibility on small screens while keeping the Menu Trigger and Theme Toggle fixed.
        -   **Compact Spacing**: Removed excess top padding (`0px`) and minimized bottom margins (`8px`) to maximize vertical space for navigation links.

---

## [1.4.5] - 2026-02-15

### Added & Improved
-   **Universal Color Selection**: Standardized the theme color picker across all pages.
    -   **Desktop**: Integrated a clean row of all 10 color seeds directly into the header.
    -   **Mobile**: Implemented a responsive "Palette" icon and Modal Bottom Sheet via `navigation.js`.
-   **Widget Polish**: 
    -   **Calendar Enhancement**: Added Day/Date header and interactivity (ripples/hover) to events.
    -   **Alignment**: Fixed overflows in Clock/Weather and vertically centered News media.
    -   **Density**: Balanced list item spacing in Contacts and removed "dead space" at the bottom of the dashboard.
-   **User Control**: Moved "Show Labels/Grid" controls to the right side of the header for better reach.

## [1.4.4] - 2026-02-15

### Added
-   **Monochrome Theme**: Introduced a neutral grey-scale color seed for high-contrast accessibility.
-   **Theme Propagation**: Integrated the Monochrome swatch project-wide across all component demonstration pages.

## [1.4.3] - 2026-02-15

### Added
-   **Branding**: Integrated a transparent project logo into the Top App Bar project-wide.
-   **Documentation & Standards**: 
    -   Created `materialdesign-skill.md` for AI-driven development guidance.
    -   Updated `DEVELOPMENT.md` with OLED mode and branding requirements.
    -   Defined detailed Typography & Hierarchy standards using `Roboto Flex` variations.

## [1.4.2] - 2026-02-15

### Added & Fixed
-   **Playground Overhaul**: 
    -   **Color Matrix**: Searchable grid displaying all 300+ generated design tokens.
    -   **Live Lab**: Interactive area for testing buttons, cards, and inputs.
    -   **Side Panel**: Persistent theme mode and accent seed controls.
-   **Settings Cleanup**: Removed redundant "Customisation" (Corner Radius) section as controls moved to the Playground.
-   **UI Fixes**: Resolved layout regressions in side panels and the palette color naming engine.

## [1.4.1] - 2026-02-15

### Refactored & Polished
-   **Modularity Refactor**: Extracted all internal `<style>` and `<script>` blocks from HTML files into modular project files in `src/`.
-   **Hero Section Polish**: Updated dashboard copy to focus on MD3/Material You technical traits.
-   **Settings Migration**: Retired legacy "Live Preview" logic to focus on the unified theme engine.

## [1.4.0] - 2026-02-14

### Fixed & Improved
-   **Cross-Browser Compatibility:** Fixed a critical issue where slider fill colors were not rendering on Chromium browsers due to specificity conflicts. Implemented a robust CSS solution that works consistently across Firefox, Chromium, and Safari.
-   **OLED Polish:**
    -   Added distinct borders to cards in OLED mode to ensure visibility against the pure black background.
    -   Updated all color seed selectors to correctly apply to the OLED theme, fixing broken palettes in OLED mode.
-   **Typography:** Fixed typography page to correctly use active color seed tokens for headings and examples.

---

## [1.3.9] - 2026-02-14

### Fixed
-   **Daily Mix Color Scheme:** Replaced hardcoded Daily Mix widget colors with `md-sys-color` tokens to ensure proper dynamic theming.
-   **Widget Vibrancy:** Updated the entire widget library to use active theme tokens.
-   **Adaptive Grid:** Implemented a responsive grid system for widgets.
-   **UI Refinement:** Moved color swatches to the widget header.
-   **Expressive Input:** Fixed a double border issue on the Expressive Floating Label input.
-   **Continuous Slider:** Added a fill color to the standard continuous slider track.
-   **OLED Theme:** Introduced a "True Black" OLED theme option and Settings integration.

### Refactored
-   **Widget CSS:** Modularized `widgets.css` into specific modules.

## [1.3.8] - 2026-02-14

### Added
-   **Widget Features:**
    -   **Labels Toggle:** Added a "Show Labels" toggle to the Widgets dashboard, allowing users to hide widget titles for a cleaner, image-focused layout.
    -   **Unique IDs:** Assigned unique IDs (e.g., `#widget-music-card`, `#widget-daily-mix`) to all widgets for granular styling and layout control.
-   **Documentation:** Added a fundamental "Create New Widgets" guide to `DEVELOPMENT.md` covering structure, scoping, and interactivity.

### Fixed & Improved
-   **Widget Redesigns:**
    -   **Daily Mix:** Overhauled the Daily Mix widget to a 4x5 vertical layout with a two-tone background (Light Blue/Grey) and a floating song list card, matching the new design language.
    -   **Circular Music Player:** Refined the circular player with a larger 240px album art container and diagonally positioned floating buttons that sit precisely on the border.
-   **Interaction Polish:** Fixed a layout shift issue where music player buttons would jump position during hover and click states.
-   **Visual Refinements:**
    -   Removed redundant title/artist text from the Circular Music Player for a cleaner aesthetic.
    -   Corrected grid size labels in `widgets.html` and their corresponding CSS classes.
    -   Fixed color token usage to ensure proper theming across all widget backgrounds.

## [1.3.7] - 2026-02-14

### Added
- **Visual Grid System:** Implemented a non-destructive background grid for the widgets page that provides a 5-column visualization without impacting the original styling and sizing of the widgets.
- **Refined Grid Toggle:** Enhanced the "Show Grid" toggle with an elevated capsule design and improved vertical alignment in the dashboard header.

### Fixed & Improved
- **Responsive Alignment:** Optimized container width snapping to account for the Navigation Rail and page margins, preventing grid "bleeding" or cut-off lines.
- **Toggle Layout:** Fixed wrapping behavior in the header to ensure the title and toggle remain accessible and well-spaced on tablet and mobile.

## [1.3.6] - 2026-02-14

### Added
-   **Widget Library Expansion:** Introduced new functional widgets with grid size labels:
    -   **Digital Clock 2x2:** Real-time clock with dynamic updates.
    -   **Stopwatch 2x1:** Fully functional timer.
    -   **Weather 2x2:** Visual forecast widget.
    -   **Featured News 4x2:** Expandable news card.
    -   **Calendar 2x2:** Schedule overview.
    -   **Battery Status 2x1:** Live level indicator.
-   **Grid Visibility System:** Implemented a perfect square 100x100px grid for the Widgets dashboard with a dedicated "Show Grid" toggle in the header.
-   **Container Architecture:** Split the Widgets header and content into distinct containers with rounded perimeters and 28px corners.
-   **Functional Widgets:** Implemented `src/js/components/widgets.js` to bring the music player widgets to life.
    -   Supported Play/Pause, Shuffle toggles, and multi-state Repeat (None, All, One).
    -   Added interactivity to the "Daily Mix" list items.
-   **Dashboard Expansion:** Added a direct "Widgets" navigation card to the `index.html` dashboard.

### Fixed & Improved
-   **Design Restoration:** Restored original widget aesthetics (Card, Daily Mix, Circular, Compact) as per user feedback and reference images.
-   **Refined Labeling:** Added grid size metadata (e.g., 4x2, 2x2) directly into widget titles for better layout clarity.
-   **Layout Consistency:** Reverted to section-based grid layout for improved readability and structure.
-   **Widget Refinements:**
    -   Implemented infinite state cycling (Repeat None -> All -> One) for the repeat button.
    -   Added high-fidelity "press" animations to all music widget buttons.
    -   Enhanced component targeting to ensure the Compact Music Widget's controls are fully functional.
    -   Added visual feedback for Daily Mix list item selection.

### Refactored
-   **Component Organization:** Extracted the "Widgets" section from the Cards page into its own dedicated page (`src/widgets.html`).
-   **Navigation:** Added "Widgets" to the global Navigation Rail and Drawer for direct access to specialized components (Music Players, Contacts Widget).

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

---

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
