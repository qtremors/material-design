/* ==========================================================================
   SLIDERS COMPONENT
   ========================================================================== */

function initSliders() {
    const updateSlider = (slider) => {
        const min = parseFloat(slider.min) || 0;
        const max = parseFloat(slider.max) || 100;
        const denominator = max - min;
        const val = denominator === 0 ? 0 : (slider.value - min) / denominator * 100;
        
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
