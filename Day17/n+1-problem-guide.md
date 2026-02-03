# N+1問題の理解と対策

## N+1問題とは

### 問題の例

// 1. 都道府県を全件取得（1回のクエリ）
List<Prefecture> prefectures = prefectureRepository.findAll();

// 2. 各都道府県の天気記録を取得（N回のクエリ）
for (Prefecture pref : prefectures) {
    List<WeatherRecord> records = weatherRecordRepository
        .findByPrefecture(pref);  // ← 47回実行される！
}

// 合計: 1 + 47 = 48回のクエリ

## 発行されるSQL

-- 1回目
SELECT * FROM prefectures;

-- 2回目以降（47回）
SELECT * FROM weather_records WHERE prefecture_id = 1;
SELECT * FROM weather_records WHERE prefecture_id = 2;
SELECT * FROM weather_records WHERE prefecture_id = 3;
...
SELECT * FROM weather_records WHERE prefecture_id = 47;

### パフォーマンスへの影響

- データベースへのアクセス回数が激増
- レスポンスタイムが遅くなる
- データベースサーバーの負荷が上がる


## 対策1：JOIN FETCH

### @queryでの実装

@Query("SELECT w FROM WeatherRecord w " +
       "LEFT JOIN FETCH w.dailyForecasts " +
       "WHERE w.prefecture.id = :prefectureId")
Optional<WeatherRecord> findByPrefectureWithForecasts(
    @Param("prefectureId") Long prefectureId
);

### 発行されるSQL

SELECT w.*, df.* 
FROM weather_records w
LEFT JOIN daily_forecasts df ON w.id = df.weather_record_id
WHERE w.prefecture_id = 13;

### メリット

- 1回のクエリで関連データも取得
- N+1問題が発生しない


## 対策2：@entitygraph

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {
    
    @EntityGraph(attributePaths = {"dailyForecasts", "prefecture"})
    Optional<WeatherRecord> findById(Long id);
}

### メリット

- アノテーションで簡単に指定
- メソッド名規約で使える


## 対策3：Batch Fetch Size

application.propertiesに追加：
spring.jpa.properties.hibernate.default_batch_fetch_size=10

### 効果：

- N+1問題は解決しないが、クエリ数を減らせる
- 47回 → 5回程度に削減


## 実装例：正しいデータ取得

### 悪い例（N+1発生）

@GetMapping("/prefectures-with-weather")
public List<PrefectureWeatherDto> getAllWithWeather() {
    List<Prefecture> prefectures = prefectureRepository.findAll();
    
    return prefectures.stream()
        .map(pref -> {
            // ← ここでN+1発生！
            Optional<WeatherRecord> record = weatherRecordRepository
                .findFirstByPrefectureOrderByFetchedAtDesc(pref);
            
            return new PrefectureWeatherDto(pref, record.orElse(null));
        })
        .collect(Collectors.toList());
}

### 良い例（1回のクエリ）

@Query("SELECT p, w FROM Prefecture p " +
       "LEFT JOIN WeatherRecord w ON w.prefecture.id = p.id " +
       "WHERE w.id IN (" +
       "  SELECT MAX(w2.id) FROM WeatherRecord w2 " +
       "  GROUP BY w2.prefecture.id" +
       ") OR w.id IS NULL")
List<Object[]> findAllPrefecturesWithLatestWeather();


## ベストプラクティス

1. 関連データが必要な場合は JOIN FETCH
2. 不要な場合は LAZY fetch（デフォルト）
3. Hibernateのクエリログを確認
　spring.jpa.show-sql=true
　logging.level.org.hibernate.SQL=DEBUG
4. テスト時にクエリ数を確認

