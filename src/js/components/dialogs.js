/* ==========================================================================
   DIALOGS COMPONENT
   ========================================================================== */

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

// Global Interaction Handler helper (can be called directly if needed)
window.initDialogs = initDialogs;
