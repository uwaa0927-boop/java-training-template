# prefectures（都道府県マスタ）
|    列名     |    データ型    | NULL |    デフォルト   　   |   説明　  |
| 　　---- 　　| 　 　----　 　 | ---- | 　　　　 ---- 　 　　|　  ---- 　|
| id　    　  | BIGINT 　     | NO   | AUTO_INCREMENT 　  | 主キー    |
| name 　　   | VARCHAR(10) 　| NO   | - 　　　　　　  　   | 都道府県名 |
| name_en　　 | VARCHAR(50) 　| NO   | - 　　　　　　 　    | 英語名 　　|
| latitude   | DECIMAL(9,6)  | NO   | - 　　　　　　　     | 緯度 　　　|
| longitude  | DECIMAL(9,6)  | NO 　| - 　　　　　　 　    | 経度 　　  |
| region     | VARCHAR(20)   | NO   | - 　　　　　　  　   | 地域区分 　|
| created_at | TIMESTAMP 　　 | NO   | CURRENT_TIMESTAMP | 作成日時   |
| updated_at | TIMESTAMP 　　 | NO   | ON UPDATE 　　　   | 更新日時   |

---

# weather_records（天気記録）
|    　　　列名　  　　   |    データ型    | NULL |    デフォルト   　   |       　説明　　　　   |
| 　　　　　---- 　　　 　| 　 　----　 　 | ---- | 　　　　 ---- 　 　　 |　 　　　  ---- 　　　　|
| id 　　　　　　　　　　　| BIGINT 　　　  | NO 　| AUTO_INCREMENT 　  | 主キー 　 　　　　　　  |
| prefecture_id 　　　　| BIGINT 　　　  | NO   | - 　　　　　　　　　  | 都道府県ID 　　　　　   |
| fetched_at　　　　　　 | TIMESTAMP 　  | NO   | - 　　　　　　　　　  | 取得日時　　　　　　    |
| temperature 　　　　　 | DECIMAL(5,2) | YES 　| - 　　　         　 | 気温　　　　　　　　　  |
| weather_code 　　　   | INT 　　　　   | YES  | - 　　　　　　　　　  | 天気コード 　　　　　　 |
| wind_speed 　　　　　  | DECIMAL(5,2) | YES 　| - 　　　　　　　　　  | 風速 　　　　　　　　　 |
| humidity 　　　　　　  | INT 　　　　   | YES　 | - 　　　　　　　　　  | 湿度　　　　　　　　　  |
| apparent_temperature | DECIMAL(5,2) | YES　 | - 　　　　　　　　　  | 体感温度 　　　　　　　 |
| precipitation 　　　  | DECIMAL(5,2) | YES 　| - 　　　　　　　　　  | 降水量　　　　　　　　  |
| cloud_cover 　　　    | INT 　　　　   | YES 　| - 　　　　　　　　　  | 雲量　　　　　　　　　  |
| created_at  　　　    | TIMESTAMP 　　| NO 　 | CURRENT_TIMESTAMP　| 作成日時 　　　　　　　 |

---

# daily_forecasts（日次予報）

| 列名　　　　　　　　  | 　　データ型 　 | NULL | 　　 デフォルト 　　　| 　　説明 　　|
| 　　　　---- 　　　　| 　 　----　 　 | ---- | 　　 　 ---- 　 　  |　  ---- 　　|
| id 　　　　　　　　  | BIGINT 　   　| NO  　| AUTO_INCREMENT    | 主キー 　   |
| weather_record_id | BIGINT 　　   | NO 　 | - 　　　　　　　　   | 天気記録ID  |
| forecast_date　 　 | DATE 　　　   | NO 　 | - 　　　　　　　　   | 予報の日付 　|
| temperature_max   | DECIMAL(5,2) | YES 　| - 　　　　　　　　   | 最高気温　　 |
| temperature_min   | DECIMAL(5,2) | YES　 | - 　　　　　　　　   | 最低気温 　  |
| weather_code 　　  | INT 　　　　 　| YES 　| - 　　　　　　　　   | 天気コード  |
| precipitation_sum | DECIMAL(5,2) | YES 　| - 　　　　　　　　   | 降水量の合計 |
| wind_speed_max    | DECIMAL(5,2) | YES 　| - 　　　　　　　　   | 最大風速 　  |
| sunrise 　　　　　  | TIME 　　　   | YES 　| - 　　　　　　　　   | 日の出時刻 　|
| sunset 　　　　　　 | TIME　　　  　 | YES 　| - 　　　　　　　　   | 日の入り時刻 |
| created_at 　　　  | TIMESTAMP　　 | NO　  | CURRENT_TIMESTAMP | 作成日時   　|
