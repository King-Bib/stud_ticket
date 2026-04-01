document.addEventListener('DOMContentLoaded', () => {
    // DOM Elements
    const screens = {
        home: document.getElementById('home-screen'),
        login: document.getElementById('login-screen'),
        profile: document.getElementById('profile-screen')
    };

    const modal = document.getElementById('settings-modal');
    const settingsBtn = document.getElementById('settings-btn');
    const closeSettingsBtn = document.getElementById('close-settings-btn');
    const themeToggle = document.getElementById('theme-toggle');
    const themeIcon = document.getElementById('theme-icon');
    const qrcodeContainer = document.getElementById('qrcode');
    const testConnectionBtn = document.getElementById('test-connection-btn');

    // Navigation
    const navButtons = {
        toLogin: document.getElementById('login-nav-btn'),
        doLogin: document.getElementById('do-login-btn'),
        backToHome: document.getElementById('back-to-home'),
        backToLogin: document.getElementById('back-to-login')
    };

    // Update Time
    function updateTime() {
        const now = new Date();
        const timeStr = now.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' });
        document.querySelectorAll('#current-time, .standalone-time').forEach(el => {
            el.textContent = timeStr;
        });

        const dateStr = now.toLocaleDateString('ru-RU', { month: 'long', day: 'numeric' }).toUpperCase();
        document.getElementById('current-date').textContent = dateStr;
    }

    setInterval(updateTime, 60000);
    updateTime();

    // Theme Management
    function toggleTheme() {
        const isDark = document.body.classList.toggle('dark-theme');
        document.body.classList.toggle('light-theme', !isDark);
        localStorage.setItem('theme', isDark ? 'dark' : 'light');
        
        // Update Icon
        if (isDark) {
            themeIcon.innerHTML = '<path d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707m0-11.314l.707.707m11.314 11.314l.707.707M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8z"/>';
        } else {
            themeIcon.innerHTML = '<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>';
        }
    }

    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.replace('light-theme', 'dark-theme');
        toggleTheme(); // Set icon
        toggleTheme(); // Revert back but with icon updated? No, better logic:
    }

    // Modal Logic
    settingsBtn.addEventListener('click', () => modal.classList.add('active'));
    closeSettingsBtn.addEventListener('click', () => modal.classList.remove('active'));

    // Navigation Logic
    function showScreen(screenId) {
        Object.values(screens).forEach(s => s.classList.remove('active'));
        screens[screenId].classList.add('active');
        
        if (screenId === 'profile') {
            generateQRCode();
        }
    }

    navButtons.toLogin.addEventListener('click', () => showScreen('login'));
    navButtons.doLogin.addEventListener('click', () => showScreen('profile'));
    navButtons.backToHome.addEventListener('click', () => showScreen('home'));
    navButtons.backToLogin.addEventListener('click', () => showScreen('home')); // Back from login goes home

    // Theme Toggle
    themeToggle.addEventListener('click', toggleTheme);

    // QR Code Generation
    function generateQRCode() {
        qrcodeContainer.innerHTML = '';
        const userData = {
            first_name: "Артём",
            last_name: "Библев",
            surname: "Викторович"
        };
        const qrData = `${userData.last_name} ${userData.first_name} ${userData.surname}`;
        new QRCode(qrcodeContainer, {
            text: qrData,
            width: 156,
            height: 156,
            colorDark: "#000000",
            colorLight: "#ffffff",
            correctLevel: QRCode.CorrectLevel.M
        });
    }

    // Server Connection Test
    testConnectionBtn.addEventListener('click', () => {
        const ip = document.getElementById('server-ip');
        const port = document.getElementById('server-port');

        if (!ip.value || !port.value) {
            testConnectionBtn.textContent = 'Введите данные';
            testConnectionBtn.classList.add('error');
            setTimeout(() => {
                testConnectionBtn.textContent = 'Проверить подключение';
                testConnectionBtn.classList.remove('error');
            }, 2000);
            return;
        }

        testConnectionBtn.textContent = 'Проверка...';
        testConnectionBtn.classList.remove('success', 'error');

        // Simulate API request
        setTimeout(() => {
            testConnectionBtn.textContent = 'Подключено';
            testConnectionBtn.classList.add('success');
            setTimeout(() => {
                modal.classList.remove('active');
                testConnectionBtn.textContent = 'Проверить подключение';
                testConnectionBtn.classList.remove('success');
            }, 1000);
        }, 1500);
    });
});
