package com.example.weatherapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherRecordTest {

    @Test
    @DisplayName("WeatherRecordにDailyForecastを追加できる")
    void testAddDailyForecast() {
        // Given
        Prefecture prefecture = Prefecture.builder()
                .id(13L)
                .name("東京都")
                .build();

        WeatherRecord weatherRecord = WeatherRecord.builder()
                .prefecture(prefecture)
                .fetchedAt(LocalDateTime.now())
                .temperature(BigDecimal.valueOf(15.5))
                .weatherCode(1)
                .build();

        DailyForecast forecast = DailyForecast.builder()
                .forecastDate(LocalDate.now().plusDays(1))
                .temperatureMax(BigDecimal.valueOf(18.0))
                .temperatureMin(BigDecimal.valueOf(10.0))
                .weatherCode(2)
                .build();

        // When
        weatherRecord.addDailyForecast(forecast);

        // Then
        assertThat(weatherRecord.getDailyForecasts()).hasSize(1);
        assertThat(weatherRecord.getDailyForecasts().get(0)).isEqualTo(forecast);
        assertThat(forecast.getWeatherRecord()).isEqualTo(weatherRecord);  // 双方向関連が設定されている
    }

    @Test
    @DisplayName("複数のDailyForecastを追加できる")
    void testAddMultipleDailyForecasts() {
        // Given
        WeatherRecord weatherRecord = WeatherRecord.builder()
                .fetchedAt(LocalDateTime.now())
                .build();

        // When
        for (int i = 0; i < 7; i++) {
            DailyForecast forecast = DailyForecast.builder()
                    .forecastDate(LocalDate.now().plusDays(i))
                    .temperatureMax(BigDecimal.valueOf(15.0 + i))
                    .temperatureMin(BigDecimal.valueOf(8.0 + i))
                    .weatherCode(1)
                    .sunrise(LocalTime.of(6, 30))
                    .sunset(LocalTime.of(17, 30))
                    .build();

            weatherRecord.addDailyForecast(forecast);
        }

        // Then
        assertThat(weatherRecord.getDailyForecasts()).hasSize(7);
        assertThat(weatherRecord.getDailyForecasts().get(0).getTemperatureMax()).isEqualTo(15.0);
        assertThat(weatherRecord.getDailyForecasts().get(6).getTemperatureMax()).isEqualTo(21.0);
    }
}