# Material Design UI and UX for Web

Use this skill to design and review the independent framework-free implementation under `material-web/`.

The Android app may inform visual and behavioral intent, but web components own their markup, state model, tokens, accessibility, tests, and release lifecycle. Do not translate Compose signatures into browser APIs.

## Visual System

- Use semantic custom properties for product UI.
- Raw colors belong only in token definitions, illustrations, or isolated test fixtures.
- Use Material color roles by meaning: surface, container, primary, secondary, tertiary, outline, error, and corresponding `on-*` roles.
- Choose surface-container roles by hierarchy and contrast, not decoration alone.
- Support light and dark schemes. Treat OLED styling as an optional project feature, not a Material requirement.
- Never rely on color, shape, position, or motion alone to communicate state.

## Typography

- Use the Material display, headline, title, body, and label roles consistently.
- Keep body text readable and allow user zoom and font substitution.
- Use Roboto Flex axes only as progressive enhancement.
- Test the minimum and maximum supported axis values for reflow.
- Avoid fixed-height text containers that clip increased line height or translated content.

## Layout and Adaptation

Use content-driven breakpoints and the available viewport:

- Compact: below `600px`.
- Medium: `600px` through `839px`.
- Expanded: `840px` and above.

These ranges guide testing; they do not identify a device type.

- Prefer one-pane composition in compact space.
- Add navigation rails or supporting panes only when the task benefits.
- Use expanded space for stronger hierarchy or multi-pane layouts; do not simply stretch compact UI.
- Preserve state, focus, and action outcomes when layout or input mode changes.
- Verify reflow at 200% zoom, narrow landscape layouts, long text, and browser text enlargement.

## Shape and Targets

- Use project shape tokens rather than scattered radii.
- Choose shape by component role and state.
- Prefer at least `48x48px` targets for primary custom controls.
- Never fall below WCAG 2.2 target-size requirements without an applicable exception.
- Maintain enough spacing to prevent adjacent-target mistakes.

## Elevation and Interaction States

- Use tonal surface roles, borders, and shadows consistently.
- Do not depend on shadow alone in dark or forced-color modes.
- Implement the states that apply: enabled, disabled, hover, focus, pressed, selected, dragged, loading, success, and error.
- A ripple is optional presentation, not the sole indicator.
- Keep a clearly visible focus indicator and ensure sticky or floating UI does not obscure focused elements.
- Preserve native control semantics unless a custom interaction genuinely requires an established ARIA pattern.

## Motion

- Centralize duration and easing values as project motion tokens.
- Use the shortest motion that preserves continuity and comprehension.
- Keep animations interruptible and reversible.
- Avoid decorative overshoot where it can imply a state change or obscure content.
- Respect `prefers-reduced-motion`; preserve state, completion, and safety timing while removing unnecessary movement.

## Accessibility Baseline

- Use semantic HTML first.
- Provide accessible names that match visible labels.
- Support keyboard, pointer, touch, screen readers, high contrast, and forced colors.
- Maintain text and non-text contrast.
- Do not trap focus or unexpectedly move it.
- Announce asynchronous status changes when necessary without excessive live-region output.
- Test at 200% zoom and with content reflow.

## Review Checklist

- Semantic roles and token names express intent.
- Light, dark, high-contrast, and forced-color conditions remain legible.
- Typography and layout survive long text and zoom.
- All applicable interaction states are visible and semantic.
- Keyboard focus is visible and unobscured.
- Motion is interruptible and reduced-motion safe.
- Compact, medium, and expanded layouts preserve content and state.
- Intentional differences from Android are documented.
