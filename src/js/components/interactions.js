/* ==========================================================================
   GLOBAL INTERACTION DELEGATION
   ========================================================================== */

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
             const toolbar = toolbarTrigger;
             const isTriggerClick = e.target.closest('.trigger');

             if(toolbar.classList.contains('collapsed')) {
                 toolbar.classList.remove('collapsed');
                 return;
             } else if (isTriggerClick) {
                 toolbar.classList.add('collapsed');
                 return;
             }
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

         // Horizontal Speed Dial Toggle
         const hSpeedDialTrigger = e.target.closest('[data-action="toggle-horizontal-speed-dial"]');
         if (hSpeedDialTrigger) {
             const container = hSpeedDialTrigger.closest('.horizontal-speed-dial-container');
             if (!container) return;

             const speedDial = container.querySelector('.horizontal-speed-dial');
             const icon = hSpeedDialTrigger.querySelector('span');
             
             if (!speedDial || !icon) return;

             // Check computed style or inline style
             if (speedDial.style.width === '0px' || speedDial.style.width === '' || speedDial.style.width === '0') {
                 speedDial.style.width = '240px'; 
                 speedDial.style.opacity = '1';
                 icon.innerText = 'close';
                 icon.style.transform = 'rotate(180deg)';
             } else {
                 speedDial.style.width = '0';
                 speedDial.style.opacity = '0';
                 icon.innerText = 'add';
                 icon.style.transform = 'rotate(0deg)';
             }
             return;
         }

        // Drawer Item Pick (Demo)
        const drawerItem = e.target.closest('[data-action="drawer-item-pick"]');
        if (drawerItem) {
            const container = drawerItem.parentElement;
            if (!container) return; // Safety guard
            
            container.querySelectorAll('.drawer-item').forEach(item => item.classList.remove('active'));
            drawerItem.classList.add('active');
            
            // Update content area text for demo
            if (container.parentElement) {
                const contentArea = container.parentElement.querySelector('div[style*="absolute"] span');
                if(contentArea) {
                    contentArea.innerText = drawerItem.innerText.trim();
                    contentArea.style.opacity = '1';
                }
            }
            return;
        }

        // TextField Clear Logic
        const clearBtn = e.target.closest('[data-action="clear-input"]');
        if (clearBtn) {
            const field = clearBtn.closest('.md-field, .expressive-input-group');
            if (field) {
                const input = field.querySelector('input');
                if (input) {
                    input.value = '';
                    input.focus();
                    input.dispatchEvent(new Event('input', { bubbles: true }));
                }
            }
            return;
        }

        // Icon Toolbar Item Selection
        const toolbarItem = e.target.closest('.icon-toolbar .icon-btn:not(.trigger)');
        if (toolbarItem) {
            const toolbar = toolbarItem.closest('.icon-toolbar');
            if (toolbar) {
                toolbar.querySelectorAll('.icon-btn').forEach(btn => btn.classList.remove('active'));
                toolbarItem.classList.add('active');
            }
            return;
        }


        // Generic Theme Toggle (Used by Mobile Toggle & Rail)
        const themeToggle = e.target.closest('#themeToggle, #mobileThemeToggle');
        if (themeToggle) {
             const current = document.documentElement.getAttribute('data-theme') || 'light';
             const prev = document.documentElement.getAttribute('data-prev-theme') || 'light';
             
             let next = 'dark';

             // Cycle: Light -> Dark -> OLED -> Dark -> Light
             if (current === 'light') {
                 next = 'dark';
             } else if (current === 'dark') {
                 if (prev === 'light') next = 'oled';
                 else next = 'light';
             } else if (current === 'oled') {
                 next = 'dark';
             }

             if (typeof window.setThemeConfig === 'function') {
                 window.setThemeConfig('theme', next);
             }
             return;
        }

        // Color Swatches (Header)
        const swatch = e.target.closest('.color-swatch');
        if (swatch) {
            const seed = swatch.getAttribute('data-seed');
            if (typeof window.setThemeConfig === 'function') {
                window.setThemeConfig('seed', seed);
                if (typeof window.closeSheet === 'function') window.closeSheet();
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
            if (typeof window.closeDialog === 'function') window.closeDialog(e);
        }
        if (e.target.classList.contains('sheet-scrim')) {
            if (typeof window.closeSheet === 'function') window.closeSheet();
        }
    });
}

window.initInteractions = initInteractions;
