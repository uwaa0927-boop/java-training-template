package com.example.weatherapp.mapper;

import com.example.weatherapp.dto.CurrentWeatherDto;
import com.example.weatherapp.dto.DailyForecastDto;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.entity.DailyForecast;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.WeatherRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherMapperTest {

    private WeatherMapper weatherMapper;
    private PrefectureMapper prefectureMapper;

    @BeforeEach
    void setUp() {
        prefectureMapper = new PrefectureMapper();
        weatherMapper = new WeatherMapper(prefectureMapper);
    }

    @Test
    @DisplayName("WeatherRecordをCurrentWeatherDtoに変換できる")
    void testToCurrentDto() {
        // Given
        WeatherRecord record = WeatherRecord.builder()
                .fetchedAt(LocalDateTime.now())
                .temperature(BigDecimal.valueOf(15.5))
                .weatherCode(1)
                .windSpeed(BigDecimal.valueOf(3.2))
                .humidity(65)
                .apparentTemperature(BigDecimal.valueOf(13.8))
                .precipitation(BigDecimal.valueOf(0.0))
                .cloudCover(25)
                .build();

        // When
        CurrentWeatherDto dto = weatherMapper.toCurrentDto(record);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getTemperature_2m()).isEqualTo(15.5);
        assertThat(dto.getWeathercode()).isEqualTo(1);
        assertThat(dto.getWeatherDescription()).isEqualTo("晴れ");
        assertThat(dto.getWeatherIcon()).isEqualTo("🌤️");
    }

    @Test
    @DisplayName("DailyForecastをDailyForecastDtoに変換できる")
    void testToDailyDto() {
        // Given
        DailyForecast forecast = DailyForecast.builder()
                .forecastDate(LocalDate.of(2024, 1, 5))
                .temperatureMax(BigDecimal.valueOf(15.2))
                .temperatureMin(BigDecimal.valueOf(8.1))
                .weatherCode(61)
                .precipitationSum(BigDecimal.valueOf(5.2))
                .windSpeedMax(BigDecimal.valueOf(8.1))
                .sunrise(LocalTime.of(6, 51))
                .sunset(LocalTime.of(16, 46))
                .build();

        // When
        DailyForecastDto dto = weatherMapper.toDailyDto(forecast);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getDate()).isEqualTo(LocalDate.of(2024, 1, 5));
        assertThat(dto.getDayOfWeek()).isEqualTo("金");
        assertThat(dto.getWeatherDescription()).isEqualTo("小雨");
        assertThat(dto.getWeatherIcon()).isEqualTo("🌧️");
        assertThat(dto.getPrecipitationProbability()).isEqualTo(70);
    }

    @Test
    @DisplayName("WeatherRecordを完全なWeatherDetailDtoに変換できる")
    void testToDetailDto() {
        // Given
        Prefecture prefecture = Prefecture.builder()
                .id(13L)
                .name("東京都")
                .build();

        WeatherRecord record = WeatherRecord.builder()
                .prefecture(prefecture)
                .fetchedAt(LocalDateTime.now())
                .temperature(BigDecimal.valueOf(15.5))
                .weatherCode(1)
                .build();

        List<DailyForecast> forecasts = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            forecasts.add(DailyForecast.builder()
                    .weatherRecord(record)
                    .forecastDate(LocalDate.now().plusDays(i))
                    .temperatureMax(BigDecimal.valueOf(15.0 + i))
                    .temperatureMin(BigDecimal.valueOf(8.0 + i))
                    .weatherCode(1)
                    .build());
        }
        record.getDailyForecasts().addAll(forecasts);

        // When
        WeatherDetailDto dto = weatherMapper.toDetailDto(record);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getPrefecture()).isNotNull();
        assertThat(dto.getPrefecture().getName()).isEqualTo("東京都");
        assertThat(dto.getCurrent()).isNotNull();
        assertThat(dto.getDailyForecasts()).hasSize(7);
    }
}
