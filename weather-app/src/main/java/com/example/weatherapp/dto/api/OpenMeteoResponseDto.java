package com.example.weatherapp.dto.api;

import com.example.weatherapp.dto.CurrentWeatherDto;
import com.example.weatherapp.dto.DailyWeatherDto;
import lombok.Data;

@Data
public class OpenMeteoResponseDto {
    private Double latitude;
    private Double longitude;
    private String timezone;
    private Double elevation;
    private CurrentWeatherDto current;
    private DailyWeatherDto daily;
}
