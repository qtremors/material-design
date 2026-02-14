/* ==========================================================================
   SHARED LOGIC
   Core functionality and component initialization
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Navigation Injection (if not present)
    if (!document.querySelector('.md-nav-rail')) {
        if (typeof renderNavigation === 'function') renderNavigation();
    }
    
    // 2. Persistence (Theme Style)
    initPersistence();

    // 3. Initialize Components
    // Check if functions exist (loaded via scripts) and call them
    if (window.initRipples) window.initRipples();
    if (window.initNavigation) window.initNavigation(); // Key-binds for drawer
    if (window.initTabs) window.initTabs();
    if (window.initDialogs) window.initDialogs();
    if (window.initSheets) window.initSheets();
    if (window.initSelectionControls) window.initSelectionControls();
    if (window.initSliders) window.initSliders();
    if (window.initInteractions) window.initInteractions(); // Global Delegates
    if (window.initExpressiveAnimations) window.initExpressiveAnimations();
    if (window.initScrollAnimations) window.initScrollAnimations();
});

function initPersistence() {
    const root = document.documentElement;
    let style = 'm3';
    try {
        style = localStorage.getItem('mdStyle') || 'm3';
    } catch (e) {
        console.warn('LocalStorage access denied, falling back to default style.');
    }
    root.setAttribute('data-style', style);
}

// Global update for settings preview (invoked by settings.html if present)
window.refreshSettingsPreview = function() {
    const root = document.documentElement;
    const seed = root.getAttribute('data-seed');
    const theme = root.getAttribute('data-theme');
    const radius = root.getAttribute('data-radius');
    const style = root.getAttribute('data-style');

    console.log(`Settings Refreshed: ${theme}, ${seed}, ${radius}, ${style}`);
}

/* --- 2. NAVIGATION (Drawer, Rail logic overridden in navigation.js, but Keybinds here) --- */
function initNavigation() {
    const menuBtns = document.querySelectorAll('#menuBtn, .menu-trigger');
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
window.initNavigation = initNavigation;
window.toggleDrawer = toggleDrawer;

/* --- 3. THEME TOGGLING KEYBINDS --- */
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
