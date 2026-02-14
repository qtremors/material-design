/* ==========================================================================
   INPUTS & SELECTION CONTROLS
   ========================================================================== */

function initSelectionControls() {
    document.querySelectorAll('.chip').forEach(chip => {

        if(chip.classList.contains('active') || chip.getAttribute('data-toggle') === 'true') {
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

function toggleChip(chip) {
    if(chip.getAttribute('data-toggle') === 'true' || chip.classList.contains('filter-chip')) {
        chip.classList.toggle('active');
        
        const icon = chip.querySelector('.check-icon');
        
        if(chip.classList.contains('active')) {
            if(!icon) {
                const newIcon = document.createElement('span');
                newIcon.className = 'material-symbols-rounded check-icon';
                newIcon.innerText = 'check';
                newIcon.style.fontSize = '18px';
                newIcon.style.marginRight = '8px';
                newIcon.style.marginLeft = '-4px';
                chip.prepend(newIcon);
            }
        } else {
            if(icon) icon.remove();
        }
    }
}

window.initSelectionControls = initSelectionControls;
window.toggleChip = toggleChip;
