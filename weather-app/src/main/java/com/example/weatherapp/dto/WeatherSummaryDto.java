package com.example.weatherapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WeatherSummaryDto {

    private Long id;
    private String prefectureName;
    private BigDecimal currentTemperature;
    private LocalDateTime fetchedAt;

    public WeatherSummaryDto(Long id, String prefectureName, BigDecimal currentTemperature, LocalDateTime fetchedAt) {
        this.id = id;
        this.prefectureName = prefectureName;
        this.currentTemperature = currentTemperature;
        this.fetchedAt = fetchedAt;
    }

    public Long getId() {
        return id;
    }

    public String getPrefectureName() {
        return prefectureName;
    }

    public BigDecimal getCurrentTemperature() {
        return currentTemperature;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }
}