/* ==========================================================================
   RIPPLE EFFECT
   ========================================================================== */

function initRipples() {
    document.addEventListener('click', (e) => {
        const target = e.target.closest('.ripple-target, .btn, .md-rail-item, .md-drawer-item, .md-tab, .chip, .card-action');
        if (target && !target.disabled && !target.classList.contains('no-ripple')) {
            createRipple(e, target);
        }
    });
}

function createRipple(event, element) {
    if(element.classList.contains('switch') || element.classList.contains('checkbox-container')) return;

    const circle = document.createElement("span");
    const diameter = Math.max(element.clientWidth, element.clientHeight);
    const radius = diameter / 2;
    const rect = element.getBoundingClientRect();

    circle.style.width = circle.style.height = `${diameter}px`;
    circle.style.left = `${event.clientX - rect.left - radius}px`;
    circle.style.top = `${event.clientY - rect.top - radius}px`;
    circle.classList.add("ripple");

    // Check for custom ripple color
    const customColor = element.getAttribute('data-ripple-color');
    if (customColor) {
        // Map common role names to tokens, or use raw value
        if (customColor.startsWith('on-') || customColor.startsWith('primary') || customColor.startsWith('secondary') || customColor.startsWith('tertiary') || customColor.startsWith('error')) {
             circle.style.backgroundColor = `var(--md-sys-color-${customColor})`;
        } else {
             circle.style.backgroundColor = customColor;
        }
        // Ensure opacity override if needed, though usually handled by ::before/::after states. 
        // For the ripple span itself, we might want to keep the .12 opacity defined in CSS base.css
    }

    const existingRipple = element.querySelector('.ripple');
    if (existingRipple) existingRipple.remove();

    circle.addEventListener('animationend', () => circle.remove());
    element.appendChild(circle);
}

// Expose to global scope for scripts.js to call
window.initRipples = initRipples;
window.createRipple = createRipple;
