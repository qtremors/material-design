/* ==========================================================================
   SLIDERS COMPONENT
   ========================================================================== */

function initSliders() {
    const updateSlider = (slider) => {
        const val = (slider.value - slider.min) / (slider.max - slider.min) * 100;
        const container = slider.closest('.slider-container');
        if (container) {
            container.style.setProperty('--slider-value', val + '%');
        }
    };

    document.querySelectorAll('.md-slider').forEach(slider => {
        slider.addEventListener('input', () => updateSlider(slider));
        updateSlider(slider); // Initial state
    });
}

window.initSliders = initSliders;
