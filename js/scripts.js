/* =========================================
   SHARED LOGIC
   Consolidated from all source files
   ========================================= */

document.addEventListener('DOMContentLoaded', () => {
    initRipples();
    initNavigation();
    initTheme();
    initTabs();
    initDialogs();
    initSheets();
    initSelectionControls();
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
    // Do not add ripple to specific inputs if they trigger elsewhere
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

    if (menuBtns.length && drawer && overlay) {
        menuBtns.forEach(btn => {
            btn.addEventListener('click', toggleDrawer);
        });
        overlay.addEventListener('click', toggleDrawer);
    }
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
function initTheme() {
    const themeBtn = document.getElementById('themeToggle');
    if (themeBtn) {
        themeBtn.addEventListener('click', () => {
            const current = document.body.getAttribute('data-theme');
            const newTheme = current === 'dark' ? 'light' : 'dark';
            document.body.setAttribute('data-theme', newTheme);
            
            // Update Icon
            const icon = themeBtn.querySelector('.material-symbols-rounded, .material-symbols-outlined');
            if(icon) icon.innerText = newTheme === 'dark' ? 'light_mode' : 'dark_mode';
        });
    }

    // Color Seeds
    const swatches = document.querySelectorAll('.color-swatch');
    swatches.forEach(swatch => {
        swatch.addEventListener('click', () => {
            const seed = swatch.getAttribute('data-seed');
            setThemeColor(seed, swatch);
        });
    });

}

function setThemeColor(seed, element) {
    document.body.setAttribute('data-seed', seed);
    if(element) {
        document.querySelectorAll('.color-swatch').forEach(sw => sw.classList.remove('active'));
        element.classList.add('active');
    }
}

/* --- 4. TABS --- */
function initTabs() {
    const tabs = document.querySelectorAll('.md-tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // Find parent group
            const group = tab.parentElement;
            if(!group) return;
            
            // Deactivate siblings
            group.querySelectorAll('.md-tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // Find target content
            const targetId = tab.getAttribute('data-target');
            if(targetId) {
                // Deactivate all panels in the same generic container?
                // Assuming tabs and panels are siblings or known structure. 
                // Simple implementation: Hide all panels, show target.
                document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
                const target = document.getElementById(targetId);
                if(target) target.classList.add('active');
            }
        });
    });
}

function switchTab(tabElement, contentId) {
    // Legacy helper if called directly
    tabElement.click();
}

/* --- 5. COMPONENT INTERACTIONS --- */
function toggleLoading(btn) {
    btn.classList.toggle('is-loading');
}

function initDialogs() {
    // Generic closer
    window.openDialog = (id) => {
        const dialog = document.getElementById(id || 'defaultDialog');
        if(!dialog) return;

        // Parent Backdrop
        const backdrop = dialog.closest('.dialog-backdrop') || document.getElementById('dialog-backdrop');
        
        // If multiple dialogs share a backdrop, toggle visibility
        if(backdrop) {
             backdrop.querySelectorAll('.dialog').forEach(d => d.classList.add('hidden'));
             dialog.classList.remove('hidden');
             backdrop.classList.add('open');
        }
    }

    window.closeDialog = (event, id) => {
        if(event) event.stopPropagation(); // Prevent ripple or other clicks
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
    // Chip Toggling
    document.querySelectorAll('.chip').forEach(chip => {
        chip.addEventListener('click', () => {
            if(chip.getAttribute('data-toggle') === 'true') {
                chip.classList.toggle('active');
                // Optional: Toggle check icon logic
                const icon = chip.querySelector('.material-symbols-rounded.check-icon');
                if(chip.classList.contains('active')) {
                    if(!icon) {
                        const newIcon = document.createElement('span');
                        newIcon.className = 'material-symbols-rounded check-icon';
                        newIcon.innerText = 'check';
                        newIcon.style.fontSize = '18px';
                        newIcon.style.marginRight = '8px';
                        chip.prepend(newIcon);
                    }
                } else {
                    if(icon) icon.remove();
                }
            }
        });
    });
}
