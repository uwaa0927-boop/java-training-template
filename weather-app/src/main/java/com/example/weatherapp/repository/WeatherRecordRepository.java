package com.example.weatherapp.repository;

import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.WeatherRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * WeatherRecord（天気記録）のRepositoryインターフェース
 */
@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    /**
     * 都道府県IDで検索（最新順）
     *
     * @param prefectureId 都道府県ID
     * @return 該当する天気記録のリスト
     */
    List<WeatherRecord> findByPrefectureIdOrderByFetchedAtDesc(Long prefectureId);

    /**
     * 都道府県で検索（最新順、上位N件）
     *
     * @param prefecture 都道府県エンティティ
     * @param limit 取得件数
     * @return 該当する天気記録のリスト
     */
    List<WeatherRecord> findTop10ByPrefectureOrderByFetchedAtDesc(Prefecture prefecture);

    /**
     * 都道府県の最新の天気記録を1件取得
     *
     * @param prefecture 都道府県エンティティ
     * @return 最新の天気記録（Optional）
     */
    Optional<WeatherRecord> findFirstByPrefectureOrderByFetchedAtDesc(Prefecture prefecture);

    /**
     * 日時範囲で検索
     *
     * @param start 開始日時
     * @param end 終了日時
     * @return 該当する天気記録のリスト
     */
    List<WeatherRecord> findByFetchedAtBetweenOrderByFetchedAtDesc(
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * 都道府県と日時範囲で検索
     *
     * @param prefecture 都道府県
     * @param start 開始日時
     * @param end 終了日時
     * @return 該当する天気記録のリスト
     */
    List<WeatherRecord> findByPrefectureAndFetchedAtBetween(
            Prefecture prefecture,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * 天気コードで検索
     *
     * @param weatherCode 天気コード
     * @param pageable ページング情報
     * @return 該当する天気記録のページ
     */
    Page<WeatherRecord> findByWeatherCode(Integer weatherCode, Pageable pageable);

    /**
     * 気温範囲で検索
     *
     * @param minTemp 最低気温
     * @param maxTemp 最高気温
     * @return 該当する天気記録のリスト
     */
    List<WeatherRecord> findByTemperatureBetween(Double minTemp, Double maxTemp);

    /**
     * DailyForecastsをEAGER fetchで取得
     * N+1問題を回避
     *
     * @param id 天気記録ID
     * @return 天気記録（DailyForecasts込み）
     */
    @Query("SELECT w FROM WeatherRecord w " +
            "LEFT JOIN FETCH w.dailyForecasts " +
            "WHERE w.id = :id")
    Optional<WeatherRecord> findByIdWithForecasts(@Param("id") Long id);

    /**
     * 都道府県の最新記録をDailyForecasts込みで取得
     *
     * @param prefectureId 都道府県ID
     * @return 最新の天気記録（DailyForecasts込み）
     */
    @Query("SELECT w FROM WeatherRecord w " +
            "LEFT JOIN FETCH w.dailyForecasts " +
            "WHERE w.prefecture.id = :prefectureId " +
            "ORDER BY w.fetchedAt DESC " +
            "LIMIT 1")
    Optional<WeatherRecord> findLatestWithForecasts(@Param("prefectureId") Long prefectureId);

    /**
     * 古い記録を削除（データ保持期間管理用）
     *
     * @param before この日時より前の記録を削除
     */
    void deleteByFetchedAtBefore(LocalDateTime before);

    /**
     * 都道府県の記録件数をカウント
     *
     * @param prefecture 都道府県
     * @return 記録件数
     */
    long countByPrefecture(Prefecture prefecture);
}