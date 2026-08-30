# Material 3 Expressive Web Development

This skill provides engineering guidance on implementing **Material 3 Expressive** components, architecture, design tokens, responsive layouts, and accessibility in modern web applications.

## 1. Web Architecture for Material 3

Structure web applications to separate concerns cleanly:

```text
┌─────────────────────────────────────────────────────────┐
│                      Design Tokens                      │
│  - Color roles (Light/Dark themes)                      │
│  - Typography scale & variable font axes                │
│  - Shape corner radiuses & elevation surface tokens     │
│  - Motion durations & easing curves                     │
└───────────────────────────┬─────────────────────────────┘
                            │ consumed by
┌───────────────────────────▼─────────────────────────────┐
│                 Component Architecture                  │
│  - Semantic HTML base (buttons, dialogs, inputs)        │
│  - State management (CSS state classes & ARIA attrs)    │
│  - Encapsulated styles (BEM / CSS Modules / Components) │
└───────────────────────────┬─────────────────────────────┘
                            │ composed into
┌───────────────────────────▼─────────────────────────────┐
│                   Adaptive Layouts                      │
│  - Responsive grid systems & fluid containers           │
│  - Adaptive navigation (drawer, rail, top bar)          │
│  - Master-detail multi-pane layouts                     │
└─────────────────────────────────────────────────────────┘
```

## 2. Component Development Workflow

1. **Semantic HTML Structure:** Build the component using the most semantically accurate HTML elements.
2. **ARIA & Accessibility State:** Expose dynamic state using standard ARIA attributes (`aria-expanded`, `aria-selected`, `aria-disabled`, `aria-controls`).
3. **Design Token Application:** Style using CSS custom properties (`var(--md-sys-color-primary)`, etc.).
4. **Interactive States:** Style `:hover`, `:focus-visible`, `:active`, `:disabled`, and keyboard focus indicators.
5. **Responsive Adaptability:** Ensure the component reflows properly across Compact, Medium, and Expanded viewports.
6. **Reduced Motion:** Provide non-animated or fade transitions when `prefers-reduced-motion: reduce` is active.

## 3. Expressive Web Component Patterns

### Split Button
Combines a primary button with an adjacent dropdown menu trigger:
```html
<div class="md-split-button" role="group" aria-label="Save options">
  <button type="button" class="md-split-button__primary">Save</button>
  <button type="button" class="md-split-button__menu-trigger" aria-haspopup="menu" aria-expanded="false" aria-label="Additional save options">
    <span class="material-symbols-rounded" aria-hidden="true">arrow_drop_down</span>
  </button>
  <ul class="md-menu" role="menu" hidden>
    <li role="menuitem" tabindex="-1">Save as Draft</li>
    <li role="menuitem" tabindex="-1">Save and Publish</li>
  </ul>
</div>
```

### Floating Toolbar
```html
<nav class="md-floating-toolbar" aria-label="Document tools">
  <button type="button" aria-label="Bold"><span class="material-symbols-rounded" aria-hidden="true">format_bold</span></button>
  <button type="button" aria-label="Italic"><span class="material-symbols-rounded" aria-hidden="true">format_italic</span></button>
  <button type="button" aria-label="Link"><span class="material-symbols-rounded" aria-hidden="true">link</span></button>
</nav>
```

```css
.md-floating-toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: var(--md-sys-color-surface-container-high);
  border-radius: var(--md-sys-shape-corner-full);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
```

### Expressive Motion and Transitions
```css
.md-card {
  transition: transform var(--md-sys-motion-duration-short) var(--md-sys-motion-easing-emphasized),
              box-shadow var(--md-sys-motion-duration-short) var(--md-sys-motion-easing-emphasized);
}

.md-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

@media (prefers-reduced-motion: reduce) {
  .md-card {
    transition: none;
    transform: none !important;
  }
}
```

## 4. Verification and Testing

- **Cross-Browser Verification:** Test on Chromium, WebKit/Safari, and Firefox.
- **Viewport Testing:** Validate at 360px (mobile compact), 768px (tablet medium), 1024px (desktop expanded), and 1440px (large screen).
- **Keyboard Navigation:** Confirm all interactive elements are reachable via `Tab` / `Shift+Tab` and operable via `Enter` / `Space` / Arrow keys.
- **Screen Reader Testing:** Test with NVDA / VoiceOver to verify semantic roles, accessible labels, and state updates.
- **Lighthouse / Automated Audits:** Maintain 100% Accessibility and Best Practices scores.

