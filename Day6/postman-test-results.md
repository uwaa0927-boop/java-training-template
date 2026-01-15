## テスト日時
2026-01-15 14:00~

## テストケース

### 1. 東京の現在天気取得
- **URL**: https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m&timezone=Asia/Tokyo
- **Result**: ✅ 成功
- **Status**: 200 OK
- **Response Time**: 1.52s
- **Temperature**: 9.9℃
- **Weather Code**: 0

### 2. 東京の週間予報取得
- **URL**: https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum&timezone=Asia/Tokyo&forecast_days=7
- **Result**: ✅ 成功
- **Status**: 200 OK
- **Response Time**: 294ms
- **Days**: 7日分のデータ取得確認

### 3. 北海道の天気取得
- **URL**: https://api.open-meteo.com/v1/forecast?latitude=43.064&longitude=141.347&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m&timezone=Asia/Tokyo
- **Result**: ✅ 成功
- **Temperature**: -2.6℃

### 4. エラーケース: 無効な緯度
- **URL**:https://api.open-meteo.com/v1/forecast?latitude=200&longitude=139.6917&current=temperature_2m,weathercode,windspeed_10m,relativehumidity_2m&timezone=Asia/Tokyohttps://api.open-meteo.com/v1/forecast?latitude=200&longitude=139.6917&current=temperature_2m
- **Result**: ✅ エラーハンドリング確認
- **Status**: 400 Bad Request
- **Error Message**: "Latitude must be in range of -90 to 90°. Given: 200.0."
