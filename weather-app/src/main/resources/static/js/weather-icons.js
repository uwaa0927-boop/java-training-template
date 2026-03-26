/**
 * 天気コードからアイコン名を取得
 */
function getWeatherIcon(weatherCode) {
    const code = parseInt(weatherCode);

    // 快晴
    if (code === 0) return 'sun';

    // 晴れ〜曇り
    if (code >= 1 && code <= 3) {
        if (code === 1) return 'cloud-sun';
        if (code === 2) return 'cloud-sun';
        return 'cloud';
    }

    // 霧
    if (code >= 45 && code <= 48) return 'fog';

    // 雨
    if (code >= 51 && code <= 67) {
        if (code >= 61 && code <= 67) return 'rain';
        return 'drizzle';
    }

    // 雪
    if (code >= 71 && code <= 77) return 'snow';

    // にわか雨
    if (code >= 80 && code <= 82) return 'shower';

    // にわか雪
    if (code >= 85 && code <= 86) return 'snow-shower';

    // 雷雨
    if (code >= 95 && code <= 99) return 'storm';

    return 'unknown';
}

/**
 * 天気アイコン要素を作成
 */
function createWeatherIcon(weatherCode, size = 'md') {
    const iconName = getWeatherIcon(weatherCode);
    const iconPath = `/images/weather-icons/${iconName}.svg`;

    const img = document.createElement('img');
    img.src = iconPath;
    img.alt = getWeatherDescription(weatherCode);
    img.className = `weather-icon weather-icon-${size}`;

    // フォールバック：絵文字
    img.onerror = function() {
        const emoji = getWeatherEmoji(weatherCode);
        this.style.display = 'none';
        const span = document.createElement('span');
        span.textContent = emoji;
        span.className = `weather-emoji weather-emoji-${size}`;
        this.parentNode.replaceChild(span, this);
    };

    return img;
}

/**
 * 天気コードから絵文字を取得（フォールバック用）
 */
function getWeatherEmoji(weatherCode) {
    const code = parseInt(weatherCode);

    if (code === 0) return '☀️';
    if (code === 1) return '🌤️';
    if (code === 2) return '⛅';
    if (code === 3) return '☁️';
    if (code >= 45 && code <= 48) return '🌫️';
    if (code >= 51 && code <= 67) return '🌧️';
    if (code >= 71 && code <= 77) return '❄️';
    if (code >= 80 && code <= 86) return '🌦️';
    if (code >= 95) return '⛈️';

    return '❓';
}

function getWeatherDescription(weatherCode) {
    const code = parseInt(weatherCode);

    if (code === 0) return '快晴';
    if (code === 1) return '晴れ';
    if (code === 2) return '一部曇り';
    if (code === 3) return '曇り';
    if (code >= 45 && code <= 48) return '霧';
    if (code >= 51 && code <= 67) return '雨';
    if (code >= 71 && code <= 77) return '雪';
    if (code >= 80 && code <= 86) return 'にわか雨';
    if (code >= 95) return '雷雨';

    return '不明';
}

/**
 * DOMContentLoaded時に実行
 */
document.addEventListener('DOMContentLoaded', function() {
    // data-weather-code属性を持つ要素を探してアイコン表示
    const iconPlaceholders = document.querySelectorAll('[data-weather-code]');

    iconPlaceholders.forEach(placeholder => {
        if (placeholder.innerHTML.trim() !== '') return;

        const code = placeholder.getAttribute('data-weather-code');
        const size = placeholder.getAttribute('data-size') || 'md';
        const icon = createWeatherIcon(code, size);
        placeholder.appendChild(icon);
    });
});