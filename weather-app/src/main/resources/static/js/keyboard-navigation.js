console.log("keyboard navigation loaded");
/**
 * キーボードナビゲーション
 */
class KeyboardNavigation {
    constructor() {
        console.log("keyboard-navigation loaded");
        this.init();
    }

    init() {
        // キーボードユーザー検出
        document.addEventListener('keydown', this.detectKeyboardUser.bind(this));
        document.addEventListener('mousedown', this.detectMouseUser.bind(this));

        // キーボードショートカット
        document.addEventListener('keydown', this.handleShortcuts.bind(this));

        // フォーカストラップ（モーダル用）
        this.setupFocusTrap();
    }

    /**
     * キーボードユーザー検出
     */
    detectKeyboardUser(e) {
        if (e.key === 'Tab') {
            document.body.classList.add('keyboard-user');
            document.body.classList.remove('mouse-user');
        }
    }

    /**
     * マウスユーザー検出
     */
    detectMouseUser() {
        document.body.classList.add('mouse-user');
        document.body.classList.remove('keyboard-user');
    }

    /**
     * キーボードショートカット
     */
    handleShortcuts(e) {
        console.log("key pressed:", e.key);

        // Ctrl/Cmd + K: 検索フォーカス
        if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
            e.preventDefault();
            const searchInput = document.getElementById('searchInput');
            if (searchInput) {
                searchInput.focus();
            }
        }

        // Esc: モーダルを閉じる
        if (e.key === 'Escape') {
            this.closeModals();
        }

        // ?: ヘルプ表示
        if (e.key === '?') {
            this.showKeyboardHelp();
        }
    }

    /**
     * フォーカストラップ設定
     */
    setupFocusTrap() {
        const modals = document.querySelectorAll('[role="dialog"]');

        modals.forEach(modal => {
            modal.addEventListener('keydown', (e) => {
                if (e.key === 'Tab') {
                    this.trapFocus(e, modal);
                }
            });
        });
    }

    /**
     * フォーカストラップ
     */
    trapFocus(e, container) {
        const focusable = container.querySelectorAll(
            'a[href], button:not([disabled]), textarea, input, select'
        );

        const firstFocusable = focusable[0];
        const lastFocusable = focusable[focusable.length - 1];

        if (e.shiftKey) {
            if (document.activeElement === firstFocusable) {
                e.preventDefault();
                lastFocusable.focus();
            }
        } else {
            if (document.activeElement === lastFocusable) {
                e.preventDefault();
                firstFocusable.focus();
            }
        }
    }

    /**
     * モーダルを閉じる
     */
    closeModals() {
        const modals = document.querySelectorAll('[role="dialog"][aria-hidden="false"]');
        modals.forEach(modal => {
            modal.setAttribute('aria-hidden', 'true');
            modal.style.display = 'none';
        });
    }

    /**
     * キーボードヘルプ表示
     */
    showKeyboardHelp() {
        alert(`
キーボードショートカット:
Ctrl/Cmd + K: 検索
Tab: 次の要素へ移動
Shift + Tab: 前の要素へ移動
Enter/Space: 選択・実行
Esc: モーダルを閉じる
?: このヘルプを表示
        `);
    }
}

// 初期化
document.addEventListener('DOMContentLoaded', function() {
    new KeyboardNavigation();
});