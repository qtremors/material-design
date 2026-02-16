const NAV_ITEMS = [
    { label: 'Home', icon: 'dashboard', url: 'index.html' },
    { label: 'Buttons', icon: 'smart_button', url: 'buttons.html' },
    { label: 'Inputs', icon: 'text_fields', url: 'inputs.html' },
    { label: 'Cards', icon: 'view_quilt', url: 'cards.html' },
    { label: 'Widgets', icon: 'widgets', url: 'widgets.html' },
    { label: 'Navigation', icon: 'menu_open', url: 'navigation.html' },
    { label: 'Feedback', icon: 'campaign', url: 'feedback.html' },
    { label: 'Typography', icon: 'text_format', url: 'typography.html' },
    { label: 'Playground', icon: 'science', url: 'playground.html' },
    { label: 'Settings', icon: 'settings', url: 'settings.html' }
];

/**
 * Basic HTML escaping to prevent XSS if data becomes dynamic
 */
function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

/**
 * Validate URL to ensure it doesn't contain javascript: protocol
 */
function isValidUrl(url) {
    if (!url) return false;
    // Allow relative paths, anchors, and http/https
    // Block javascript: data: vbscript:
    if (/^\s*(javascript:|vbscript:|data:)/i.test(url)) return false;
    return true;
}

function renderNavigation() {
    let isInSrc = window.location.pathname.includes('/src/');
    const page = window.location.pathname.split("/").pop() || 'index.html';

    const script = document.querySelector('script[src$="navigation.js"]');
    if (script) {
        const src = script.getAttribute('src');
        if (src.includes('src/js/navigation.js')) {
            isInSrc = false;
        } else if (src.endsWith('js/navigation.js')) {
            // Check location pathname as secondary verification
            isInSrc = window.location.pathname.includes('/src/') || src.includes('../');
        }
    }

    const siblingPrefix = isInSrc ? '' : 'src/';
    const homePrefix = isInSrc ? '../' : '';

    // RAIL HTML
    let railHtml = `
    <nav class="md-nav-rail">
        <div class="menu-trigger ripple-target" role="button" tabindex="0" aria-label="Open menu" style="padding: 12px; margin-top: 4px; margin-bottom: 8px; cursor: pointer;">
            <span class="material-symbols-rounded">menu</span>
        </div>
        <div class="nav-scroll-container">
    `;

    NAV_ITEMS.forEach(item => {
        let href = '';
        if (item.url === 'index.html') {
            href = homePrefix + item.url;
        } else {
            href = siblingPrefix + item.url;
        }

        if (!isValidUrl(href)) return;

        const isActive = item.url === page ? 'active' : '';
        let displayLabel = item.label;
        if(item.label === 'Navigation') displayLabel = 'Nav';
        if(item.label === 'Typography') displayLabel = 'Type';

        railHtml += `
        <a href="${escapeHtml(href)}" class="md-rail-item ${isActive}" title="${escapeHtml(item.label)}">
            <div class="icon-container"><span class="material-symbols-rounded">${escapeHtml(item.icon)}</span></div>
            <span class="label">${escapeHtml(displayLabel)}</span>
        </a>
        `;
    });

    railHtml += `
        </div>
        <button id="themeToggle" class="md-rail-item" aria-label="Toggle theme" style="background: transparent; border: none; padding: 0; margin-top: auto; flex-shrink: 0; margin-bottom: 8px;">
             <div class="icon-container"><span class="material-symbols-rounded">dark_mode</span></div>
        </button>
    </nav>
    `;

    // DRAWER HTML
    let drawerHtml = `
    <div id="drawerOverlay" class="md-drawer-overlay"></div>
    <aside id="navDrawer" class="md-drawer">
        <div style="padding: 24px; font: var(--md-sys-typescale-title-large); color: var(--md-sys-color-primary);">
            Material Design
        </div>
        <div class="drawer-content">
    `;

    NAV_ITEMS.forEach(item => {
        let href = '';
        if (item.url === 'index.html') {
            href = homePrefix + item.url;
        } else {
            href = siblingPrefix + item.url;
        }

        if (!isValidUrl(href)) return;

        const isActive = item.url === page ? 'active' : '';
        drawerHtml += `
            <a href="${escapeHtml(href)}" class="drawer-item ${isActive} ripple-target">
                <span class="material-symbols-rounded">${escapeHtml(item.icon)}</span> ${escapeHtml(item.label)}
            </a>
        `;
        if (item.label === 'Typography') {
             drawerHtml += `<hr style="border: 0; border-top: 1px solid var(--md-sys-color-outline-variant); margin: 8px 0;">`;
        }
    });

    drawerHtml += `
        </div>
    </aside>
    `;

    // COLOR SELECTION SHEET (MOBILE)
    const colorSheetHtml = `
    <div id="colorSheet" class="bottom-sheet">
        <div class="sheet-content">
            <div class="sheet-header">
                <div class="sheet-handle"></div>
                <h3 style="text-align: center;">Select Theme Color</h3>
            </div>
            <div class="color-sheet-grid">
                <div class="color-swatch" data-seed="monochrome" style="background: #5F6368;" title="Monochrome"></div>
                <div class="color-swatch" data-seed="blue" style="background: #2962FF;" title="Blue"></div>
                <div class="color-swatch" data-seed="purple" style="background: #6750A4;" title="Purple"></div>
                <div class="color-swatch" data-seed="green" style="background: #006C51;" title="Green"></div>
                <div class="color-swatch" data-seed="teal" style="background: #006A6A;" title="Teal"></div>
                <div class="color-swatch" data-seed="cyan" style="background: #0097A7;" title="Cyan"></div>
                <div class="color-swatch" data-seed="yellow" style="background: #6D5E00;" title="Yellow"></div>
                <div class="color-swatch" data-seed="orange" style="background: #8B5000;" title="Orange"></div>
                <div class="color-swatch" data-seed="pink" style="background: #BC004B;" title="Pink"></div>
                <div class="color-swatch" data-seed="red" style="background: #B3261E;" title="Red"></div>
            </div>
            <div style="height: 24px;"></div>
        </div>
    </div>
    `;

    document.body.insertAdjacentHTML('afterbegin', drawerHtml);
    document.body.insertAdjacentHTML('afterbegin', railHtml);
    
    // Inject Scrim only if not present (Singleton)
    if (!document.getElementById('sheet-scrim')) {
        const scrimHtml = `<div id="sheet-scrim" class="sheet-scrim"></div>`;
        document.body.insertAdjacentHTML('beforeend', scrimHtml);
    }
    
    document.body.insertAdjacentHTML('beforeend', colorSheetHtml);

    // MOBILE TOP BAR UI
    const topBar = document.querySelector('.top-app-bar');
    if (topBar) {
        if (!topBar.querySelector('.menu-trigger')) {
            const menuBtn = `
                <button class="md-btn icon-btn ripple-target menu-trigger mobile-only" aria-label="Open menu">
                    <span class="material-symbols-rounded">menu</span>
                </button>
            `;
            topBar.insertAdjacentHTML('afterbegin', menuBtn);
        }

        if (!topBar.querySelector('[data-target="colorSheet"]')) {
            const paletteToggleField = `
                <button class="md-btn icon-btn ripple-target mobile-only" data-action="open-sheet" data-target="colorSheet" title="Select Color" style="margin-left: 8px;">
                    <span class="material-symbols-rounded">palette</span>
                </button>
            `;
            topBar.insertAdjacentHTML('beforeend', paletteToggleField);
        }

        if (!topBar.querySelector('#mobileThemeToggle')) {
            const toggleHtml = `
                <button id="mobileThemeToggle" class="md-btn icon-btn ripple-target" aria-label="Toggle theme" style="margin-left: 8px; display: none;">
                    <span class="material-symbols-rounded">dark_mode</span>
                </button>
            `;
            topBar.insertAdjacentHTML('beforeend', toggleHtml);
        }
    }

    // Auto-render swatch row if container exists
    renderSwatchRow();

    // Sync icons with current theme state (and active swatch)
    if (window.ThemeEngine) ThemeEngine.updateUI();
}

/**
 * Generator for the color swatch row (replaces duplicated HTML)
 */
function renderSwatchRow() {
    // Find all swatch-row containers that are empty
    const containers = document.querySelectorAll('.swatch-row');
    
    // Define the swatches data (source of truth)
    const swatches = [
        { seed: 'monochrome', color: '#5F6368', title: 'Monochrome' },
        { seed: 'blue', color: '#2962FF', title: 'Blue' },
        { seed: 'purple', color: '#6750A4', title: 'Purple' },
        { seed: 'green', color: '#006C51', title: 'Green' },
        { seed: 'teal', color: '#006A6A', title: 'Teal' },
        { seed: 'cyan', color: '#0097A7', title: 'Cyan' },
        { seed: 'yellow', color: '#6D5E00', title: 'Yellow' },
        { seed: 'orange', color: '#8B5000', title: 'Orange' },
        { seed: 'pink', color: '#BC004B', title: 'Pink' },
        { seed: 'red', color: '#B3261E', title: 'Red' }
    ];

    containers.forEach(container => {
        if (container.children.length > 0) return;
        
        if (!container.style.gap) container.style.gap = '8px';
        if (!container.style.display) container.style.display = 'flex';

        let html = '';
        swatches.forEach(s => {
            html += `<div class="color-swatch" data-seed="${s.seed}" style="background: ${s.color};" title="${s.title}"></div>`;
        });
        container.innerHTML = html;
    });
}
