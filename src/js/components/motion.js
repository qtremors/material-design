/* ==========================================================================
   EXPRESSIVE MOTION & ANIMATIONS
   ========================================================================== */

function initExpressiveAnimations() {
    // Squiggly Progress Loop
    const wavyBars = document.querySelectorAll('.progress-wavy .bar');
    if (wavyBars.length > 0) {
        setInterval(() => {
            wavyBars.forEach(bar => {
                const width = Math.random() * 60 + 20;
                bar.style.width = `${width}%`;
            });
        }, 2000);
    }

    // Segmented Progress Loop
    const segments = document.querySelectorAll('.progress-segmented .segment');
    let segmentIndex = 1;
    
    if(segments.length > 0) {
        setInterval(() => {
            segments.forEach(s => s.classList.remove('filled'));
            for(let i=0; i<segmentIndex; i++) {
                if(segments[i]) segments[i].classList.add('filled');
            }
            segmentIndex++;
            if(segmentIndex > segments.length) segmentIndex = 0;
        }, 1000);
    }
}

function initScrollAnimations() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('card-entrance');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.md-card, .expressive-card, .hero-card').forEach(el => {
        el.classList.remove('card-entrance');
        observer.observe(el);
    });
}

window.initExpressiveAnimations = initExpressiveAnimations;
window.initScrollAnimations = initScrollAnimations;
