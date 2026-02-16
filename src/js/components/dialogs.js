/* ==========================================================================
   DIALOGS COMPONENT
   ========================================================================== */

function initDialogs() {
    /**
     * Opens a specific dialog by ID.
     * @param {string} id - The ID of the dialog to open.
     */
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

    /**
     * Closes the dialog.
     * @param {Event} [event] - The event that triggered the close.
     * @param {string} [id] - Specific dialog ID to close.
     */
    window.closeDialog = (event, id) => {
        if(event) event.stopPropagation();
        const backdrop = id ? document.getElementById(id) : document.querySelector('.dialog-backdrop.open');
        if(backdrop) backdrop.classList.remove('open');
    }
}

// Expose for global usage
window.initDialogs = initDialogs;
