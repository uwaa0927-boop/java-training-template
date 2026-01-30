package com.example.weatherapp.repository;

import com.example.weatherapp.entity.DailyForecast;
import com.example.weatherapp.entity.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DailyForecast（日次予報）のRepositoryインターフェース
 */
@Repository
public interface DailyForecastRepository extends JpaRepository<DailyForecast, Long> {

    /**
     * 天気記録IDで検索（日付順）
     *
     * @param weatherRecordId 天気記録ID
     * @return 該当する日次予報のリスト（7日分）
     */
    List<DailyForecast> findByWeatherRecordIdOrderByForecastDateAsc(Long weatherRecordId);

    /**
     * 天気記録で検索
     *
     * @param weatherRecord 天気記録エンティティ
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByWeatherRecordOrderByForecastDateAsc(WeatherRecord weatherRecord);

    /**
     * 予報日で検索
     *
     * @param forecastDate 予報日
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByForecastDate(LocalDate forecastDate);

    /**
     * 予報日範囲で検索
     *
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByForecastDateBetweenOrderByForecastDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * 天気記録と予報日で検索
     *
     * @param weatherRecord 天気記録
     * @param forecastDate 予報日
     * @return 該当する日次予報（Optional）
     */
    Optional<DailyForecast> findByWeatherRecordAndForecastDate(
            WeatherRecord weatherRecord,
            LocalDate forecastDate
    );

    /**
     * 天気コードで検索
     *
     * @param weatherCode 天気コード
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByWeatherCode(Integer weatherCode);

    /**
     * 最高気温が閾値以上の予報を検索
     *
     * @param temperature 気温閾値
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByTemperatureMaxGreaterThanEqual(Double temperature);

    /**
     * 最低気温が閾値以下の予報を検索
     *
     * @param temperature 気温閾値
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByTemperatureMinLessThanEqual(Double temperature);

    /**
     * 降水量が閾値以上の予報を検索（雨の日）
     *
     * @param amount 降水量閾値（mm）
     * @return 該当する日次予報のリスト
     */
    List<DailyForecast> findByPrecipitationSumGreaterThan(Double amount);

    /**
     * 都道府県IDと予報日範囲で予報を取得
     * WeatherRecordを経由して検索
     *
     * @param prefectureId 都道府県ID
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 該当する日次予報のリスト
     */
    @Query("SELECT df FROM DailyForecast df " +
            "JOIN df.weatherRecord wr " +
            "WHERE wr.prefecture.id = :prefectureId " +
            "AND df.forecastDate BETWEEN :startDate AND :endDate " +
            "ORDER BY df.forecastDate ASC")
    List<DailyForecast> findByPrefectureAndDateRange(
            @Param("prefectureId") Long prefectureId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}