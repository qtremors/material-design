# Material Design UI and UX for Web

This skill provides design and UI implementation guidance for building modern, responsive, and accessible web applications using Material Design 3 principles.

## 1. CSS Custom Properties Design Token System

Structure web stylesheets around semantic CSS custom properties rather than hardcoded hex values:

```css
:root {
  /* Color Roles - Light Scheme */
  --md-sys-color-primary: #00639b;
  --md-sys-color-on-primary: #ffffff;
  --md-sys-color-primary-container: #cee5ff;
  --md-sys-color-on-primary-container: #001d33;

  --md-sys-color-surface: #f8f9ff;
  --md-sys-color-on-surface: #191c20;
  --md-sys-color-surface-container: #eceef4;
  --md-sys-color-surface-container-high: #e6e8ee;

  --md-sys-color-outline: #73777f;
  --md-sys-color-outline-variant: #c3c7d0;

  /* Typography Scale */
  --md-sys-typescale-display-large: 400 3.5rem / 4rem 'Roboto Flex', sans-serif;
  --md-sys-typescale-headline-medium: 400 1.75rem / 2.25rem 'Roboto Flex', sans-serif;
  --md-sys-typescale-title-medium: 500 1rem / 1.5rem 'Roboto Flex', sans-serif;
  --md-sys-typescale-body-large: 400 1rem / 1.5rem 'Roboto Flex', sans-serif;
  --md-sys-typescale-label-large: 500 0.875rem / 1.25rem 'Roboto Flex', sans-serif;

  /* Shape Tokens */
  --md-sys-shape-corner-none: 0px;
  --md-sys-shape-corner-small: 8px;
  --md-sys-shape-corner-medium: 12px;
  --md-sys-shape-corner-large: 16px;
  --md-sys-shape-corner-full: 9999px;

  /* Motion Tokens */
  --md-sys-motion-duration-short: 200ms;
  --md-sys-motion-duration-medium: 400ms;
  --md-sys-motion-easing-emphasized: cubic-bezier(0.2, 0, 0, 1);
}

@media (prefers-color-scheme: dark) {
  :root {
    --md-sys-color-primary: #97cbff;
    --md-sys-color-on-primary: #003355;
    --md-sys-color-primary-container: #004b77;
    --md-sys-color-on-primary-container: #cee5ff;

    --md-sys-color-surface: #101418;
    --md-sys-color-on-surface: #e1e2e8;
    --md-sys-color-surface-container: #1d2024;
    --md-sys-color-surface-container-high: #272a2f;

    --md-sys-color-outline: #8d9199;
    --md-sys-color-outline-variant: #43474e;
  }
}
```

## 2. Responsive Layout and Breakpoints

Plan layouts using standard Material window width classes mapped to CSS media queries:

| Size Class | CSS Breakpoint | Navigation & Layout Pattern |
| --- | --- | --- |
| **Compact** | `< 600px` | Single-column reading flow, modal bottom sheets, full-width actions, collapsible mobile drawer/overlay. |
| **Medium** | `600px - 839px` | Two-column grid, persistent navigation rail or header, balanced fluid containers. |
| **Expanded** | `840px - 1199px` | Multi-column layout, persistent navigation, master-detail panes. |
| **Large / XL** | `>= 1200px` | Constrained maximum content width (`max-width: 1280px`) with centered layout to maintain optimal reading length. |

## 3. Semantic HTML and Interaction States

- **Native First:** Use native HTML elements (`<button>`, `<dialog>`, `<details>`, `<input>`) before building custom ARIA widgets.
- **Complete States:** Style all interactive states: `:hover`, `:focus-visible`, `:active`, `:disabled`, and `[aria-selected="true"]`.
- **Visible Focus:** Ensure an unobscured focus indicator:
  ```css
  :focus-visible {
    outline: 2px solid var(--md-sys-color-primary);
    outline-offset: 2px;
  }
  ```

## 4. Web Accessibility (WCAG 2.2 AA)

- **Target Size:** Provide at least 48x48 CSS pixel interactive hit targets for buttons and form controls on touch viewports.
- **Color Contrast:** Verify minimum 4.5:1 text contrast and 3:1 non-text contrast in both light and dark themes.
- **Reduced Motion:** Respect user motion preferences:
  ```css
  @media (prefers-reduced-motion: reduce) {
    *, *::before, *::after {
      animation-duration: 0.01ms !important;
      animation-iteration-count: 1 !important;
      transition-duration: 0.01ms !important;
    }
  }
  ```
- **Heading Order:** Maintain logical heading levels (`<h1>` through `<h6>`) independent of visual styling.
- **Dynamic Zoom:** Ensure layouts remain readable and functional at up to 200% browser text zoom without horizontal page scrolling.

