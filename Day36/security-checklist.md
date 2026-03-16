# セキュリティチェックリスト

## ✅ XSS対策
- [x] すべてのユーザー入力をエスケープ
- [x] Thymeleafのth:text使用
- [ ] Content-Security-Policy設定

## ✅ CSRF対策
- [ ] Spring SecurityのCSRF有効化
- [ ] フォームにCSRFトークン

## ✅ SQLインジェクション対策
- [x] JPAのパラメータバインディング使用
- [x] 生SQLを使用しない

## ✅ 認証・認可
- [ ] パスワードのハッシュ化
- [ ] セッション管理の適切な実装

## ✅ HTTPヘッダー
- [ ] X-Frame-Options設定
- [ ] X-Content-Type-Options設定
- [ ] Strict-Transport-Security設定

## ✅ 依存関係
- [ ] 脆弱性のある依存関係なし
- [ ] 定期的なアップデート
