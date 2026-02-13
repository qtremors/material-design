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

function renderNavigation() {
    const path = window.location.pathname;
    const page = path.split("/").pop() || 'index.html';

    // RAIL HTML
    let railHtml = `
    <nav class="md-nav-rail">
        <div class="menu-trigger ripple-target" style="padding: 12px; margin-bottom: 24px; cursor: pointer;">
            <span class="material-symbols-rounded">menu</span>
        </div>
    `;

    NAV_ITEMS.forEach(item => {
        const isActive = item.url === page ? 'active' : '';
        let displayLabel = item.label;
        if(item.label === 'Navigation') displayLabel = 'Nav';
        if(item.label === 'Typography') displayLabel = 'Type';

        railHtml += `
        <a href="${item.url}" class="md-rail-item ${isActive}" title="${item.label}">
            <div class="icon-container"><span class="material-symbols-rounded">${item.icon}</span></div>
            <span class="label">${displayLabel}</span>
        </a>
        `;
    });

    railHtml += `
        <div style="flex:1"></div>
        <div id="themeToggle" class="md-rail-item" style="cursor: pointer;">
             <div class="icon-container"><span class="material-symbols-rounded">dark_mode</span></div>
        </div>
    </nav>
    `;

    // DRAWER HTML
    let drawerHtml = `
    <div id="drawerOverlay" class="md-drawer-overlay"></div>
    <aside id="navDrawer" class="md-drawer">
        <div style="padding: 24px; font: var(--md-sys-typescale-title-large); color: var(--md-sys-color-primary);">
            Material Showcase
        </div>
        <div class="drawer-content">
    `;

    NAV_ITEMS.forEach(item => {
        const isActive = item.url === page ? 'active' : '';
        drawerHtml += `
            <a href="${item.url}" class="drawer-item ${isActive} ripple-target">
                <span class="material-symbols-rounded">${item.icon}</span> ${item.label}
            </a>
        `;
        // Divider after Navigation item in original
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
