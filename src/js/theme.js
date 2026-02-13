/**
 * THEME ENGINE
 * Handles Theme, Color, Radius, and Font persistence across pages.
 */

const ThemeEngine = {
    // Default Config
    state: {
        theme: 'light',
        seed: 'blue',
        radius: 'medium'
    },

    /**
     * Initialize the theme engine.
     * Runs immediately to prevent flash of unstyled content.
     */
    init() {
        this.load();
        this.apply();
        
        // Expose global helper
        window.setThemeConfig = (key, value) => {
            this.state[key] = value;
            this.save();
            this.apply();
        };

        // Wait for DOM to bind UI elements
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => this.bindUI());
        } else {
            this.bindUI();
        }
    },

    /**
     * Load state from Storage (Dual Strategy)
     */
    load() {
        // 1. Try LocalStorage (Standard Web)
        try {
            const lsTheme = localStorage.getItem('theme');
            const lsSeed = localStorage.getItem('seed');
            const lsRadius = localStorage.getItem('radius');
            
            if (lsTheme) this.state.theme = lsTheme;
            if (lsSeed) this.state.seed = lsSeed;
            if (lsRadius) this.state.radius = lsRadius;
        } catch (e) {
            // LocalStorage might be blocked or partitioned
        }

        // 2. Try window.name (file sync support)
        // Checks if the window name contains a valid JSON config from a previous page
        try {
            if (window.name && window.name.startsWith('{')) {
                const sessionState = JSON.parse(window.name);
                if (sessionState.theme && sessionState.seed) {
                    this.state = { ...this.state, ...sessionState };
                }
            }
        } catch (e) {}
    },

    /**
     * Save state to Storage
     */
    save() {
        // 1. LocalStorage
        try {
            localStorage.setItem('theme', this.state.theme);
            localStorage.setItem('seed', this.state.seed);
            localStorage.setItem('radius', this.state.radius);
        } catch (e) {}

        // 2. window.name (Sync for next page load)
        try {
            window.name = JSON.stringify(this.state);
        } catch (e) {}
    },

    /**
     * Apply current state to the DOM (HTML tag)
     */
    apply() {
        const root = document.documentElement;
        root.setAttribute('data-theme', this.state.theme);
        root.setAttribute('data-seed', this.state.seed);
        root.setAttribute('data-radius', this.state.radius);
        
        // Also update UI if active
        this.updateUI();
    },

    /**
     * Bind click listeners to theme toggles and color swatches
     */
    bindUI() {
        // Update icon on load if it exists (for static pages)
        this.updateAllThemeIcons();
        this.updateUI();
    },

    /**
     * Update active states of UI elements
     */
    updateUI() {
        // Update All Toggle Icons
        this.updateAllThemeIcons();

        // Update Swatches Active State
        const swatches = document.querySelectorAll('.color-swatch');
        swatches.forEach(s => {
            if (s.getAttribute('data-seed') === this.state.seed) s.classList.add('active');
            else s.classList.remove('active');
        });
        
        // Settings Page Specific Updates (if functions exist)
        if(typeof window.updateSettingsUI === 'function') {
            window.updateSettingsUI();
        }
    },

    updateAllThemeIcons() {
        const toggles = document.querySelectorAll('#themeToggle, #mobileThemeToggle');
        toggles.forEach(btn => {
            const icon = btn.querySelector('.material-symbols-rounded, .material-symbols-outlined');
            if (icon) {
                icon.innerText = this.state.theme === 'dark' ? 'light_mode' : 'dark_mode';
            }
        });
    }
};

// Run immediately
ThemeEngine.init();
