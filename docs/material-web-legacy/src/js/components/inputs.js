/* ==========================================================================
   INPUTS & SELECTION CONTROLS
   ========================================================================== */

/**
 * Initializes selection controls (chips).
 */
function initSelectionControls() {
    document.querySelectorAll('.chip').forEach(chip => {

        const isToggle = chip.getAttribute('data-toggle') === 'true' || chip.classList.contains('filter-chip');
        if(isToggle) {
             chip.setAttribute('role', 'button');
             chip.setAttribute('tabindex', '0');
        }

        chip.addEventListener('click', () => toggleChip(chip));
        chip.addEventListener('keydown', (e) => {
            if(e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleChip(chip);
            }
        });
    });
}

/**
 * Toggles the state of a chip.
 * @param {HTMLElement} chip - The chip element to toggle.
 */
function toggleChip(chip) {
    if(chip.getAttribute('data-toggle') === 'true' || chip.classList.contains('filter-chip')) {
        chip.classList.toggle('active');
        
        const icon = chip.querySelector('.check-icon');
        
        if(chip.classList.contains('active')) {
            if(!icon) {
                const newIcon = document.createElement('span');
                newIcon.className = 'material-symbols-rounded check-icon';
                newIcon.innerText = 'check';
                chip.prepend(newIcon);
            }
        } else {
            if(icon) icon.remove();
        }
    }
}

window.initSelectionControls = initSelectionControls;
window.toggleChip = toggleChip;
