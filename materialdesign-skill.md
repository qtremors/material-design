---
name: material-design-ui
description: Professional guidelines for designing, styling, and polishing frontends according to Material Design 3 and Expressive standards.
---

# Material Design UI & UX Design Skill

This skill defines the visual language, design principles, and user experience standards for the **Material Design** project. Use these guidelines to create or overhaul frontends that feel premium, dynamic, and strictly compliant with Material Design 3 (MD3) and "Material You".

## 1. Visual Language & Aesthetics
The core aesthetic is **"Material You"** (dynamic/personal) mixed with **"Material 3 Expressive"** (playful/modern).

-   **The Color System**: Everything is driven by design tokens.
    -   **Strict Token Compliance**: Never use raw hex codes (`#123`), `rgb()`, or CSS named colors.
    -   **Usage**: Components must use `--md-sys-color-*` tokens (e.g., `on-surface-variant`, `primary-container`).
-   **Typography**: Hierarchy is king.
    -   Use `var(--md-sys-typescale-*)` for all text.
    -   **Headline/Title**: Use `Roboto Flex` with variable weights for emphasis.
    -   **Body/Label**: Use `Roboto` for clarity.
-   **Vibrancy**: Use high-contrast surface containers (`surface-container-high`) for elevated cards and low-contrast ones (`surface-container-low`) for background grouping.

## 2. Typography & Hierarchy
Typography in MD3 is not just about size; it's about communicative weight and structure.

-   **Font Families**:
    -   **Primary Display/Headlines**: `Roboto Flex` (Variable). Use it for `display`, `headline`, and `title` levels.
    -   **Support/Body**: `Roboto` or `Open Sans`. Use for `body` and `label` levels.
-   **The Type Scale**:
    -   **Display (Large/Med/Small)**: Used for high-emphasis screens (e.g., Hero headers).
    -   **Headline**: Used for page titles and major sections.
    -   **Title**: Used for card headers and modal titles.
    -   **Body**: Used for readable content. Default size is `16px`.
    -   **Label**: Used for buttons, chips, and small metadata.
-   **Expressive Type**:
    -   Leverage `font-variation-settings` for `wdth` (width) and `wght` (weight) on `Roboto Flex` to create dynamic, stretching headers that feel "Expressive".

## 3. Layout & Adaptability
Designs must be fluid and adapt across all screen sizes without losing their core "feel".

-   **Standard Spacing**:
    -   Use 8px grid multiples (8px, 16px, 24px, 32px, 48px).
    -   Default page container padding: `16px` (mobile), `24px` (tablet), `48px` (desktop).
-   **Adaptive Containers**:
    -   **Mobile (< 600px)**: Move the Navigation Rail to a Navigation Drawer. Center header titles and actions for visual balance.
    -   **Desktop (> 900px)**: Use the Navigation Rail. Content should be centered in a `max-width: 1200px` container.
-   **Grid Systems**: Use `display: grid` with `repeat(auto-fill, minmax(280px, 1fr))` for dashboard layouts to ensure perfect responsiveness.

## 3. Shapes & Sizing
Material Design 3 uses rounder shapes and larger targets than MD2.

-   **Corner Radiuses**:
    -   **Buttons**: Full Round (`var(--md-sys-shape-corner-full)`).
    -   **Cards**: Extra Large (`var(--md-sys-shape-corner-extra-large)`).
    -   **Dialogs/Sheets**: Specific tokens for top-only or full rounding.
-   **Touch Targets**: Every interactive element must be at least `48x48px` for accessibility.

## 4. Elevation & Feedback
Elevation is communicated through **Shadows** and **State Layers**, never through simple gray borders.

-   **Elevation Levels**:
    -   Use `var(--md-sys-elevation-*)` (Levels 1 to 5).
    -   **Level 0**: Flat (Surface).
    -   **Level 1-2**: Standard cards/menus.
    -   **Level 3**: Dialogs/FABs.
-   **Interaction Feedback**:
    -   **The Ripple**: Every click must trigger a radial ripple (`.ripple-target`).
    -   **State Layers**: Overlays that use the current "On" color at specific opacities:
        -   **Hover**: 8% opacity.
        -   **Pressed**: 12% opacity.
        -   **Focus**: 10% opacity.

## 5. Motion & Feel
Motion should feel physical, purposeful, and "Expressive".

-   **Standard Durations**: 200ms (standard), 400ms (medium/complex).
-   **Easings**:
    -   **Standard**: `cubic-bezier(0.2, 0, 0, 1)` (Predictable entry/exit).
    -   **Emphasized**: `cubic-bezier(0.3, 0, 0.8, 0.15)` (Dramatic transitions).
    -   **Expressive (Bouncy)**: For FAB morphs or high-impact UI, use `cubic-bezier(0.175, 0.885, 0.32, 1.275)`.

## 6. Checklist: The "MD3 Feel Test"
Before finalizing a frontend design, ask:
1.  **Does it glow?** (Does the theme seed propagate correctly to all container levels?)
2.  **Does it breathe?** (Is there enough whitespace/padding to meet MD3 standards?)
3.  **Is it soft?** (Are corner radiuses correct, or are there "sharp" corners violating the brand?)
4.  **Is it alive?** (Do ripples trigger on every interaction? Do buttons lift on hover?)
5.  **Is it adaptive?** (Does the layout reorganize cleanly when shrinking to mobile?)
