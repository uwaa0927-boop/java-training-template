package com.example.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 天気詳細画面用DTO
 * 都道府県情報 + 現在の天気 + 週間予報をまとめたもの
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDetailDto {

    /** 都道府県情報 */
    private PrefectureDto prefecture;

    /** 現在の天気 */
    private CurrentWeatherDto current;

    /** 週間予報（7日分） */
    private List<DailyForecastDto> dailyForecasts;

    /** データ取得日時 */
    private LocalDateTime fetchedAt;
}
