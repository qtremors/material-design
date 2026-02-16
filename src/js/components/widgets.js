/**
 * Widgets Interactivity
 * Handles music player states and other interactive widgets.
 */

function initWidgets() {
    // 1. Music Player Controls
    document.querySelectorAll('.card-music-widget, .card-music-circular, .card-music-compact').forEach(player => {
        // Universal button click animation
        player.querySelectorAll('button').forEach(btn => {
            // Skip buttons that rely on CSS transforms for positioning
            if (btn.classList.contains('float-btn')) return;
            
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

    // 5. Grid Visibility Toggle & Labels Toggle
    const gridToggle = document.getElementById('gridToggle');
    const labelsToggle = document.getElementById('labelsToggle');
    const contentContainer = document.querySelector('.widgets-content-container');
    const widgetsGrid = document.querySelector('.widgets-grid');
    
    // Strict Grid Resizing Logic
    const snapToGrid = () => {
        if (!contentContainer) return;
        
        // Calculate available width (accounting for main margins if any)
        const main = document.querySelector('main');
        const mainStyle = window.getComputedStyle(main);
        const marginLeft = parseFloat(mainStyle.marginLeft) || 0;
        
        // Determine number of 100px columns that fit
        const parentContainer = contentContainer.parentElement; // .container
        if (!parentContainer) return;

        const parentWidth = parentContainer.getBoundingClientRect().width;
        

        
        // Calculate max columns that fit in the parent
        // Subtract 2px for borders
        const availableCols = Math.floor((parentWidth - 2) / 100);
        
        // Enforce minimum of 5 columns as per user request
        const cols = Math.max(5, availableCols);
        
        // Calculate dynamic cell size to fit width perfectly without scroll
        const cellSize = (parentWidth - 2) / cols;

        contentContainer.style.setProperty('--grid-cols', cols);
        contentContainer.style.setProperty('--grid-cell-size', `${cellSize}px`);
        
        contentContainer.style.width = '100%';
    };

    if (contentContainer) {
        // Run on load and resize
        snapToGrid();
        window.addEventListener('resize', snapToGrid);
        
        if (gridToggle) {
            // Load saved state
            const isGridActive = localStorage.getItem('mdWidgetsGridActive') === 'true';
            gridToggle.checked = isGridActive;
            contentContainer.classList.toggle('grid-active', isGridActive);

            gridToggle.addEventListener('change', () => {
                const isActive = gridToggle.checked;
                contentContainer.classList.toggle('grid-active', isActive);
                localStorage.setItem('mdWidgetsGridActive', isActive);
            });
        }
        
        if (labelsToggle && widgetsGrid) {
            const savedState = localStorage.getItem('mdWidgetsLabelsVisible');
            const isLabelsVisible = savedState === 'true'; 
            
            labelsToggle.checked = isLabelsVisible;
            widgetsGrid.classList.toggle('labels-hidden', !isLabelsVisible);

            labelsToggle.addEventListener('change', () => {
                const isVisible = labelsToggle.checked;
                widgetsGrid.classList.toggle('labels-hidden', !isVisible);
                localStorage.setItem('mdWidgetsLabelsVisible', isVisible);
                
                // Trigger repack
                packWidgets();
            });
        }
    }

    // 6. Bin-Packing Layout Engine
    const packWidgets = () => {
        const container = document.querySelector('.widgets-grid');
        if (!container) return;

        // Check if labels are hidden
        const areLabelsHidden = container.classList.contains('labels-hidden');

        // 1. Calculate Available Columns
        const styles = window.getComputedStyle(container);
        const paddingLeft = parseFloat(styles.paddingLeft) || 0;
        const paddingRight = parseFloat(styles.paddingRight) || 0;
        const availableWidth = container.clientWidth - paddingLeft - paddingRight;
        
        // Use the same logic as snapToGrid
        const availableCols = Math.floor(availableWidth / 100);
        const cols = Math.max(5, availableCols);
        
        if (cols <= 0) return;

        // 2. Initialize Occupied Map
        const occupied = new Set();
        const isOccupied = (r, c, w, h) => {
            for (let i = 0; i < h; i++) {
                for (let j = 0; j < w; j++) {
                    const checkRow = r + i;
                    const checkCol = c + j;
                    if (checkCol > cols) return true; // Out of bounds
                    if (occupied.has(`${checkRow},${checkCol}`)) return true;
                }
            }
            return false;
        };
        const markOccupied = (r, c, w, h) => {
            for (let i = 0; i < h; i++) {
                for (let j = 0; j < w; j++) {
                    occupied.add(`${r + i},${c + j}`);
                }
            }
        };

        // 3. Process Widgets
        document.querySelectorAll('.widget-wrapper').forEach(widget => {
             // Parse Size from Class
             const match = Array.from(widget.classList).find(cls => cls.match(/^w-(\d+)x(\d+)$/));
             if (!match) return;
             
             const [_, wStr, hStr] = match.match(/^w-(\d+)x(\d+)$/);
             const w = parseInt(wStr);
             let h = parseInt(hStr); 
             
             // +1 for Label Row ONLY if labels are visible
             if (!areLabelsHidden) {
                h += 1;
             }

             // Check for Forced Position (inline style)
             const getStyle = (prop) => {
                 if (!widget.dataset[`original${prop}`]) {
                     widget.dataset[`original${prop}`] = widget.style[prop] || 'none';
                 }
                 return widget.dataset[`original${prop}`] !== 'none' ? parseInt(widget.dataset[`original${prop}`]) : null;
             };

             const forcedCol = getStyle('gridColumnStart');
             const forcedRow = getStyle('gridRowStart');

             // Find Position
             let placed = false;
             let row = 1;
             while (!placed && row < 1000) {
                 // If forced row, only check that row
                 if (forcedRow && row !== forcedRow) {
                     row++;
                     continue;
                 }

                 for (let col = 1; col <= cols - w + 1; col++) {
                     // If forced col, skip others
                     if (forcedCol && col !== forcedCol) continue;

                     if (!isOccupied(row, col, w, h)) {
                         // Place it
                         widget.style.gridColumnStart = col;
                         widget.style.gridRowStart = row;
                         widget.style.gridColumnEnd = `span ${w}`;
                         widget.style.gridRowEnd = `span ${h}`;
                         
                         markOccupied(row, col, w, h);
                         placed = true;
                         break;
                     }
                 }
                 if (!placed) row++;
             }
        });
    };

    // Run on init and resize
    // Wait for DOM to handle visibility classes before first pack
    requestAnimationFrame(() => {
        packWidgets();
    });
    
    let resizeTimer;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(packWidgets, 100);
    });
}

window.initWidgets = initWidgets;
