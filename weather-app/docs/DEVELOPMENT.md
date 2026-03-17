# 開発ガイド

## 開発環境セットアップ

### IDE設定

#### IntelliJ IDEA推奨設定

1. Lombok Plugin インストール
2. Enable annotation processing
3. Code Style: Google Java Style

#### VS Code推奨設定

1. Extension Pack for Java
2. Spring Boot Extension Pack
3. Lombok Annotations Support

### コーディング規約

#### Java

- インデント: 4スペース
- 行の長さ: 120文字以内
- 命名規則: キャメルケース
- JavaDoc: public メソッドに必須

#### CSS

- インデント: 2スペース
- BEM命名規則
- CSS変数を活用

#### JavaScript

- インデント: 2スペース
- ES6+構文使用
- セミコロン必須

### Git運用

#### ブランチ戦略

main          # 本番
  └─ develop  # 開発
      └─ feature/xxx  # 機能開発
      
#### コミットメッセージ

<type>(<scope>): <subject>

feat: 新機能
fix: バグ修正
docs: ドキュメント
style: フォーマット
refactor: リファクタリング
test: テスト追加
chore: ビルド・ツール

例:
feat(weather): 7日間予報機能追加
fix(ui): レスポンシブ表示の不具合修正

## ビルド・デプロイ

### ローカルビルド

./gradlew clean build

### 開発サーバー起動

./gradlew bootRun

### 本番ビルド

./gradlew clean build -Pprofile=prod

## トラブルシューティング

### よくある問題

#### ポート8080が使用中

＃ プロセス確認
lsof -i :8080

＃ プロセス停止
kill -9 <PID>

#### MySQLに接続できない

1. MySQLが起動しているか確認
2. ユーザー名・パスワードを確認
3. データベースが作成されているか確認

#### テストが失敗する

./gradlew clean test --info
