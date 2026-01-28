package com.example.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class WeatherDetailDto {
    // 都道府県情報
    private Long prefectureId;
    private String prefectureName;

    // 現在の天気
    private CurrentWeatherDto current;

    // 週間予報
    private List<DailyForecastDto> dailyForecasts;

    // メタ情報
    private LocalDateTime fetchedAt;
}
