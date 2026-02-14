/* ==========================================================================
   BOTTOM SHEETS COMPONENT
   ========================================================================== */

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

let snackbarTimeoutId = null;

function showSnackbar(text, type = 'info') {
    const snackbar = document.getElementById('snackbar');
    if(!snackbar) return;
    
    // Clear any existing timeout to avoid early hiding
    if (snackbarTimeoutId) {
        clearTimeout(snackbarTimeoutId);
        snackbarTimeoutId = null;
    }

    // Reset classes
    snackbar.classList.remove('toast-success', 'toast-error', 'toast-info', 'toast-warning');
    if(type !== 'default') snackbar.classList.add(`toast-${type}`);

    // Set icon based on type
    let icon = '';
    switch(type) {
        case 'success': icon = 'check_circle'; break;
        case 'error': icon = 'error'; break;
        case 'warning': icon = 'warning'; break;
        case 'info': icon = 'info'; break;
    }

    const content = snackbar.querySelector('.content') || snackbar.querySelector('span');
    if(content) {
        content.innerHTML = ''; // Clear existing
        if(icon) {
            const iconSpan = document.createElement('span');
            iconSpan.className = 'material-symbols-rounded';
            iconSpan.textContent = icon;
            content.appendChild(iconSpan);
        }
        const textSpan = document.createElement('span');
        textSpan.textContent = text || 'Message sent';
        content.appendChild(textSpan);
    }
    
    snackbar.classList.add('show');
    
    snackbarTimeoutId = setTimeout(() => {
        snackbar.classList.remove('show');
        snackbarTimeoutId = null;
    }, 3000);
}

window.initSheets = initSheets;
window.showSnackbar = showSnackbar;
window.showToast = showSnackbar;
