# API仕様書

## 概要

天気予報アプリのAPI仕様を記載します。

## エンドポイント

### 1. トップページ

**エンドポイント:** 

\`GET /\`

**説明:** 

47都道府県の一覧を地域別に表示

**クエリパラメータ:**

| 名前 | 型 | 必須 | 説明 |
|------|-----|------|------|
| region | String | No | 地域フィルタ（北海道、東北、関東、中部、関西、中国、四国、九州、沖縄） |

**レスポンス:**

- ステータス: 200 OK
- Content-Type: text/html
- ビュー: index.html

**例:**

GET /?region=関東

### 2. 天気詳細ページ

**エンドポイント:** 

\`GET /weather/{id}\`

**説明:** 

指定した都道府県の天気情報を表示

**パスパラメータ:**

| 名前 | 型 | 必須 | 説明 |
|------|-----|------|------|
| id | Long | Yes | 都道府県ID（1-47） |

**レスポンス:**

- ステータス: 200 OK
- Content-Type: text/html
- ビュー: weather-detail.html

**エラー:**

- 404 Not Found: 都道府県が見つからない
- 503 Service Unavailable: API接続エラー

**例:**

GET /weather/13 # 東京都

## データモデル

### Prefecture（都道府県）

{
  "id": 13,
  "name": "東京都",
  "nameEn": "Tokyo",
  "region": "関東",
  "latitude": 35.6895,
  "longitude": 139.6917
}

### WeatherDetail（天気詳細）

{
  "prefecture": {
    "id": 13,
    "name": "東京都"
  },
  "current": {
    "time": "2024-01-05T15:00:00",
    "temperature": 15.5,
    "apparentTemperature": 14.2,
    "weatherCode": 1,
    "weatherDescription": "晴れ",
    "humidity": 65,
    "windSpeed": 3.2,
    "precipitation": 0.0,
    "cloudCover": 20
  },
  "dailyForecasts": [
    {
      "date": "2024-01-05",
      "dayOfWeek": "金",
      "temperatureMax": 18.0,
      "temperatureMin": 10.0,
      "weatherCode": 1,
      "weatherDescription": "晴れ",
      "precipitationSum": 0.0,
      "sunrise": "2024-01-05T06:50:00",
      "sunset": "2024-01-05T16:38:00"
    }
  ],
  "fetchedAt": "2024-01-05T15:30:00"
}

## エラーコード

| コード | 説明 | 対処方法 |
|--------|------|----------|
| 400 | 不正なリクエスト | リクエストパラメータを確認 |
| 404 | リソースが見つからない | URLを確認 |
| 500 | サーバーエラー | 管理者に連絡 |
| 503 | サービス利用不可 | しばらく待ってリトライ |
