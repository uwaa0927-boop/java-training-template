package com.example.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 日次予報DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyForecastDto {

    /** 予報日 */
    private LocalDate date;

    /** 曜日（月、火、...） */
    private String dayOfWeek;

    /** 最高気温（℃） */
    private Double temperatureMax;

    /** 最低気温（℃） */
    private Double temperatureMin;

    /** 天気コード */
    private Integer weatherCode;

    /** 天気の説明 */
    private String weatherDescription;

    /** 天気アイコン */
    private String weatherIcon;

    /** 降水量合計（mm） */
    private Double precipitationSum;

    /** 降水確率（%） ※計算で算出 */
    private Integer precipitationProbability;

    /** 最大風速（m/s） */
    private Double windSpeedMax;

    /** 日の出時刻 */
    private LocalTime sunrise;

    /** 日の入り時刻 */
    private LocalTime sunset;
}
