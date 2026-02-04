package com.example.weatherapp.mapper;

import com.example.weatherapp.dto.CurrentWeatherDto;
import com.example.weatherapp.dto.DailyForecastDto;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.entity.DailyForecast;
import com.example.weatherapp.entity.WeatherRecord;
import com.example.weatherapp.util.WeatherCodeMapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * WeatherRecord/DailyForecast ↔ DTO 変換
 */
@Component
public class WeatherMapper {

    private final PrefectureMapper prefectureMapper;

    public WeatherMapper(PrefectureMapper prefectureMapper) {
        this.prefectureMapper = prefectureMapper;
    }

    /**
     * WeatherRecord → CurrentWeatherDto
     */
    public CurrentWeatherDto toCurrentDto(WeatherRecord record) {
        if (record == null) {
            return null;
        }

        return CurrentWeatherDto.builder()
                .time(record.getFetchedAt())
                .temperature(record.getTemperature()!= null ? record.getTemperature().doubleValue() : null)
                .weatherCode(record.getWeatherCode())
                .weatherDescription(WeatherCodeMapper.getDescription(record.getWeatherCode()))
                .weatherIcon(WeatherCodeMapper.getIcon(record.getWeatherCode()))
                .windSpeed(record.getWindSpeed()!= null ? record.getWindSpeed().doubleValue() : null)
                .humidity(record.getHumidity())
                .apparentTemperature(record.getApparentTemperature()!= null ? record.getApparentTemperature().doubleValue() : null)
                .precipitation(record.getPrecipitation()!= null ? record.getPrecipitation().doubleValue() : null)
                .cloudCover(record.getCloudCover())
                .build();
    }

    /**
     * DailyForecast → DailyForecastDto
     */
    public DailyForecastDto toDailyDto(DailyForecast forecast) {
        if (forecast == null) {
            return null;
        }

        // 曜日を日本語で取得
        String dayOfWeek = forecast.getForecastDate()
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.JAPANESE);

        // 降水確率を計算（降水量から推定）
        Integer precipProb = calculatePrecipitationProbability(
                forecast.getPrecipitationSum()!= null ? forecast.getPrecipitationSum().doubleValue() : null
        );

        return DailyForecastDto.builder()
                .date(forecast.getForecastDate())
                .dayOfWeek(dayOfWeek)
                .temperatureMax(forecast.getTemperatureMax()!= null ? forecast.getTemperatureMax().doubleValue() : null)
                .temperatureMin(forecast.getTemperatureMin()!= null ? forecast.getTemperatureMin().doubleValue() : null)
                .weatherCode(forecast.getWeatherCode())
                .weatherDescription(WeatherCodeMapper.getDescription(forecast.getWeatherCode()))
                .weatherIcon(WeatherCodeMapper.getIcon(forecast.getWeatherCode()))
                .precipitationSum(forecast.getPrecipitationSum()!= null ? forecast.getPrecipitationSum().doubleValue() : null)
                .precipitationProbability(precipProb)
                .windSpeedMax(forecast.getWindSpeedMax()!= null ? forecast.getWindSpeedMax().doubleValue() : null)
                .sunrise(forecast.getSunrise())
                .sunset(forecast.getSunset())
                .build();
    }

    /**
     * DailyForecast List → DailyForecastDto List
     */
    public List<DailyForecastDto> toDailyDtoList(List<DailyForecast> forecasts) {
        return forecasts.stream()
                .map(this::toDailyDto)
                .collect(Collectors.toList());
    }

    /**
     * WeatherRecord → WeatherDetailDto（完全版）
     */
    public WeatherDetailDto toDetailDto(WeatherRecord record) {
        if (record == null) {
            return null;
        }

        return WeatherDetailDto.builder()
                .prefecture(prefectureMapper.toDto(record.getPrefecture()))
                .current(toCurrentDto(record))
                .dailyForecasts(toDailyDtoList(record.getDailyForecasts()))
                .fetchedAt(record.getFetchedAt())
                .build();
    }

    /**
     * 降水量から降水確率を推定（簡易計算）
     */
    private Integer calculatePrecipitationProbability(Double precipSum) {
        if (precipSum == null || precipSum <= 0) {
            return 0;
        } else if (precipSum < 1.0) {
            return 20;
        } else if (precipSum < 5.0) {
            return 50;
        } else if (precipSum < 10.0) {
            return 70;
        } else {
            return 90;
        }
    }
}