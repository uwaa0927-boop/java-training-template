-- データベース作成
CREATE DATABASE IF NOT EXISTS weather_app
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE weather_app;

-- 既存テーブルを削除（開発環境のみ）
DROP TABLE IF EXISTS daily_forecasts;
DROP TABLE IF EXISTS weather_records;
DROP TABLE IF EXISTS prefectures;

-- prefectures（都道府県マスタ）
CREATE TABLE prefectures (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(10) NOT NULL COMMENT '都道府県名',
                             name_en VARCHAR(50) NOT NULL COMMENT '英語名',
                             latitude DECIMAL(9, 6) NOT NULL COMMENT '緯度',
                             longitude DECIMAL(9, 6) NOT NULL COMMENT '経度',
                             region VARCHAR(20) NOT NULL COMMENT '地域区分',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='都道府県マスタ';

-- weather_records（天気記録）
CREATE TABLE weather_records (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 prefecture_id BIGINT NOT NULL COMMENT '都道府県ID',
                                 fetched_at TIMESTAMP NOT NULL COMMENT '取得日時',
                                 temperature DECIMAL(5, 2) COMMENT '気温（℃）',
                                 weather_code INT COMMENT '天気コード',
                                 wind_speed DECIMAL(5, 2) COMMENT '風速（m/s）',
                                 humidity INT COMMENT '湿度（%）',
                                 apparent_temperature DECIMAL(5, 2) COMMENT '体感温度（℃）',
                                 precipitation DECIMAL(5, 2) COMMENT '降水量（mm）',
                                 cloud_cover INT COMMENT '雲量（%）',
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY fk_prefecture (prefecture_id)
                                     REFERENCES prefectures(id) ON DELETE CASCADE,
                                 INDEX idx_prefecture (prefecture_id),
                                 INDEX idx_fetched (fetched_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='天気記録';

-- daily_forecasts（日次予報）
CREATE TABLE daily_forecasts (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 weather_record_id BIGINT NOT NULL COMMENT '天気記録ID',
                                 forecast_date DATE NOT NULL COMMENT '予報日',
                                 temperature_max DECIMAL(5, 2) COMMENT '最高気温（℃）',
                                 temperature_min DECIMAL(5, 2) COMMENT '最低気温（℃）',
                                 weather_code INT COMMENT '天気コード',
                                 precipitation_sum DECIMAL(5, 2) COMMENT '降水量合計（mm）',
                                 wind_speed_max DECIMAL(5, 2) COMMENT '最大風速（m/s）',
                                 sunrise TIME COMMENT '日の出時刻',
                                 sunset TIME COMMENT '日の入り時刻',
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY fk_weather_record (weather_record_id)
                                     REFERENCES weather_records(id) ON DELETE CASCADE,
                                 INDEX idx_record (weather_record_id),
                                 INDEX idx_date (forecast_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日次予報';