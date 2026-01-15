# Open-Meteo API 調査レポート

## APIの概要

Open-Meteoは無料で使える天気予報API

### 特徴
- **無料**: 商用利用も可能
- **認証不要**: API キーやトークン不要
- **高速**: レスポンスが速い
- **豊富なデータ**: 様々な気象データを提供

## エンドポイント

### Weather Forecast API
https://api.open-meteo.com/v1/forecast

## 必須パラメータ

天気を取得するために場所の指定が必要なため、場所ごとに緯度/経度を用意しておく必要がある
| パラメータ | 型 | 説明 | 例 |
|-----------|---|------|-----|
| latitude | float | 緯度 | 35.6895 |
| longitude | float | 経度 | 139.6917 |

## オプションパラメータ

### current（現在の天気）
current=で現在の天気として取得したい値を指定できる
current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m

利用可能な値：
- `temperature_2m`: 地上2mの気温（℃）
- `weathercode`: 天気コード
- `windspeed_10m`: 風速（m/s）
- `relativehumidity_2m`: 相対湿度（%）
- `apparent_temperature`: 体感温度
- `precipitation`: 降水量
- `cloudcover`: 雲量

### daily（日次予報）
daily=で毎日の天気として取得したい値を指定できる
daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum

利用可能な値：
- `temperature_2m_max`: 最高気温
- `temperature_2m_min`: 最低気温
- `weathercode`: 天気コード
- `precipitation_sum`: 降水量合計
- `sunrise`: 日の出時刻
- `sunset`: 日の入り時刻

### その他のパラメータ
- `timezone`: タイムゾーン（例: Asia/Tokyo）
- `forecast_days`: 予報日数（1-16日、デフォルト7日）

## 天気コード（WMO Weather Code）

天気コードは数値で返ってくるため、日本語などで表示する場合は別途変換させる必要がある
| コード | 説明 | アイコン候補 |
|--------|------|------------|
| 0 | 快晴 | ☀️ |
| 1, 2, 3 | 晴れ、一部曇り、曇り | 🌤️ ⛅ ☁️ |
| 45, 48 | 霧 | 🌫️ |
| 51, 53, 55 | 霧雨 | 🌦️ |
| 61, 63, 65 | 雨 | 🌧️ |
| 71, 73, 75 | 雪 | ❄️ |
| 77 | 雪の粒 | 🌨️ |
| 80, 81, 82 | にわか雨 | 🌦️ |
| 85, 86 | にわか雪 | 🌨️ |
| 95 | 雷雨 | ⛈️ |
| 96, 99 | 雷雨（雹） | ⛈️ |

## リクエスト例

### 東京の現在の天気
GET：https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m&timezone=Asia/Tokyo


### 東京の週間天気予報
GET：https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum&timezone=Asia/Tokyo&forecast_days=7


### 現在 + 週間予報
GET：https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m&daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum&timezone=Asia/Tokyo&forecast_days=7

## レスポンス例

JSON形式で返ってくる
```json
{
  "latitude": 35.6895,
  "longitude": 139.6917,
  "generationtime_ms": 0.123,
  "utc_offset_seconds": 32400,
  "timezone": "Asia/Tokyo",
  "timezone_abbreviation": "JST",
  "elevation": 40.0,
  "current": {
    "time": "2024-01-04T15:00",
    "temperature_2m": 12.5,
    "weathercode": 1,
    "windspeed_10m": 3.2,
    "relativehumidity_2m": 65
  },
  "daily": {
    "time": ["2024-01-04", "2024-01-05", ...],
    "temperature_2m_max": [15.2, 14.8, ...],
    "temperature_2m_min": [8.1, 7.5, ...],
    "weathercode": [1, 3, ...],
    "precipitation_sum": [0.0, 2.5, ...]
  }
}

### レート制限

無料プラン: 10,000リクエスト/日(10,000以上は有料)
商用利用: 可能（有料）

### エラーレスポンス

## パラメータエラー
入力したパラメーターが不正な場合はエラーとして返ってくる
{
  "error": true,
  "reason": "Latitude must be in range of -90 to 90°. Given: 200."
}
