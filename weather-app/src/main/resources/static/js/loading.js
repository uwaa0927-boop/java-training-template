/**
 * ローディング管理クラス
 */
class LoadingManager {
    constructor() {
        this.overlay = null;
        this.init();
    }

    /**
     * 初期化
     */
    init() {
        // オーバーレイ作成
        this.overlay = document.createElement('div');
        this.overlay.className = 'loading-overlay';
        this.overlay.id = 'loadingOverlay';

        // スピナーとテキスト
        this.overlay.innerHTML = `
            <div class="spinner-container">
                <div class="spinner"></div>
                <div class="loading-text">読み込み中...</div>
                <div class="progress-bar progress-bar-indeterminate">
                    <div class="progress-bar-fill"></div>
                </div>
            </div>
        `;

        document.body.appendChild(this.overlay);
    }

    /**
     * ローディング表示
     * @param {string} message - 表示メッセージ
     */
    show(message = '読み込み中...') {
        const textEl = this.overlay.querySelector('.loading-text');
        if (textEl) {
            textEl.textContent = message;
        }

        // 少し遅延して表示（すぐ終わる処理で点滅させない）
        setTimeout(() => {
            this.overlay.classList.add('show');
        }, 100);
    }

    /**
     * ローディング非表示
     */
    hide() {
        this.overlay.classList.remove('show');
    }

    /**
     * メッセージ更新
     * @param {string} message - 新しいメッセージ
     */
    updateMessage(message) {
        const textEl = this.overlay.querySelector('.loading-text');
        if (textEl) {
            textEl.textContent = message;
        }
    }
}

// グローバルインスタンス
const loading = new LoadingManager();

/**
 * ページ読み込み時のローディング
 */
document.addEventListener('DOMContentLoaded', function() {

    // すべてのリンクにローディング追加
    const weatherLinks = document.querySelectorAll('a[href^="/weather/"]');
    weatherLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            loading.show('天気情報を取得中...');
        });
    });

    // フォーム送信時
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            loading.show('処理中...');
        });
    });

    // 地域フィルタ変更時
    const regionSelect = document.getElementById('regionSelect');
    if (regionSelect) {
        regionSelect.addEventListener('change', function() {
            loading.show('地域情報を読み込み中...');
        });
    }
});

/**
 * ページ読み込み完了時にローディング非表示
 */
window.addEventListener('load', function() {
    loading.hide();
});

/**
 * 戻るボタン時もローディング非表示
 */
window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        loading.hide();
    }
});

/**
 * スケルトンローディング表示
 */
function showSkeletonLoading(containerId, count = 3) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = '';

    for (let i = 0; i < count; i++) {
        const skeleton = document.createElement('div');
        skeleton.className = 'skeleton-card';
        skeleton.innerHTML = `
            <div class="skeleton skeleton-title"></div>
            <div class="skeleton skeleton-text"></div>
            <div class="skeleton skeleton-text"></div>
        `;
        container.appendChild(skeleton);
    }
}

/**
 * スケルトンローディング非表示
 */
function hideSkeletonLoading(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.classList.remove('loading');
}

/**
 * Intersection Observerで画像遅延ロード
 */
document.addEventListener('DOMContentLoaded', function() {
    const lazyImages = document.querySelectorAll('img.lazy-load');

    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy-load');
                    img.classList.add('loaded');
                    imageObserver.unobserve(img);
                }
            });
        });

        lazyImages.forEach(img => imageObserver.observe(img));
    } else {
        // フォールバック：すぐにロード
        lazyImages.forEach(img => {
            img.src = img.dataset.src;
        });
    }
});