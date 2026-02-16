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
        if (customColor.startsWith('on-') || customColor.startsWith('primary') || customColor.startsWith('secondary') || customColor.startsWith('tertiary') || customColor.startsWith('error')) {
             circle.style.backgroundColor = `var(--md-sys-color-${customColor})`;
        } else {
             circle.style.backgroundColor = customColor;
        }
    }

    const existingRipple = element.querySelector('.ripple');
    if (existingRipple) existingRipple.remove();

    circle.addEventListener('animationend', () => circle.remove());
    element.appendChild(circle);
}

// Expose for global usage
window.initRipples = initRipples;
window.createRipple = createRipple;
