package com.example.weatherapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DailyWeatherDto {
    private List<String> time;
    private List<Double> temperature2mMax;
    private List<Double> temperature2mMin;
    private List<Integer> weathercode;
    private List<Double> precipitationSum;
    private List<Double> windspeed10mMax;
    private List<String> sunrise;
    private List<String> sunset;
}