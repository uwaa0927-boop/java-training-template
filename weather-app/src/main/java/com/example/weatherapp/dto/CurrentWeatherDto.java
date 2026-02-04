package com.example.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 現在の天気DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentWeatherDto {

    /** 観測時刻 */
    private LocalDateTime time;

    /** 気温（℃） */
    private Double temperature;

    /** 天気コード */
    private Integer weatherCode;

    /** 天気の説明（日本語） */
    private String weatherDescription;

    /** 天気アイコン */
    private String weatherIcon;

    /** 風速（m/s） */
    private Double windSpeed;

    /** 湿度（%） */
    private Integer humidity;

    /** 体感温度（℃） */
    private Double apparentTemperature;

    /** 降水量（mm） */
    private Double precipitation;

    /** 雲量（%） */
    private Integer cloudCover;
}
