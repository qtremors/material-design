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
    -   **Headline/Title**: Use `Roboto` (Standard) or `Roboto Flex` (Expressive) for emphasis.
    -   **Body/Label**: Use `Roboto` for clarity.
-   **Vibrancy**: Use high-contrast surface containers (`surface-container-high`) for elevated cards.
-   **Themes**: Support **Light**, **Dark**, and **OLED** (True Black) modes.

## 2. Typography & Hierarchy
Typography in MD3 is not just about size; it's about communicative weight and structure.

-   **Font Families**:
    -   **Standard**: `Roboto` (Default).
    -   **Expressive**: `Roboto Flex` (Variable). Used when `data-style="expressive"` is active.
-   **The Type Scale**:
    -   **Display**: High-emphasis screens (e.g., Hero headers).
    -   **Headline**: Page titles.
    -   **Title**: Card headers.
    -   **Body**: Readable content (`16px` base).
    -   **Label**: Small metadata/buttons.
-   **Expressive Type**:
    -   Leverage `font-variation-settings: 'wdth' 115` on `Roboto Flex` for dynamic headers.

## 3. Layout & Adaptability
Designs must be fluid and adapt across all screen sizes.

-   **Standard Spacing**:
    -   Use 8px grid multiples (8px, 16px, 24px, 32px, 48px).
-   **Adaptive Containers**:
    -   **Mobile (< 600px)**: Use **Navigation Drawer** (Modal) and Bottom Sheet for actions.
    -   **Tablet (600px - 840px)**: Use **Navigation Rail** (Left).
    -   **Desktop (> 840px)**: Use **Navigation Rail** or **Navigation Drawer** (Standard).
-   **Grid Systems**: Use `repeat(auto-fill, minmax(300px, 1fr))` for responsive dashboards.

## 4. Shapes & Sizing
Material Design 3 uses rounder shapes and larger targets than MD2.

-   **Corner Radiuses**:
    -   **Buttons**: Full Round (`--md-sys-shape-corner-full`).
    -   **Cards**: Extra Large (`--md-sys-shape-corner-extra-large`).
    -   **Standard**: Medium/Large (`--md-sys-shape-corner-medium`).
-   **Touch Targets**: Minimum `48x48px` for accessibility.

## 5. Elevation & Feedback
Elevation is communicated through **Shadows** and **State Layers**.
*Note: In OLED/Dark modes, elevation is often replaced or augmented by borders (`outline-variant`) for contrast.*

-   **Elevation Levels**:
    -   Level 0-5 using `--md-sys-elevation-*`.
-   **Interaction Feedback**:
    -   **The Ripple**: Radial reaction (`.ripple-target`).
    -   **State Layers** (Overlay Opacity):
        -   **Hover**: 8% (`0.08`)
        -   **Focus/Pressed**: 12% (`0.12`)

## 6. Motion & Feel
Motion should feel physical and purposeful.

-   **Standard Durations**: 200ms (standard), 400ms (medium/complex).
-   **Easings**:
    -   **Standard**: `cubic-bezier(0.2, 0, 0, 1)`.
    -   **Emphasized**: `cubic-bezier(0.3, 0, 0.8, 0.15)`.
    -   **Spring**: `cubic-bezier(0.175, 0.885, 0.32, 1.275)` for "Expressive".

## 7. Checklist: The "MD3 Feel Test"
Before finalizing a frontend design, ask:
1.  **Does it glow?** (Does the theme seed propagate correctly to all container levels?)
2.  **Does it breathe?** (Is there enough whitespace/padding to meet MD3 standards?)
3.  **Is it soft?** (Are corner radiuses correct, or are there "sharp" corners violating the brand?)
4.  **Is it alive?** (Do ripples trigger on every interaction? Do buttons lift on hover?)
5.  **Is it adaptive?** (Does the layout reorganize cleanly when shrinking to mobile?)
