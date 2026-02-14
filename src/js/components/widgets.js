/**
 * Widgets Interactivity
 * Handles music player states and other interactive widgets.
 */

function initWidgets() {
    // 1. Music Player Controls
    document.querySelectorAll('.card-music-widget, .card-music-circular, .card-music-compact').forEach(player => {
        // Universal button click animation
        player.querySelectorAll('button').forEach(btn => {
            btn.addEventListener('click', () => {
                btn.style.transform = 'scale(0.9)';
                setTimeout(() => btn.style.transform = '', 100);
            });
        });

        const playBtn = player.querySelector('.play-btn, .play-pause') || 
                        Array.from(player.querySelectorAll('button')).find(btn => 
                            btn.querySelector('.material-symbols-rounded')?.textContent.includes('play') ||
                            btn.querySelector('.material-symbols-rounded')?.textContent.includes('pause')
                        );

        if (playBtn) {
            playBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                const icon = playBtn.querySelector('.material-symbols-rounded');
                if (icon) {
                    const isPlaying = icon.textContent === 'pause';
                    icon.textContent = isPlaying ? 'play_arrow' : 'pause';
                }
            });
        }

        player.addEventListener('click', (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const icon = btn.querySelector('.material-symbols-rounded');
            if (!icon) return;

            if (icon.textContent === 'shuffle') {
                btn.classList.toggle('active');
                btn.style.color = btn.classList.contains('active') ? 'var(--md-sys-color-primary)' : '';
            }

            if (icon.textContent === 'repeat' || icon.textContent === 'repeat_one') {
                if (!btn.dataset.state || btn.dataset.state === 'none') {
                    btn.dataset.state = 'all';
                    btn.classList.add('active');
                    btn.style.color = 'var(--md-sys-color-primary)';
                    icon.textContent = 'repeat';
                } else if (btn.dataset.state === 'all') {
                    btn.dataset.state = 'one';
                    icon.textContent = 'repeat_one';
                } else {
                    btn.dataset.state = 'none';
                    btn.classList.remove('active');
                    btn.style.color = '';
                    icon.textContent = 'repeat';
                }
            }
        });
    });

    // 2. Digital Clock
    const clockTime = document.getElementById('clockTime');
    const clockDate = document.getElementById('clockDate');
    if (clockTime && clockDate) {
        const updateClock = () => {
            const now = new Date();
            clockTime.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            clockDate.textContent = now.toLocaleDateString([], { weekday: 'long', month: 'short', day: 'numeric' });
        };
        updateClock();
        setInterval(updateClock, 1000);
    }

    // 3. Stopwatch
    const swTime = document.getElementById('stopwatchTime');
    const swStart = document.getElementById('stopwatchStart');
    const swReset = document.getElementById('stopwatchReset');
    if (swTime && swStart && swReset) {
        let swInterval;
        let swStartTimestamp;
        let swElapsed = 0;
        let swIsRunning = false;

        const formatTime = (ms) => {
            const m = Math.floor(ms / 60000);
            const s = Math.floor((ms % 60000) / 1000);
            const d = Math.floor((ms % 1000) / 100);
            return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}.${d}`;
        };

        swStart.addEventListener('click', () => {
            const icon = swStart.querySelector('.material-symbols-rounded');
            if (swIsRunning) {
                clearInterval(swInterval);
                swElapsed += Date.now() - swStartTimestamp;
                icon.textContent = 'play_arrow';
            } else {
                swStartTimestamp = Date.now();
                swInterval = setInterval(() => {
                    swTime.textContent = formatTime(swElapsed + (Date.now() - swStartTimestamp));
                }, 100);
                icon.textContent = 'pause';
            }
            swIsRunning = !swIsRunning;
        });

        swReset.addEventListener('click', () => {
            clearInterval(swInterval);
            swElapsed = 0;
            swIsRunning = false;
            swTime.textContent = '00:00.0';
            swStart.querySelector('.material-symbols-rounded').textContent = 'play_arrow';
        });
    }

    // 4. Daily Mix List
    document.querySelectorAll('.daily-mix-list .list-item').forEach(item => {
        item.addEventListener('click', () => {
            const icon = item.querySelector('.material-symbols-rounded');
            const isPlaying = icon.textContent === 'pause';
            item.closest('.daily-mix-list').querySelectorAll('.material-symbols-rounded').forEach(i => i.textContent = 'play_arrow');
            icon.textContent = isPlaying ? 'play_arrow' : 'pause';
            item.style.backgroundColor = 'rgba(255,255,255,0.2)';
            setTimeout(() => item.style.backgroundColor = '', 200);
        });
    });
}

window.initWidgets = initWidgets;
