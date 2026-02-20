-- テスト用の最小限データ
DELETE FROM daily_forecasts;
DELETE FROM weather_records;
DELETE FROM prefectures;

-- 都道府県テストデータ

-- 北海道
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (1, '北海道', 'Hokkaido', 43.064167, 141.346944, '北海道');

-- 東北
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (2, '青森県', 'Aomori', 40.824444, 140.74, '東北'),
    (3, '岩手県', 'Iwate', 39.703611, 141.1525, '東北'),
    (4, '宮城県', 'Miyagi', 38.268889, 140.872222, '東北'),
    (5, '秋田県', 'Akita', 39.718611, 140.1025, '東北'),
    (6, '山形県', 'Yamagata', 38.240556, 140.363333, '東北'),
    (7, '福島県', 'Fukushima', 37.75, 140.467778, '東北');

-- 関東
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (8, '茨城県', 'Ibaraki', 36.341389, 140.446667, '関東'),
    (9, '栃木県', 'Tochigi', 36.565556, 139.883611, '関東'),
    (10, '群馬県', 'Gunma', 36.390833, 139.060556, '関東'),
    (11, '埼玉県', 'Saitama', 35.856944, 139.648889, '関東'),
    (12, '千葉県', 'Chiba', 35.605, 140.123333, '関東'),
    (13, '東京都', 'Tokyo', 35.689487, 139.691706, '関東'),
    (14, '神奈川県', 'Kanagawa', 35.447778, 139.6425, '関東');

-- 中部
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (15, '新潟県', 'Niigata', 37.902222, 139.023611, '中部'),
    (16, '富山県', 'Toyama', 36.695278, 137.211389, '中部'),
    (17, '石川県', 'Ishikawa', 36.594444, 136.625556, '中部'),
    (18, '福井県', 'Fukui', 36.065278, 136.221667, '中部'),
    (19, '山梨県', 'Yamanashi', 35.664167, 138.568333, '中部'),
    (20, '長野県', 'Nagano', 36.651389, 138.181111, '中部'),
    (21, '岐阜県', 'Gifu', 35.391111, 136.722222, '中部'),
    (22, '静岡県', 'Shizuoka', 34.976944, 138.383056, '中部'),
    (23, '愛知県', 'Aichi', 35.180278, 136.906389, '中部');

-- 関西
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (24, '三重県', 'Mie', 34.730278, 136.508611, '関西'),
    (25, '滋賀県', 'Shiga', 35.004444, 135.868333, '関西'),
    (26, '京都府', 'Kyoto', 35.021389, 135.755556, '関西'),
    (27, '大阪府', 'Osaka', 34.686389, 135.52, '関西'),
    (28, '兵庫県', 'Hyogo', 34.691389, 135.183056, '関西'),
    (29, '奈良県', 'Nara', 34.685278, 135.832778, '関西'),
    (30, '和歌山県', 'Wakayama', 34.225833, 135.1675, '関西');

-- 中国
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (31, '鳥取県', 'Tottori', 35.503889, 134.238056, '中国'),
    (32, '島根県', 'Shimane', 35.472222, 133.050556, '中国'),
    (33, '岡山県', 'Okayama', 34.661667, 133.934722, '中国'),
    (34, '広島県', 'Hiroshima', 34.396389, 132.459444, '中国'),
    (35, '山口県', 'Yamaguchi', 34.186111, 131.470556, '中国');

-- 四国
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (36, '徳島県', 'Tokushima', 34.065833, 134.559444, '四国'),
    (37, '香川県', 'Kagawa', 34.340278, 134.043333, '四国'),
    (38, '愛媛県', 'Ehime', 33.841667, 132.765556, '四国'),
    (39, '高知県', 'Kochi', 33.559722, 133.531111, '四国');

-- 九州
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (40, '福岡県', 'Fukuoka', 33.606389, 130.418056, '九州'),
    (41, '佐賀県', 'Saga', 33.249444, 130.299167, '九州'),
    (42, '長崎県', 'Nagasaki', 32.744722, 129.873611, '九州'),
    (43, '熊本県', 'Kumamoto', 32.789722, 130.741667, '九州'),
    (44, '大分県', 'Oita', 33.238056, 131.6125, '九州'),
    (45, '宮崎県', 'Miyazaki', 31.911111, 131.423889, '九州'),
    (46, '鹿児島県', 'Kagoshima', 31.560278, 130.558056, '九州');

-- 沖縄
INSERT INTO prefectures (id, name, name_en, latitude, longitude, region) VALUES
    (47, '沖縄県', 'Okinawa', 26.2125, 127.680833, '沖縄');

-- Reset H2 identity sequence to avoid collision
ALTER TABLE prefectures ALTER COLUMN id RESTART WITH 100;