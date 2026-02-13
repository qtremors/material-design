/* ==========================================================================
   SHARED LOGIC
   Core functionality and component initialization
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    if (!document.querySelector('.md-nav-rail')) {
        if (typeof renderNavigation === 'function') renderNavigation();
    }
    
    initRipples();
    initNavigation();
    initTabs();
    initDialogs();
    initSheets();
    initSelectionControls();
    initInteractions(); // Generalized Interaction Handler
});

/* --- 1. RIPPLE ENGINE --- */
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

    const existingRipple = element.querySelector('.ripple');
    if (existingRipple) existingRipple.remove();

    circle.addEventListener('animationend', () => circle.remove());
    element.appendChild(circle);
}

/* --- 2. NAVIGATION (Drawer, Rail) --- */
function initNavigation() {
    const menuBtns = document.querySelectorAll('#menuBtn, .menu-trigger');
    const drawer = document.getElementById('navDrawer');
    const overlay = document.getElementById('drawerOverlay');

    if (menuBtns.length > 0) {
        document.body.addEventListener('click', (e) => {
            if (e.target.closest('.menu-trigger') || e.target.closest('#menuBtn') || e.target.closest('[data-action="toggle-drawer"]')) {
                toggleDrawer();
            }
        });
        
        if (overlay) overlay.addEventListener('click', toggleDrawer);
    }
    

    document.body.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            const trigger = e.target.closest('.menu-trigger');
            if (trigger) {
                e.preventDefault();
                toggleDrawer();
            }
        }
    });
}

function toggleDrawer() {
    const drawer = document.getElementById('navDrawer');
    const overlay = document.getElementById('drawerOverlay');
    if (!drawer || !overlay) return;

    const isOpen = drawer.classList.contains('open');
    if (isOpen) {
        drawer.classList.remove('open');
        overlay.classList.remove('open');
    } else {
        drawer.classList.add('open');
        overlay.classList.add('open');
    }
}

/* --- 3. THEME TOGGLING --- */

document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
        const themeToggle = e.target.closest('#themeToggle');
        if (themeToggle) {
            e.preventDefault();
             themeToggle.click();
        }
        
        const swatch = e.target.closest('.color-swatch');
        if (swatch) {
             e.preventDefault();
             swatch.click();
        }
    }
});


/* --- 4. TABS --- */
function initTabs() {
    const tabs = document.querySelectorAll('.md-tab');
    tabs.forEach(tab => {
        tab.setAttribute('role', 'tab');
        tab.setAttribute('tabindex', '0');
        
        tab.addEventListener('click', () => {
             handleTabSwitch(tab);
        });
        
        tab.addEventListener('keydown', (e) => {
            if(e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                handleTabSwitch(tab);
            }
        });
    });
}

function handleTabSwitch(tab) {
    const group = tab.parentElement;
    if(!group) return;
    
    group.querySelectorAll('.md-tab').forEach(t => {
        t.classList.remove('active');
        t.setAttribute('aria-selected', 'false');
    });
    tab.classList.add('active');
    tab.setAttribute('aria-selected', 'true');

    const targetId = tab.getAttribute('data-target');
    if(targetId) {
        const context = group.parentElement;
        
        if (context) {
             context.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
             const target = context.querySelector(`#${targetId}`) || document.getElementById(targetId);
             if(target) target.classList.add('active');
        } else {
             document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
             const target = document.getElementById(targetId);
             if(target) target.classList.add('active');
        }
    }
}

function switchTab(tabElement, contentId) {
    tabElement.click();
}

/* --- 5. COMPONENT INTERACTIONS & DIALOGS --- */

function initDialogs() {
    // Generic closer
    window.openDialog = (id) => {
        const dialog = document.getElementById(id || 'defaultDialog');
        if(!dialog) return;

        const backdrop = dialog.closest('.dialog-backdrop') || document.getElementById('dialog-backdrop');
        
        if(backdrop) {
             backdrop.querySelectorAll('.dialog').forEach(d => d.classList.add('hidden'));
             dialog.classList.remove('hidden');
             backdrop.classList.add('open');
        }
    }

    window.closeDialog = (event, id) => {
        if(event) event.stopPropagation();
        const backdrop = id ? document.getElementById(id) : document.querySelector('.dialog-backdrop.open');
        if(backdrop) backdrop.classList.remove('open');
    }
}

function initSheets() {
    window.openSheet = (id) => {
        const sheet = document.getElementById(id || 'bottom-sheet');
        const scrim = document.getElementById('sheet-scrim');
        if(sheet) sheet.classList.add('open');
        if(scrim) {
            scrim.style.opacity = '1'; 
            scrim.style.pointerEvents = 'auto';
        }
    }

    window.closeSheet = () => {
        document.querySelectorAll('.bottom-sheet').forEach(s => s.classList.remove('open'));
        const scrim = document.getElementById('sheet-scrim');
        if(scrim) {
            scrim.style.opacity = '0';
            scrim.style.pointerEvents = 'none';
        }
    }
}

function showSnackbar(text) {
    const snackbar = document.getElementById('snackbar');
    if(!snackbar) return;
    
    if(text) snackbar.querySelector('span').innerText = text;
    snackbar.classList.add('show');
    
    setTimeout(() => {
        snackbar.classList.remove('show');
    }, 3000);
}

/* --- 6. SELECTION CONTROLS --- */
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


/* --- 7. GLOBAL INTERACTION DELEGATION --- */
function initInteractions() {
    document.body.addEventListener('click', (e) => {
        
        const loadBtn = e.target.closest('[data-action="toggle-loading"]');
        if (loadBtn && !loadBtn.disabled) {
            loadBtn.classList.toggle('is-loading');
            return;
        }

        const segBtn = e.target.closest('[data-action="segment-pick"]');
        if (segBtn && !segBtn.disabled) {
            const group = segBtn.closest('.segmented-btn-group');
            if(group) {
                group.querySelectorAll('.segmented-btn').forEach(b => b.classList.remove('selected'));
                segBtn.classList.add('selected');
            }
            return;
        }

        const multiBtn = e.target.closest('[data-action="segment-toggle"]');
        if (multiBtn && !multiBtn.disabled) {
            multiBtn.classList.toggle('selected');
            return;
        }

        const openDialogBtn = e.target.closest('[data-action="open-dialog"]');
        if (openDialogBtn && !openDialogBtn.disabled) {
            const targetId = openDialogBtn.getAttribute('data-target');
            if (window.openDialog) window.openDialog(targetId);
            return;
        }

        const closeDialogBtn = e.target.closest('[data-action="close-dialog"]');
        if (closeDialogBtn && !closeDialogBtn.disabled) {
            if (window.closeDialog) window.closeDialog(e);
            return;
        }

        const openSheetBtn = e.target.closest('[data-action="open-sheet"]');
        if (openSheetBtn && !openSheetBtn.disabled) {
             const targetId = openSheetBtn.getAttribute('data-target');
             if(window.openSheet) window.openSheet(targetId);
             return;
        }

        const closeSheetBtn = e.target.closest('[data-action="close-sheet"]');
        if (closeSheetBtn && !closeSheetBtn.disabled) {
             if(window.closeSheet) window.closeSheet();
             return;
        }

        const snackbarBtn = e.target.closest('[data-action="show-snackbar"]');
        if (snackbarBtn && !snackbarBtn.disabled) {
             const text = snackbarBtn.getAttribute('data-text');
             if(window.showSnackbar) window.showSnackbar(text);
             return;
        }
    });

    document.body.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            const target = e.target.closest('[data-action]');
            if(target) {
                e.preventDefault();
                target.click();
            }
        }
    });
    
    document.body.addEventListener('click', (e) => {
        if (e.target.classList.contains('dialog-backdrop')) {
            window.closeDialog(e);
        }
        if (e.target.classList.contains('sheet-scrim')) {
            window.closeSheet();
        }
    });
}
