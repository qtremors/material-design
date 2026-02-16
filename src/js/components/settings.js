/**
 * Settings Page Logic
 */

window.updateSettingsUI = function() {
    const root = document.documentElement;
    
    // Theme
    const currentTheme = root.getAttribute('data-theme') || 'light';
    document.querySelectorAll('[id^="btn-theme-"]').forEach(btn => {
        if(btn.id.includes(currentTheme)) btn.classList.add('active');
        else btn.classList.remove('active');
    });

    // Seed
    const currentSeed = root.getAttribute('data-seed') || 'blue';
    document.querySelectorAll('.color-option').forEach(opt => {
        if(opt.dataset.color === currentSeed) opt.classList.add('active');
        else opt.classList.remove('active');
    });

}

// Interaction Delegation for Settings
document.addEventListener('click', (e) => {
    const target = e.target;
    
    const modeBtn = target.closest('[data-action="set-theme-mode"]');
    if(modeBtn) {
        if (typeof window.setThemeConfig === 'function') {
            window.setThemeConfig('theme', modeBtn.dataset.value);
        }
        return;
    }

    const seedBtn = target.closest('[data-action="set-theme-seed"]');
    if(seedBtn) {
        if (typeof window.setThemeConfig === 'function') {
            window.setThemeConfig('seed', seedBtn.dataset.value);
        }
        return;
    }
    
    const resetBtn = target.closest('[data-action="reset-settings"]');
    if(resetBtn) {
        localStorage.removeItem('theme');
        localStorage.removeItem('seed');
        localStorage.removeItem('radius');
        
        window.name = ''; 
        location.reload();
        return;
    }
});

// Initial Run
window.addEventListener('DOMContentLoaded', () => {
    if (window.updateSettingsUI) window.updateSettingsUI();
});
