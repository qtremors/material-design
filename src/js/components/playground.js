/**
 * PLAYGROUND CONTROLLER (v4.1)
 * Optimized for Material Design Project with Integrated Hex-based Color Naming.
 */

// --- COLOR NAMING ENGINE (ntc.js inspired) ---
const ColorNamer = {
    names: [
        ["000000", "Black"], ["000080", "Navy Blue"], ["0000C8", "Dark Blue"], ["0000FF", "Blue"], ["000741", "Stratos"], ["001B1C", "Swamp"], ["002387", "Resolution Blue"], ["002900", "Deep Fir"], ["002E20", "Burnham"], ["003366", "International Klein Blue"], ["0033AA", "Prussian Blue"], ["003532", "Midnight Teal"], ["0047AB", "Cobalt"], ["004816", "Crusoe"], ["004950", "Sherpa Blue"], ["0056A7", "Endeavour"], ["006666", "Mosque"], ["006A6C", "Deep Teal"], ["0076A3", "Lochmara"], ["007BA7", "Cerulean"], ["007EC7", "Azure Radiance"], ["007FFF", "Azure"], ["008000", "Green"], ["008080", "Teal"], ["0095B6", "Bondi Blue"], ["009DC4", "Pacific Blue"], ["00A693", "Persian Green"], ["00A86B", "Jade"], ["00CC99", "Caribbean Green"], ["00CCCC", "Robin's Egg Blue"], ["00FF00", "Green"], ["00FF7F", "Spring Green"], ["00FFFF", "Cyan / Aqua"], ["010D1A", "Blue Charcoal"], ["011635", "Midnight Blue"], ["011D13", "Holly"], ["012731", "Cyprus"], ["01361C", "Everglade"], ["013F6A", "Elephant"], ["014B43", "Jewel"], ["015E85", "Orient"], ["016162", "Blue Stone"], ["016D39", "Fun Green"], ["01796F", "Pine Green"], ["017987", "Blue Lagoon"], ["01826B", "Deep Sea"], ["01A368", "Green Haze"], ["020D11", "Night Rider"], ["022D15", "English Holly"], ["02402C", "Sherwood Green"], ["024E46", "Congress Blue"], ["026395", "Bahama Blue"], ["0286CA", "Observatory"], ["02A4D3", "Cerulean"], ["03163C", "Tangaroa"], ["032B52", "Green Vogue"], ["036A6E", "Mosque"], ["041020", "Midnight"], ["041322", "Auckland"], ["042E4E", "Green Vogue"], ["044022", "Gable Green"], ["044230", "Everglade"], ["051040", "Gulf Blue"], ["051657", "Soviet Green"], ["055989", "Sailor Blue"], ["056F57", "Evening Sea"], ["06113C", "Dark Blue"], ["062301", "Black Forest"], ["062A78", "Gulf Blue"], ["063131", "Cyprus"], ["063529", "Jewel"], ["06A189", "Tradewind"], ["073A50", "Big Stone"], ["07575B", "Cascal"], ["080110", "Black Rock"], ["081910", "Swamp"], ["082567", "Resolution Blue"], ["088370", "Deep Sea"], ["09010A", "Night Ride"], ["092256", "Green Vogue"], ["09230F", "Deep Fir"], ["09255D", "Green Vogue"], ["092F38", "Cyprus"], ["093A24", "Everglade"], ["095859", "Blue Stone"], ["097F4B", "Fun Green"], ["0A001C", "Blue Charcoal"], ["0A410D", "Castleton Green"], ["0A480D", "Deep Fir"], ["0A6906", "Fun Green"], ["0A6F75", "Blue Lagoon"], ["0B0B0B", "Cod Gray"], ["0B0F08", "Swamp"], ["0B1107", "Holly"], ["0B1304", "Deep Fir"], ["0B6207", "Fun Green"], ["0B7610", "Fun Green"], ["0B8E3B", "Fun Green"], ["0C0B1D", "Stratos"], ["0C0D0F", "Night Rider"], ["0C1911", "Swamp"], ["0C2330", "Cyprus"], ["0C3B40", "Cyprus"], ["0C753C", "Fun Green"], ["0D0332", "Stratos"], ["0D1117", "Midnight Blue"], ["0D1C19", "Swamp"], ["0D2E1C", "Deep Fir"], ["0D3E3A", "Cyprus"], ["0D47A1", "Blue"], ["0E0E18", "Stratos"], ["0E2A30", "Cyprus"], ["0F2D9E", "Resolution Blue"], ["0F332A", "Swamp"], ["0F4D43", "Sherpa Blue"], ["0F52BA", "Sapphire"], ["10121D", "Stratos"], ["101405", "Deep Fir"], ["101D22", "Cyprus"], ["102030", "Cyprus"], ["110D12", "Night Rider"], ["111933", "Gulf Blue"], ["11635C", "Blue Stone"], ["11776C", "Blue Lagoon"], ["117F86", "Blue Lagoon"], ["120A12", "Night Rider"], ["120A8F", "Ultramarine"], ["123447", "Big Stone"], ["126B40", "Fun Green"], ["130000", "Black"], ["130A06", "Night Rider"], ["130D0D", "Night Rider"], ["131158", "Stratos"], ["13264D", "Gulf Blue"], ["134F19", "Deep Fir"], ["140605", "Night Rider"], ["1450BC", "Horizon"], ["151922", "Swamp"], ["154332", "Deep Fir"], ["155187", "Orient"], ["15588D", "Orient"], ["1560BD", "Denim"], ["15736B", "Blue Stone"], ["161616", "Cod Gray"], ["161628", "Gulf Blue"], ["161910", "Deep Fir"], ["16245C", "Gulf Blue"], ["163222", "Deep Fir"], ["16322C", "Swamp"], ["163531", "Cyprus"], ["171F04", "Deep Fir"], ["174620", "Deep Fir"], ["177245", "Fun Green"], ["182D09", "Deep Fir"], ["184851", "Big Stone"], ["185130", "Deep Fir"], ["19330E", "Deep Fir"], ["193751", "Big Stone"], ["1959A8", "Denim"], ["1A1A15", "Deep Fir"], ["1A1A1A", "Cod Gray"], ["1A5B49", "Blue Lagoon"], ["1B0245", "Stratos"], ["1B1035", "Gulf Blue"], ["1B1123", "Stratos"], ["1B127B", "Stratos"], ["1B1404", "Deep Fir"], ["1B2431", "Cyprus"], ["1B2F11", "Deep Fir"], ["1B3162", "Gulf Blue"], ["1B659D", "Denim"], ["1C1208", "Deep Fir"], ["1C1E13", "Deep Fir"], ["1C39BB", "Persian Blue"], ["1C402E", "Deep Fir"], ["1C7C7D", "Blue Lagoon"], ["1D6142", "Deep Fir"], ["1E0F04", "Deep Fir"], ["1E1609", "Deep Fir"], ["1E1708", "Deep Fir"], ["1E1D23", "Blue Charcoal"], ["1E1E1E", "Cod Gray"], ["1E385B", "Gable Green"], ["1E433C", "Cyprus"], ["1E4C2D", "Deep Fir"], ["1E6D64", "Blue Stone"], ["1F110F", "Deep Fir"], ["1F1F1F", "Cod Gray"], ["1F262B", "Cyprus"], ["1F4764", "Big Stone"], ["1F9D05", "Fun Green"], ["202020", "Cod Gray"], ["202221", "Swamp"], ["204441", "Cyprus"], ["204D29", "Deep Fir"], ["211812", "Deep Fir"], ["212121", "Cod Gray"], ["213A33", "Cyprus"], ["22022A", "Stratos"], ["220824", "Stratos"], ["22191B", "Night Rider"], ["221F1E", "Night Rider"], ["222425", "Blue Charcoal"], ["224621", "Deep Fir"], ["232230", "Stratos"], ["232D33", "Cyprus"], ["232E26", "Swamp"], ["233418", "Deep Fir"], ["242124", "Night Rider"], ["242A1D", "Deep Fir"], ["242E16", "Deep Fir"], ["242F1C", "Deep Fir"], ["245668", "Big Stone"], ["251607", "Deep Fir"], ["251706", "Deep Fir"], ["251F4F", "Stratos"], ["25272C", "Blue Charcoal"], ["25311C", "Deep Fir"], ["2596be", "Bondi Blue"], ["261414", "Deep Fir"], ["262335", "Stratos"], ["26283B", "Gulf Blue"], ["271607", "Deep Fir"], ["273A81", "Resolution Blue"], ["27421F", "Deep Fir"], ["274A5D", "Big Stone"], ["274E13", "Deep Fir"], ["281E15", "Deep Fir"], ["282022", "Night Rider"], ["28222A", "Stratos"], ["282828", "Cod Gray"], ["282B42", "Gulf Blue"], ["283626", "Deep Fir"], ["286A81", "Big Stone"], ["292130", "Stratos"], ["292324", "Night Rider"], ["292929", "Cod Gray"], ["293332", "Cyprus"], ["293720", "Deep Fir"], ["294A2F", "Deep Fir"], ["2962FF", "Blue"], ["2A0359", "Stratos"], ["2A140E", "Deep Fir"], ["2A2030", "Stratos"], ["2A2622", "Night Rider"], ["2A2F23", "Deep Fir"], ["2B0202", "Black"], ["2B194F", "Stratos"], ["2B3228", "Deep Fir"], ["2C1510", "Deep Fir"], ["2C1613", "Deep Fir"], ["2C2133", "Stratos"], ["2C2329", "Night Rider"], ["2C2A33", "Stratos"], ["2C2D24", "Deep Fir"], ["2C2E25", "Deep Fir"], ["2C4A44", "Cyprus"], ["2D383A", "Cyprus"], ["2E1905", "Deep Fir"], ["2E2C3D", "Stratos"], ["2E3123", "Deep Fir"], ["2E3222", "Deep Fir"], ["2E3C2B", "Deep Fir"], ["2E3D30", "Swamp"], ["2E8B57", "Sea Green"], ["2F212E", "Stratos"], ["2F2F2F", "Cod Gray"], ["2F3CB3", "Denim"], ["300529", "Stratos"], ["301F4D", "Stratos"], ["301F4E", "Stratos"], ["302226", "Night Rider"], ["302425", "Night Rider"], ["302731", "Stratos"], ["304C2E", "Deep Fir"], ["31252D", "Stratos"], ["314459", "Big Stone"], ["32127A", "Stratos"], ["321EFD", "Azure Radiance"], ["32292E", "Stratos"], ["322A2D", "Stratos"], ["32302D", "Night Rider"], ["323232", "Cod Gray"], ["326760", "Blue Stone"], ["330066", "Stratos"], ["33042A", "Stratos"], ["331F33", "Stratos"], ["332128", "Night Rider"], ["33222B", "Stratos"], ["33243E", "Stratos"], ["332E37", "Blue Charcoal"], ["333230", "Night Rider"], ["333333", "Cod Gray"], ["341F2F", "Stratos"], ["343434", "Cod Gray"], ["343A40", "Dark Slate Gray"], ["352126", "Night Rider"], ["352232", "Stratos"], ["35252D", "Stratos"], ["35332E", "Night Rider"], ["353330", "Night Rider"], ["353535", "Cod Gray"], ["35373F", "Blue Charcoal"], ["36222D", "Stratos"], ["363534", "Night Rider"], ["36454F", "Charcoal"], ["371D36", "Stratos"], ["372922", "Night Rider"], ["373021", "Deep Fir"], ["373E41", "Big Stone"], ["38010B", "Black"], ["38040E", "Stratos"], ["381412", "Deep Fir"], ["382830", "Stratos"], ["38312B", "Night Rider"], ["38312C", "Night Rider"], ["383233", "Night Rider"], ["383533", "Night Rider"], ["384447", "Big Stone"], ["391210", "Deep Fir"], ["391F21", "Night Rider"], ["392025", "Night Rider"], ["392330", "Stratos"], ["392D2B", "Night Rider"], ["392E2E", "Night Rider"], ["393A36", "Night Rider"], ["393D4E", "Gulf Blue"], ["394848", "Big Stone"], ["3A000A", "Black"], ["3A2021", "Night Rider"], ["3A302E", "Night Rider"], ["3A3532", "Night Rider"], ["3A3936", "Night Rider"], ["3A685A", "Blue Stone"], ["3B0910", "Deep Fir"], ["3B1F1F", "Night Rider"], ["3B2025", "Night Rider"], ["3B2E25", "Deep Fir"], ["3B2F2F", "Night Rider"], ["3B3121", "Deep Fir"], ["3B322C", "Night Rider"], ["3B3433", "Night Rider"], ["3B3532", "Night Rider"], ["3C0820", "Stratos"], ["3C2126", "Night Rider"], ["3C243B", "Stratos"], ["3C3D3E", "Blue Charcoal"], ["3D1814", "Deep Fir"], ["3D2626", "Night Rider"], ["3D2A2B", "Night Rider"], ["3D2E2B", "Night Rider"], ["3D3025", "Deep Fir"], ["3D302E", "Night Rider"], ["3D3131", "Night Rider"], ["3D3533", "Night Rider"], ["3E3D3E", "Cod Gray"], ["3E7873", "Blue Stone"], ["3F2109", "Deep Fir"], ["3F2500", "Deep Fir"], ["3F2A2A", "Night Rider"], ["3F3002", "Deep Fir"], ["3F3024", "Deep Fir"], ["3F312F", "Night Rider"], ["3F4041", "Blue Charcoal"], ["401A03", "Deep Fir"], ["402700", "Deep Fir"], ["403030", "Night Rider"], ["403D38", "Night Rider"], ["404040", "Cod Gray"], ["411F10", "Deep Fir"], ["412010", "Deep Fir"], ["413031", "Night Rider"], ["431308", "Deep Fir"], ["432D28", "Night Rider"], ["43302E", "Night Rider"], ["441111", "Deep Fir"], ["441414", "Deep Fir"], ["441B1B", "Deep Fir"], ["443333", "Night Rider"], ["444444", "Cod Gray"], ["450100", "Black"], ["452121", "Night Rider"], ["456754", "Blue Stone"], ["456E54", "Blue Stone"], ["462425", "Night Rider"], ["463030", "Night Rider"], ["474747", "Cod Gray"], ["480404", "Deep Fir"], ["482D28", "Night Rider"], ["483030", "Night Rider"], ["485252", "Big Stone"], ["491915", "Deep Fir"], ["492020", "Night Rider"], ["493030", "Night Rider"], ["493548", "Stratos"], ["4A0101", "Black"], ["4A0E0E", "Deep Fir"], ["4A1100", "Deep Fir"], ["4A2021", "Night Rider"], ["4A2E32", "Night Rider"], ["4A3030", "Night Rider"], ["4A3636", "Night Rider"], ["4A3B37", "Night Rider"], ["4A4244", "Night Rider"], ["4A444B", "Night Rider"], ["4A465A", "Gulf Blue"], ["4A4E5A", "Gulf Blue"], ["4A6363", "Slate Gray"], ["4B0000", "Black"], ["4B0202", "Deep Fir"], ["4B1111", "Deep Fir"], ["4B2020", "Night Rider"], ["4B2E32", "Night Rider"], ["4B3030", "Night Rider"], ["4B3636", "Night Rider"], ["4B3B37", "Night Rider"], ["4B4244", "Night Rider"], ["4B444B", "Night Rider"], ["4B465A", "Gulf Blue"], ["4B4E5A", "Gulf Blue"], ["5D3FD3", "Iris Blue"], ["6750A4", "Purple"], ["F4F7FC", "Ghost White"], ["FFFFFF", "White"], ["FF0000", "Red"], ["00FF00", "Lime Green"], ["0000FF", "Pure Blue"], ["FFFF00", "Yellow"], ["FF00FF", "Magenta"], ["00FFFF", "Cyan"], ["2962FF", "Sky Blue"], ["6750A4", "Royal Purple"], ["006C51", "Forest Green"], ["006A6A", "Deep Teal"], ["0097A7", "Manganese Blue"], ["6D5E00", "Mustard Green"], ["8B5000", "Raw Umber"], ["BC004B", "Deep Pink"], ["B3261E", "Deep Red"]
    ],

    init: function() {
        let color, rgb, hsl;
        for (let i = 0; i < this.names.length; i++) {
            color = "#" + this.names[i][0];
            rgb = this.rgb(color);
            hsl = this.hsl(color);
            this.names[i].push(rgb[0], rgb[1], rgb[2], hsl[0], hsl[1], hsl[2]);
        }
    },

    rgb: function(color) {
        return [parseInt(color.substring(1, 3), 16), parseInt(color.substring(3, 5), 16), parseInt(color.substring(5, 7), 16)];
    },

    hsl: function(color) {
        let rgb = [parseInt(color.substring(1, 3), 16) / 255, parseInt(color.substring(3, 5), 16) / 255, parseInt(color.substring(5, 7), 16) / 255];
        let min = Math.min(rgb[0], Math.min(rgb[1], rgb[2]));
        let max = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));
        let delta = max - min;
        let l = (min + max) / 2;
        let s = 0, h = 0;

        if (l > 0 && l < 1) s = delta / (l < 0.5 ? (2 * l) : (2 - 2 * l));
        if (delta > 0) {
            if (max == rgb[0] && max != rgb[1]) h += (rgb[1] - rgb[2]) / delta;
            if (max == rgb[1] && max != rgb[2]) h += (2 + (rgb[2] - rgb[0]) / delta);
            if (max == rgb[2] && max != rgb[0]) h += (4 + (rgb[0] - rgb[1]) / delta);
            h /= 6;
        }
        return [parseInt(h * 255), parseInt(s * 255), parseInt(l * 255)];
    },

    name: function(color) {
        color = color.toUpperCase();
        if (color.length < 3 || color.length > 7) return ["#000000", "Invalid Color", false];
        if (color.length % 3 == 0) color = "#" + color;
        if (color.length == 4) color = "#" + color.substr(1, 1) + color.substr(1, 1) + color.substr(2, 1) + color.substr(2, 1) + color.substr(3, 1) + color.substr(3, 1);

        let rgb = this.rgb(color);
        let hsl = this.hsl(color);
        let ndf1 = 0, ndf2 = 0, ndf = 0;
        let cl = -1, df = -1;

        for (let i = 0; i < this.names.length; i++) {
            if (color == "#" + this.names[i][0]) return ["#" + this.names[i][0], this.names[i][1], true];

            ndf1 = Math.pow(rgb[0] - this.names[i][2], 2) + Math.pow(rgb[1] - this.names[i][3], 2) + Math.pow(rgb[2] - this.names[i][4], 2);
            ndf2 = Math.pow(hsl[0] - this.names[i][5], 2) + Math.pow(hsl[1] - this.names[i][6], 2) + Math.pow(hsl[2] - this.names[i][7], 2);
            ndf = ndf1 + ndf2 * 2;

            if (df < 0 || df > ndf) {
                df = ndf;
                cl = i;
            }
        }

        return (cl < 0 ? ["#000000", "Invalid Color", false] : ["#" + this.names[cl][0], this.names[cl][1], false]);
    }
};

ColorNamer.init();

// --- PLAYGROUND CORE ---

const SEEDS = [
    { name: 'blue', hex: '#2962FF' },
    { name: 'purple', hex: '#6750A4' },
    { name: 'green', hex: '#006C51' },
    { name: 'teal', hex: '#006A6A' },
    { name: 'red', hex: '#B3261E' },
    { name: 'pink', hex: '#BC004B' },
    { name: 'orange', hex: '#8B5000' },
    { name: 'yellow', hex: '#6D5E00' },
    { name: 'cyan', hex: '#0097A7' }
];

const ROLE_NAMES = {
    'primary': 'Primary', 'on-primary': 'On Primary', 'primary-container': 'Primary Container', 'on-primary-container': 'On Primary Container',
    'secondary': 'Secondary', 'on-secondary': 'On Secondary', 'secondary-container': 'Secondary Container', 'on-secondary-container': 'On Secondary Container',
    'tertiary': 'Tertiary', 'on-tertiary': 'On Tertiary', 'tertiary-container': 'Tertiary Container', 'on-tertiary-container': 'On Tertiary Container',
    'error': 'Error', 'on-error': 'On Error', 'error-container': 'Error Container', 'on-error-container': 'On Error Container',
    'surface': 'Surface', 'on-surface': 'On Surface', 'surface-variant': 'Surface Variant', 'on-surface-variant': 'On Surface Variant',
    'outline': 'Outline', 'outline-variant': 'Outline Variant', 'inverse-surface': 'Inverse Surface', 'inverse-on-surface': 'Inverse On Surface',
    'inverse-primary': 'Inverse Primary', 'surface-container': 'Surface Container', 'surface-container-low': 'Surface Container Low',
    'surface-container-high': 'Surface Container High', 'surface-container-highest': 'Surface Container Highest',
    'success': 'Success', 'on-success': 'On Success', 'success-container': 'Success Container', 'on-success-container': 'On Success Container',
    'warning': 'Warning', 'on-warning': 'On Warning', 'warning-container': 'Warning Container', 'on-warning-container': 'On Warning Container'
};

let allTokensBySeed = {}; // { 'blue': [ {name, value}, ... ] }
let activeTokens = []; // Array of tokens parsed from :root

async function initPlayground() {
    // Override global UI sync to include playground swatches
    window.updateSettingsUI = syncSelectionState;
    
    syncSelectionState();
    await loadCSSVars();
    
    // Initial Render
    renderMatrixContents();
    renderSidebarPalette();

    if (typeof initRipples === 'function') initRipples();
}

function syncSelectionState() {
    const root = document.documentElement;
    const state = {
        theme: root.getAttribute('data-theme') || 'light',
        seed: root.getAttribute('data-seed') || 'blue',
        radius: root.getAttribute('data-radius') || 'medium'
    };

    // Update Sidebar state (labels/active tabs)
    // Optimization: Filter for relevant actions only
    const actionsToSync = ['set-theme-mode', 'set-theme-seed', 'set-radius'];
    
    document.querySelectorAll('[data-action]').forEach(btn => {
        const action = btn.dataset.action;
        if (!actionsToSync.includes(action)) return;

        const value = btn.dataset.value;
        if (action === 'set-theme-mode' && state.theme === value) btn.classList.add('active');
        else if (action === 'set-theme-seed' && state.seed === value) btn.classList.add('active');
        else if (action === 'set-radius' && state.radius === value) btn.classList.add('active');
        else btn.classList.remove('active');
    });

    // Update matrix if visible
    const matrixPanel = document.getElementById('palette-panel');
    if (matrixPanel && matrixPanel.classList.contains('active')) {
        const tokenInput = document.getElementById('token-search');
        renderMatrixContents(tokenInput ? tokenInput.value : '');
    }

    renderSidebarPalette();
}

function renderSidebarPalette() {
    const container = document.getElementById('sidebar-active-palette');
    if (!container || activeTokens.length === 0) return;

    container.innerHTML = '';
    
    // Sort visually before rendering
    const sortedTokens = visualSort([...activeTokens]);
    
    sortedTokens.forEach(token => {
        // Only show color roles, ignore typography/elevation vars
        if (!token.name.includes('-color-') && !token.name.includes('surface') && !token.name.includes('outline')) return;
        if (token.name.includes('-rgb')) return;

        const value = getComputedStyle(document.documentElement).getPropertyValue(token.name).trim();
        const hex = toHex(value);
        
        const baseRole = token.name.replace('--md-sys-color-', '').replace('--md-sys-', '');
        const roleName = ROLE_NAMES[baseRole] || baseRole;

        const circle = document.createElement('div');
        circle.className = 'sidebar-color-circle ripple-target';
        circle.style.background = hex;
        circle.title = `${roleName}: ${hex}`;
        circle.onclick = () => copyVal(hex, `${roleName} hex copied!`);
        
        container.appendChild(circle);
    });
}
async function loadCSSVars() {
    try {
        const response = await fetch('css/variables.css');
        const css = await response.text();
        
        // Parse :root
        const rootMatch = css.match(/:root\s*{([^}]+)}/);
        if (rootMatch) {
            activeTokens = parseTokenBlock(rootMatch[1]);
            allTokensBySeed['base'] = activeTokens;
            
            // Extract 'blue' (default) tokens from base
            // Blue doesn't have its own block in CSS, it lives in :root
            allTokensBySeed['blue'] = activeTokens.filter(t => {
                const name = t.name;
                // Only include tokens that strictly define the color personality
                // (Matches what other seeds typically override)
                const isColorRole = name.includes('primary') || 
                                  name.includes('secondary') || 
                                  name.includes('tertiary') ||
                                  name.includes('surface-variant');
                
                // Exclude global semantics (Error/Success/Warning are shared)
                // Exclude inverse-primary (usually not overridden in light mode seeds)
                const isSemantic = name.includes('error') || 
                                 name.includes('success') || 
                                 name.includes('warning') ||
                                 name.includes('inverse');
                                 
                return isColorRole && !isSemantic;
            });
        }

        // Parse individual seeds
        SEEDS.forEach(seed => {
            const seedRegex = new RegExp(`\\[data-seed="${seed.name}"\\]\\s*{([^}]+)}`, 'g');
            // Re-fetch because sharing 'g' flag across multiple seeds is messy
            const matches = [...css.matchAll(seedRegex)];
            let seedTokens = [];
            matches.forEach(m => {
                seedTokens = [...seedTokens, ...parseTokenBlock(m[1])];
            });
            if (seedTokens.length > 0) {
                allTokensBySeed[seed.name] = seedTokens;
            }
        });
    } catch (e) {
        console.error("Failed to load variables.css", e);
    }
}

function parseTokenBlock(blockText) {
    const tokens = [];
    const varRegex = /(--md-sys-[a-zA-Z0-9-]+):\s*([^;]+);/g;
    let match;
    while ((match = varRegex.exec(blockText)) !== null) {
        const name = match[1].trim();
        const value = match[2].trim();
        // Only focus on colors
        if (name.includes('color') || name.includes('surface') || name.includes('outline')) {
            if (!name.includes('-rgb')) {
                tokens.push({ name, value });
            }
        }
    }
    return tokens;
}

function toHex(color) {
    if (color.startsWith('#')) return color;
    if (color.startsWith('rgb')) {
        const parts = color.match(/\d+/g);
        return "#" + parts.slice(0, 3).map(x => parseInt(x).toString(16).padStart(2, '0')).join('').toUpperCase();
    }
    return color;
}


function renderMatrixContents(filter = '') {
    const query = filter.toLowerCase().trim();
    const activeGrid = document.getElementById('active-matrix');
    const refGrid = document.getElementById('reference-matrix');
    
    if (!activeGrid || !refGrid) return;
    activeGrid.innerHTML = '';
    refGrid.innerHTML = '';

    const getFullMetadata = (token, origin = '') => {
        let value = token.value;
        let computed = value;

        // If it's an active role or a variable reference, get the live computed value
        if (origin === 'active' || value.startsWith('var(')) {
            computed = getComputedStyle(document.documentElement).getPropertyValue(token.name).trim();
            // Fallback if empty (e.g. invalid var)
            if (!computed) computed = value;
        }
        
        const hex = toHex(computed);
        const nameData = ColorNamer.name(hex);
        const colorName = nameData[1];
        
        const baseRole = token.name.replace('--md-sys-color-', '').replace('--md-sys-', '');
        const roleName = ROLE_NAMES[baseRole] || baseRole.charAt(0).toUpperCase() + baseRole.slice(1).replace(/-/g, ' ');

        return {
            tokenName: token.name,
            roleName: roleName,
            colorName: colorName,
            value: value,
            hex: hex,
            origin: origin
        };
    };

    const matchesQuery = (meta) => {
        if (!query) return true;
        return meta.tokenName.toLowerCase().includes(query) || 
               meta.roleName.toLowerCase().includes(query) || 
               meta.colorName.toLowerCase().includes(query) || 
               meta.hex.toLowerCase().includes(query) ||
               meta.origin.toLowerCase().includes(query);
    };

    // 1. ACTIVE SYSTEM ROLES
    // Filter down to unique tokens (sometimes parsed multiple times)
    let uniqueActive = [];
    const seenActive = new Set();
    activeTokens.forEach(t => {
        if (!seenActive.has(t.name)) {
            uniqueActive.push(t);
            seenActive.add(t.name);
        }
    });

    // Sort Visually
    uniqueActive = visualSort(uniqueActive);

    uniqueActive.forEach(t => {
        const meta = getFullMetadata(t, 'active');
        if (matchesQuery(meta)) {
            activeGrid.appendChild(createTokenCard(meta));
        }
    });
    
    document.getElementById('active-count').innerText = `${seenActive.size}`;

    // 2. INACTIVE PALETTES (Previously Global)
    document.querySelector('.matrix-section-reference .matrix-header').innerText = "Inactive Color Palettes";
    document.querySelector('.matrix-section-reference .matrix-subheader').innerText = "Comparison of other available color seeds not currently active.";

    // Get current seed directly from DOM to filter
    const currentSeed = document.documentElement.getAttribute('data-seed') || 'blue';

    // Seeded Tokens (Skip current)
    SEEDS.forEach(seed => {
        if (seed.name === currentSeed) return;

        const tokens = allTokensBySeed[seed.name] || [];
        tokens.forEach(t => {
            const meta = getFullMetadata(t, seed.name);
            if (matchesQuery(meta)) {
                refGrid.appendChild(createTokenCard(meta));
            }
        });
    });
}

function createTokenCard(meta) {
    const card = document.createElement('div');
    card.className = 'token-card';
    
    let originBadge = '';
    if (meta.origin && meta.origin !== 'base' && meta.origin !== 'active') {
        originBadge = `<span class="token-badge">${meta.origin}</span>`;
    }

    card.innerHTML = `
        <div class="token-swatch ripple-target" style="background: ${meta.hex}" onclick="copyVal('${meta.hex}', 'Hex copied!')">
            ${originBadge}
        </div>
        <div class="token-info">
            <div class="token-color-name">${meta.colorName}</div>
            <div class="token-hex-code">${meta.hex}</div>
            <div class="token-var-name" onclick="copyVal('${meta.tokenName}', 'Token copied!')">${meta.tokenName}</div>
        </div>
    `;
    return card;
}

/**
 * Sort tokens visually: Neutrals -> Hue -> Lightness
 */
function visualSort(tokens) {
    return tokens.sort((a, b) => {
        // Get Computed Values
        const valA = getComputedStyle(document.documentElement).getPropertyValue(a.name).trim();
        const valB = getComputedStyle(document.documentElement).getPropertyValue(b.name).trim();
        
        const hexA = toHex(valA);
        const hexB = toHex(valB);
        
        const hslA = ColorNamer.hsl(hexA); // [h, s, l] (0-255)
        const hslB = ColorNamer.hsl(hexB);
        
        const isNeutralA = hslA[1] < 25; // Saturation < ~10%
        const isNeutralB = hslB[1] < 25;
        
        // 1. Neutrals First
        if (isNeutralA && !isNeutralB) return -1;
        if (!isNeutralA && isNeutralB) return 1;
        
        // 2. Sort by Hue (if both chromatic)
        if (!isNeutralA && !isNeutralB) {
            if (Math.abs(hslA[0] - hslB[0]) > 10) { // Tolerance for similar hues
                return hslA[0] - hslB[0];
            }
        }
        
        // 3. Sort by Lightness (Dark to Light)
        return hslA[2] - hslB[2];
    });
}

// --- GLOBAL INTERACTIONS ---

document.addEventListener('click', (e) => {
    // Actions (Theme/Seed/Radius)
    const actionBtn = e.target.closest('[data-action]');
    if (actionBtn && !actionBtn.classList.contains('md-tab')) {
        const action = actionBtn.dataset.action;
        let key = action.replace('set-theme-', '').replace('set-', '');
        if (key === 'mode') key = 'theme'; // Map 'mode' button to 'theme' engine state
        
        const value = actionBtn.dataset.value;
        if (window.setThemeConfig) {
            setThemeConfig(key, value);
        }
        return;
    }

    // Tabs logic in interactions.js handles visual switching, 
    // we just need to re-render matrix if it becomes active.
    const tabBtn = e.target.closest('.md-tab');
    if (tabBtn) {
        setTimeout(() => {
            if (tabBtn.dataset.target === 'palette-panel') {
                renderMatrixContents(document.getElementById('token-search').value);
            }
        }, 50);
    }
});

const searchInput = document.getElementById('token-search');
if (searchInput) {
    searchInput.addEventListener('input', (e) => {
        renderMatrixContents(e.target.value);
    });
}

function copyVal(text, msg) {
    navigator.clipboard.writeText(text).then(() => {
        const toast = document.getElementById('pg-toast');
        if (toast) {
            toast.innerText = msg;
            toast.classList.add('show');
            setTimeout(() => toast.classList.remove('show'), 2000);
        }
    });
}

// Start
initPlayground();
