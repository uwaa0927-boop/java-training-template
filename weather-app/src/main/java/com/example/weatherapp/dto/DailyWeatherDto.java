package com.example.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DailyWeatherDto {
    private List<String> time;

    @JsonProperty("temperature_2m_max")
    private List<Double> temperature2mMax;

    @JsonProperty("temperature_2m_min")
    private List<Double> temperature2mMin;

    private List<Integer> weathercode;

    @JsonProperty("precipitation_sum")
    private List<Double> precipitationSum;

    @JsonProperty("windspeed_10m_max")
    private List<Double> windspeed10mMax;

    private List<String> sunrise;

    private List<String> sunset;
}