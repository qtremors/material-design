const NAV_ITEMS = [
    { label: 'Home', icon: 'dashboard', url: 'index.html' },
    { label: 'Buttons', icon: 'smart_button', url: 'buttons.html' },
    { label: 'Inputs', icon: 'text_fields', url: 'inputs.html' },
    { label: 'Cards', icon: 'view_quilt', url: 'cards.html' },
    { label: 'Navigation', icon: 'menu_open', url: 'navigation.html' },
    { label: 'Feedback', icon: 'campaign', url: 'feedback.html' },
    { label: 'Typography', icon: 'text_format', url: 'typography.html' },
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
            isInSrc = true;
        }
    }

    const siblingPrefix = isInSrc ? '' : 'src/';
    const homePrefix = isInSrc ? '../' : '';

    // RAIL HTML
    let railHtml = `
    <nav class="md-nav-rail">
        <div class="menu-trigger ripple-target" role="button" tabindex="0" aria-label="Open menu" style="padding: 12px; margin-bottom: 24px; cursor: pointer;">
            <span class="material-symbols-rounded">menu</span>
        </div>
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
        <div style="flex:1"></div>
        <div id="themeToggle" class="md-rail-item" role="button" tabindex="0" aria-label="Toggle theme" style="cursor: pointer;">
             <div class="icon-container"><span class="material-symbols-rounded">dark_mode</span></div>
        </div>
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
        if (item.label === 'Navigation') {
             drawerHtml += `<hr style="border: 0; border-top: 1px solid var(--md-sys-color-outline-variant); margin: 8px 0;">`;
        }
    });

    drawerHtml += `
        </div>
    </aside>
    `;

    document.body.insertAdjacentHTML('afterbegin', drawerHtml);
    document.body.insertAdjacentHTML('afterbegin', railHtml);
}
