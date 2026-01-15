# 要件とAPI仕様の統合確認

## 1. トップページ

### 要件
- 47都道府県の一覧表示

### 実装方針
- `prefectures.csv`をデータベースに投入
- `PrefectureRepository.findAll()`で全件取得
- Thymeleafで一覧表示

### 必要なAPI呼び出し
- なし（マスタデータのみ）

---

## 2. 天気詳細ページ

### 要件
- 現在の天気表示
- 週間天気予報（7日間）

### 実装方針
1. URLから都道府県IDを取得
2. データベースから緯度・経度を取得
3. Open-Meteo APIを呼び出し
4. レスポンスをDTOに変換
5. データベースに保存
6. Thymeleafで表示

### 必要なAPI呼び出し
GET https://api.open-meteo.com/v1/forecast
?latitude={lat}
&longitude={lon}
&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m
&daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum
&timezone=Asia/Tokyo
&forecast_days=7


---

## 3. データ保存

### 要件
- API取得データの履歴保存

### 実装方針
- `WeatherRecord`エンティティ作成
- API取得後、`WeatherRecordRepository.save()`

---

## 4. UI/UX

### ローディング表示
- JavaScript + CSSで実装

### エラーハンドリング
- Spring MVCの@ControllerAdvice
- カスタムエラーページ

### レスポンシブデザイン
- CSSメディアクエリ

### 天気アイコン
- 天気コードとアイコンのマッピング

### お気に入り機能
- LocalStorageで実装

---

## 5. 技術的課題と解決策

### 課題1: APIレスポンスの遅延
**解決策**: ローディング表示 + タイムアウト設定

### 課題2: 同時アクセス時のAPI制限
**解決策**: キャッシュ機能（任意）

### 課題3: エラー時のユーザー体験
**解決策**: わかりやすいエラーメッセージ

---

## 6. 次週からの開発フロー

- Day 7: API動作確認とレスポンス解析
- Day 8-9: 画面設計
- Day 10-12: データベース設計
- Day 13-15: シーケンス図と設計レビュー
- Day 16以降: 実装開始
