/**
 * お気に入り管理クラス
 */
class FavoritesManager {
    constructor() {
        this.storageKey = 'weather_app_favorites';
        this.favorites = this.load();
        this.listeners = [];
    }

    /**
     * LocalStorageから読み込み
     */
    load() {
        try {
            const data = localStorage.getItem(this.storageKey);
            if (!data) return [];

            const parsed = JSON.parse(data);
            return Array.isArray(parsed.favorites) ? parsed.favorites : [];
        } catch (error) {
            console.error('お気に入りの読み込みエラー:', error);
            return [];
        }
    }

    /**
     * LocalStorageに保存
     */
    save() {
        try {
            const data = {
                favorites: this.favorites,
                lastUpdated: new Date().toISOString()
            };
            localStorage.setItem(this.storageKey, JSON.stringify(data));
            this.notifyListeners();
        } catch (error) {
            console.error('お気に入りの保存エラー:', error);
        }
    }

    /**
     * お気に入りに追加
     * @param {number} prefectureId - 都道府県ID
     */
    add(prefectureId) {
        const id = parseInt(prefectureId);

        if (this.favorites.includes(id)) {
            console.log('既にお気に入りに追加済み:', id);
            return false;
        }

        this.favorites.push(id);
        this.save();
        console.log('お気に入りに追加:', id);
        return true;
    }

    /**
     * お気に入りから削除
     * @param {number} prefectureId - 都道府県ID
     */
    remove(prefectureId) {
        const id = parseInt(prefectureId);
        const index = this.favorites.indexOf(id);

        if (index === -1) {
            console.log('お気に入りに存在しません:', id);
            return false;
        }

        this.favorites.splice(index, 1);
        this.save();
        console.log('お気に入りから削除:', id);
        return true;
    }

    /**
     * お気に入りトグル
     * @param {number} prefectureId - 都道府県ID
     */
    toggle(prefectureId) {
        if (this.isFavorite(prefectureId)) {
            return this.remove(prefectureId);
        } else {
            return this.add(prefectureId);
        }
    }

    /**
     * お気に入りチェック
     * @param {number} prefectureId - 都道府県ID
     * @returns {boolean}
     */
    isFavorite(prefectureId) {
        return this.favorites.includes(parseInt(prefectureId));
    }

    /**
     * すべて取得
     * @returns {Array<number>}
     */
    getAll() {
        return [...this.favorites];
    }

    /**
     * すべてクリア
     */
    clear() {
        this.favorites = [];
        this.save();
        console.log('お気に入りをすべてクリア');
    }

    /**
     * 件数取得
     * @returns {number}
     */
    count() {
        return this.favorites.length;
    }

    /**
     * リスナー登録
     * @param {Function} callback - コールバック関数
     */
    addListener(callback) {
        this.listeners.push(callback);
    }

    /**
     * リスナー通知
     */
    notifyListeners() {
        this.listeners.forEach(callback => {
            try {
                callback(this.favorites);
            } catch (error) {
                console.error('リスナー実行エラー:', error);
            }
        });
    }

    /**
     * エクスポート（JSON）
     * @returns {string}
     */
    export() {
        return JSON.stringify({
            favorites: this.favorites,
            exportedAt: new Date().toISOString()
        }, null, 2);
    }

    /**
     * インポート（JSON）
     * @param {string} json - JSONデータ
     */
    import(json) {
        try {
            const data = JSON.parse(json);
            if (Array.isArray(data.favorites)) {
                this.favorites = data.favorites;
                this.save();
                return true;
            }
        } catch (error) {
            console.error('インポートエラー:', error);
        }
        return false;
    }
}

// グローバルインスタンス
const favoritesManager = new FavoritesManager();

/**
 * お気に入りボタンUI更新
 */
function updateFavoriteButton(prefectureId, button) {
    const isFav = favoritesManager.isFavorite(prefectureId);

    if (isFav) {
        button.classList.add('active');
        button.innerHTML = '★';
        button.title = 'お気に入りから削除';
    } else {
        button.classList.remove('active');
        button.innerHTML = '☆';
        button.title = 'お気に入りに追加';
    }
}

/**
 * お気に入りボタンクリック処理
 */
function handleFavoriteClick(prefectureId, button) {
    favoritesManager.toggle(prefectureId);
    updateFavoriteButton(prefectureId, button);

    // お気に入り件数更新
    updateFavoriteCount();
}

/**
 * お気に入り件数表示更新
 */
function updateFavoriteCount() {
    const count = favoritesManager.count();
    const badge = document.getElementById('favoriteBadge');

    if (badge) {
        badge.textContent = count;
        badge.style.display = count > 0 ? 'inline-block' : 'none';
    }
}

/**
 * お気に入りボタンのARIA更新
 */
function updateFavoriteButtonAria(prefectureId, button, prefectureName) {
    const isFav = favoritesManager.isFavorite(prefectureId);

    // aria-pressed更新
    button.setAttribute('aria-pressed', isFav);

    // aria-label更新
    const label = isFav
        ? `${prefectureName}をお気に入りから削除`
        : `${prefectureName}をお気に入りに追加`;
    button.setAttribute('aria-label', label);

    // 視覚的表示
    button.innerHTML = isFav
        ? '<span aria-hidden="true">★</span>'
        : '<span aria-hidden="true">☆</span>';
}

/**
 * DOMContentLoaded時の初期化
 */
document.addEventListener('DOMContentLoaded', function() {
    // お気に入りボタン初期化
    const favoriteButtons = document.querySelectorAll('.favorite-btn');
    favoriteButtons.forEach(button => {
        const prefectureId = button.getAttribute('data-prefecture-id');
        updateFavoriteButton(prefectureId, button);

        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            handleFavoriteClick(prefectureId, button);
        });
    });

    // お気に入り件数更新
    updateFavoriteCount();
});

/**
 * お気に入り一覧を表示
 */
function renderFavorites() {
    const favoritesSection = document.getElementById('favoritesSection');
    const favoritesGrid = document.getElementById('favoritesGrid');
    const favoritesEmpty = document.getElementById('favoritesEmpty');

    if (!favoritesSection || !favoritesGrid || !favoritesEmpty) {
            return;
        }

    const favorites = favoritesManager.getAll();

    if (favorites.length === 0) {
        favoritesSection.style.display = 'block';
        favoritesGrid.style.display = 'none';
        favoritesEmpty.style.display = 'block';
        return;
    }

    favoritesSection.style.display = 'block';
    favoritesGrid.style.display = 'grid';
    favoritesEmpty.style.display = 'none';

    // お気に入りカード生成
    favoritesGrid.innerHTML = '';

    favorites.forEach(id => {
        // 都道府県情報取得（DOM から）
        const prefCard = document.querySelector(`[data-prefecture-id="${id}"]`)?.closest('.prefecture-card');
        if (!prefCard) return;

        const prefName = prefCard.querySelector('.prefecture-name')?.textContent || `ID: ${id}`;

        const card = document.createElement('div');
        card.className = 'favorite-card';
        card.innerHTML = `
            <button class="remove-btn" onclick="removeFavorite(${id})" title="削除">×</button>
            <a href="/weather/${id}" style="color: white; text-decoration: none;">
                <div class="favorite-card-content">
                    <h3>${prefName}</h3>
                </div>
            </a>
        `;

        favoritesGrid.appendChild(card);
    });
}

/**
 * お気に入りから削除
 */
function removeFavorite(prefectureId) {
    if (confirm('お気に入りから削除しますか？')) {
        favoritesManager.remove(prefectureId);
        renderFavorites();
        updateFavoriteCount();

        // ボタン更新
        const button = document.querySelector(`[data-prefecture-id="${prefectureId}"]`);
        if (button) {
            updateFavoriteButton(prefectureId, button);
        }
    }
}

/**
 * すべてクリア
 */
function clearAllFavorites() {
    if (confirm('すべてのお気に入りをクリアしますか？')) {
        favoritesManager.clear();
        renderFavorites();
        updateFavoriteCount();

        // すべてのボタン更新
        document.querySelectorAll('.favorite-btn').forEach(button => {
            const id = button.getAttribute('data-prefecture-id');
            updateFavoriteButton(id, button);
        });
    }
}

// リスナー登録
favoritesManager.addListener(renderFavorites);

// 初期表示
document.addEventListener('DOMContentLoaded', function() {
    renderFavorites();
});