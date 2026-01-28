package com.example.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CurrentWeatherDto {
    private LocalDateTime time;
    private Double temperature;
    private Integer weatherCode;
    private String weatherDescription;  // 天気コードから生成
    private String weatherIcon;         // 天気コードから生成
    private Double windSpeed;
    private Integer humidity;
    private Double apparentTemperature;
}
