# 運用マニュアル

## デプロイ手順

### 1. 事前準備

- [ ] コードレビュー完了
- [ ] テスト全パス確認
- [ ] ドキュメント更新
- [ ] リリースノート作成

### 2. デプロイ

＃ 本番ビルド
./gradlew clean build -Pprofile=prod

＃ バックアップ
cp app.jar app.jar.backup

＃ デプロイ
cp build/libs/weather-app.jar app.jar

＃ 起動
java -jar app.jar

### 3. 動作確認

- [ ] トップページ表示確認
- [ ] 天気情報取得確認
- [ ] エラーページ確認
- [ ] レスポンスタイム確認

## 監視

### ヘルスチェック

curl http://localhost:8080/actuator/health

### ログ確認

tail -f logs/application.log

## バックアップ

### データベースバックアップ

mysqldump -u root -p weather_app > backup_\$(date +%Y%m%d).sql

### リストア

mysql -u root -p weather_app < backup_20240105.sql

## 障害対応

### 障害レベル

| レベル | 説明 | 対応時間 |
|--------|------|----------|
| Critical | サービス停止 | 即時 |
| High | 主要機能停止 | 1時間以内 |
| Medium | 一部機能停止 | 4時間以内 |
| Low | 軽微な不具合 | 1営業日以内 |

### エスカレーション

1. 開発チーム
2. リードエンジニア
3. プロジェクトマネージャー
