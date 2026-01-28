package com.example.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DailyForecastDto {
    private LocalDate date;
    private Double temperatureMax;
    private Double temperatureMin;
    private Integer weatherCode;
    private String weatherDescription;
    private String weatherIcon;
    private Double precipitationSum;
}
