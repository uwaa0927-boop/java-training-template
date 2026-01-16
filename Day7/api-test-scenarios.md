# APIテストシナリオと結果

## テスト実施日
2026-01-16

## シナリオ1: 北海道（寒冷地）

### リクエスト
GET https://api.open-meteo.com/v1/forecast?latitude=43.064&longitude=141.347&current=temperature_2m,weathercode&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=Asia/Tokyo&forecast_days=7


### 結果
- Status: 200 OK
- Response Time: 2.23s
- 現在気温: -3.7℃
- 天気コード: 2（一部曇り）
- 週間最高気温: -6.1℃ ~ -2.2℃
- 週間最低気温: -14.6℃ ~ -6.3℃

### 学び
- 寒冷地では気温が低くなることが多い
- 気温がマイナスでも問題なく取得できる

---

## シナリオ2: 沖縄（温暖地）

### リクエスト
GET https://api.open-meteo.com/v1/forecast?latitude=26.212&longitude=127.681&current=temperature_2m,weathercode&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=Asia/Tokyo&forecast_days=7


### 結果
- Status: 200 OK
- Response Time: 1.38s
- 現在気温: 21.0℃
- 天気コード: 2（一部曇り）
- 週間最高気温: 15.9℃ ~ 22.9℃
- 週間最低気温: 15.1℃ ~ 20.5℃

### 学び
- 温暖地では気温が高くなることが多い
- 一部曇りや、小雨になることが多い

---

## シナリオ3: 連続取得パフォーマンステスト

### テスト内容
東京 → 大阪 → 福岡 を連続で3回取得（計9リクエスト）

### 結果
| 都道府県 | 試行1 | 試行2 | 試行3 | 平均 |
|---------|------|------|------|------|
| 東京 | 1.44s | 1.56s | 1.57s | 1.52s |
| 大阪 | 1.62s | 1.52s | 1.55s | 1.56s |
| 福岡 | 1.53s | 1.50s | 1.53s | 1.52s |

### 学び
- 平均レスポンスタイム: 約1.53s
- 地域によるレスポンスタイムの差は見られなかった

---

## シナリオ4: エラーケース

### 4-1. 無効な緯度
GET .../forecast?latitude=200&longitude=139.6917

- Status: 400 Bad Request
- Error Message: "reason": "Latitude must be in range of -90 to 90°. Given: 200.0.",
    　　　　　　　　　　　　　　"error": true

### 4-2. 無効な経度
GET .../forecast?latitude=35.6895&longitude=200

- Status: 400 Bad Request
- Error Message: "error": true,
    　　　　　　　　"reason": "Parameter 'latitude' and 'longitude' must have the same number of elements"

### 4-3. パラメータなし
GET .../forecast

- Status: 400 Bad Request
- Error Message: "reason": "Parameter 'latitude' and 'longitude' must have the same number of elements",
    　　　　　　　　"error": true

### 学び
- 不正な値を指定すると400 Bad Requestになる
- エラー時は原因がエラーメッセージに含まれているためわかりやすい

---

## 結論

### APIの信頼性
✅ 正常、異常のどちらも安定してレスポンスが返ってきている
✅ 現在、週間のどちらも問題なく取得できた

### 実装時の考慮事項
1. タイムアウト設定: 5秒
2. リトライ: 3回まで
3. エラーハンドリング: 400/500エラーの処理
4. ローディング表示: 必須
