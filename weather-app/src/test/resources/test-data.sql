-- テスト用の最小限データ
DELETE FROM daily_forecasts;
DELETE FROM weather_records;
DELETE FROM prefectures;

-- 主要な都道府県のみ投入
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
        (1, '北海道', 'Hokkaido', 43.064, 141.347, '北海道'),
        (8, '茨城県', 'Ibaraki', 36.341, 140.447, '関東'),
        (9, '栃木県', 'Tochigi', 36.566, 139.883, '関東'),
        (10, '群馬県', 'Gunma', 36.391, 139.061, '関東'),
        (11, '埼玉県', 'Saitama', 35.857, 139.649, '関東'),
        (12, '千葉県', 'Chiba', 35.605, 140.123, '関東'),
        (13, '東京都', 'Tokyo', 35.689, 139.692, '関東'),
        (14, '神奈川県', 'Kanagawa', 35.448, 139.643, '関東'),
        (26, '京都府', 'Kyoto', 35.021, 135.756, '関西'),
        (27, '大阪府', 'Osaka', 34.686, 135.520, '関西'),
        (47, '沖縄県', 'Okinawa', 26.212, 127.681, '沖縄');