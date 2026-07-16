/* ==========================================================================
   TABS COMPONENT
   ========================================================================== */

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
        const panel = document.getElementById(targetId);
        if (panel) {
             const context = panel.parentElement;
             if (context) {
                 context.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
             } else {
                 document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
             }
             panel.classList.add('active');
        }
    }
}

function switchTab(tabElement) {
    if (tabElement) tabElement.click();
}

window.initTabs = initTabs;
window.handleTabSwitch = handleTabSwitch;
window.switchTab = switchTab;
