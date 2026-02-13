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
    initSliders(); // Slider Progress Tracker
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

/* --- 6. SLIDERS --- */
function initSliders() {
    const updateSlider = (slider) => {
        const val = (slider.value - slider.min) / (slider.max - slider.min) * 100;
        const container = slider.closest('.slider-container');
        if (container) {
            container.style.setProperty('--slider-value', val + '%');
        }
    };

    document.querySelectorAll('.md-slider').forEach(slider => {
        slider.addEventListener('input', () => updateSlider(slider));
        updateSlider(slider); // Initial state
    });
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

        // Generic Segment Pick (Single Select)
        const segBtn = e.target.closest('[data-action="segment-pick"], [data-action="segment-pick-opt"]');
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

        // Icon Toolbar Toggle
        const toolbarTrigger = e.target.closest('[data-action="toggle-toolbar"]');
        if (toolbarTrigger) {
             // Only toggle if clicking the trigger button OR if it's already open (to allow closing by clicking elsewhere if needed, but here simple toggle)
             // Actually, let's make it so clicking the *trigger* button toggles it. 
             // If it's collapsed, any click opens it. 
             const toolbar = toolbarTrigger;
             if(toolbar.classList.contains('collapsed')) {
                 toolbar.classList.remove('collapsed');
             } else {
                 // If open, only close if clicking the trigger again? 
                 // For this demo, let's just toggle on click of the container or specific close button.
                 // Let's use the trigger button logic.
                 const btn = e.target.closest('.trigger');
                 if(btn) toolbar.classList.add('collapsed');
             }
             return;
        }

         // FAB Morph
         const fabMorph = e.target.closest('[data-action="morph"]');
         if (fabMorph) {
             if(fabMorph.classList.contains('square')) {
                 fabMorph.classList.remove('square');
                 fabMorph.classList.add('circle');
             } else {
                 fabMorph.classList.remove('circle');
                 fabMorph.classList.add('square');
             }
             return;
         }

         // Speed Dial Toggle
         const speedDialTrigger = e.target.closest('[data-action="toggle-speed-dial"]');
         if (speedDialTrigger) {
             const container = speedDialTrigger.closest('.speed-dial-container');
             if(container) container.classList.toggle('open');
             return;
         }

         // Horizontal Drawer Toggle
         const hDrawerTrigger = e.target.closest('[data-action="toggle-horizontal-drawer"]');
         if (hDrawerTrigger) {
             const container = hDrawerTrigger.closest('.horizontal-drawer-container');
             const drawer = container.querySelector('.horizontal-drawer');
             const icon = hDrawerTrigger.querySelector('span');
             
             // Check computed style or inline style
             if (drawer.style.width === '0px' || drawer.style.width === '' || drawer.style.width === '0') {
                 drawer.style.width = '240px'; 
                 drawer.style.opacity = '1';
                 icon.innerText = 'close';
                 icon.style.transform = 'rotate(180deg)';
             } else {
                 drawer.style.width = '0';
                 drawer.style.opacity = '0';
                 icon.innerText = 'add';
                 icon.style.transform = 'rotate(0deg)';
             }
             return;
         }

        // Drawer Item Pick (Demo)
        const drawerItem = e.target.closest('[data-action="drawer-item-pick"]');
        if (drawerItem) {
            const container = drawerItem.parentElement;
            container.querySelectorAll('.drawer-item').forEach(item => item.classList.remove('active'));
            drawerItem.classList.add('active');
            
            // Optional: Update content area text for demo
            const contentArea = container.parentElement.querySelector('div[style*="absolute"] span');
            if(contentArea) {
                contentArea.innerText = drawerItem.innerText.trim();
                contentArea.style.opacity = '1';
            }
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

/* --- 8. EXPRESSIVE ANIMATIONS --- */
function initExpressiveAnimations() {
    // Wavy Progress Loop
    setInterval(() => {
        document.querySelectorAll('.progress-wavy .bar').forEach(bar => {
            const width = Math.random() * 60 + 20; // Random between 20-80%
            bar.style.width = `${width}%`;
        });
    }, 2000);

    // Segmented Progress Loop
    const segments = document.querySelectorAll('.progress-segmented .segment');
    let segmentIndex = 0;
    
    if(segments.length > 0) {
        setInterval(() => {
            segments.forEach(s => s.classList.remove('filled'));
            for(let i=0; i<=segmentIndex; i++) {
                if(segments[i]) segments[i].classList.add('filled');
            }
            segmentIndex++;
            if(segmentIndex >= segments.length) segmentIndex = 0;
        }, 1000);
    }
}
// Add to init
document.addEventListener('DOMContentLoaded', () => {
    initExpressiveAnimations();
});
