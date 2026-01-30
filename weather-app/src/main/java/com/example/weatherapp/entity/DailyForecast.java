package com.example.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日次予報Entity
 * daily_forecasts テーブルとマッピング
 */
@Entity
@Table(name = "daily_forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyForecast {

    /**
     * 日次予報ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 天気記録（多対1）
     * 外部キー: weather_record_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_record_id", nullable = false)
    @ToString.Exclude  // toString()で無限ループを防ぐ
    private WeatherRecord weatherRecord;

    /**
     * 予報日
     * 例: 2024-01-05
     */
    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    /**
     * 最高気温（℃）
     * 例: 15.2
     */
    @Column(name = "temperature_max", precision = 5, scale = 2)
    private BigDecimal temperatureMax;

    /**
     * 最低気温（℃）
     * 例: 8.1
     */
    @Column(name = "temperature_min", precision = 5, scale = 2)
    private BigDecimal temperatureMin;

    /**
     * 天気コード
     */
    @Column(name = "weather_code")
    private Integer weatherCode;

    /**
     * 降水量合計（mm）
     * 例: 5.2
     */
    @Column(name = "precipitation_sum", precision = 5, scale = 2)
    private BigDecimal precipitationSum;

    /**
     * 最大風速（m/s）
     * 例: 8.1
     */
    @Column(name = "wind_speed_max", precision = 5, scale = 2)
    private BigDecimal windSpeedMax;

    /**
     * 日の出時刻
     * 例: 06:51
     */
    private LocalTime sunrise;

    /**
     * 日の入り時刻
     * 例: 16:46
     */
    private LocalTime sunset;

    /**
     * 作成日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}