package com.example.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 天気記録Entity
 * weather_records テーブルとマッピング
 */
@Entity
@Table(name = "weather_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRecord {

    /**
     * 天気記録ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 都道府県（多対1）
     * 外部キー: prefecture_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prefecture_id", nullable = false)
    private Prefecture prefecture;

    /**
     * API取得日時
     * この時点での天気情報
     */
    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    /**
     * 気温（℃）
     * 例: 15.5
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    /**
     * 天気コード（WMO Weather Code）
     * 例: 0=快晴, 1=晴れ, 61=雨
     */
    @Column(name = "weather_code")
    private Integer weatherCode;

    /**
     * 風速（m/s）
     * 例: 3.2
     */
    @Column(name = "wind_speed", precision = 5, scale = 2)
    private BigDecimal windSpeed;

    /**
     * 湿度（%）
     * 例: 65
     */
    private Integer humidity;

    /**
     * 体感温度（℃）
     * 例: 13.8
     */
    @Column(name = "apparent_temperature", precision = 5, scale = 2)
    private BigDecimal apparentTemperature;

    /**
     * 降水量（mm）
     * 例: 0.0
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal precipitation;

    /**
     * 雲量（%）
     * 例: 25
     */
    @Column(name = "cloud_cover")
    private Integer cloudCover;

    /**
     * 作成日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 日次予報リスト（1対多）
     * このWeatherRecordに紐づく7日分の予報
     */
    @OneToMany(
            mappedBy = "weatherRecord",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DailyForecast> dailyForecasts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * 日次予報を追加するヘルパーメソッド
     * 双方向の関連を正しく設定
     */
    public void addDailyForecast(DailyForecast forecast) {
        dailyForecasts.add(forecast);
        forecast.setWeatherRecord(this);
    }
}