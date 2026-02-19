package com.example.weatherapp.service.impl;

import com.example.weatherapp.client.OpenMeteoClient;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.dto.api.OpenMeteoResponseDto;
import com.example.weatherapp.entity.DailyForecast;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.WeatherRecord;
import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.mapper.WeatherMapper;
import com.example.weatherapp.repository.WeatherRecordRepository;
import com.example.weatherapp.service.PrefectureService;
import com.example.weatherapp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final OpenMeteoClient openMeteoClient;
    private final WeatherRecordRepository weatherRecordRepository;
    private final PrefectureService prefectureService;
    private final WeatherMapper weatherMapper;

    public WeatherServiceImpl(
            OpenMeteoClient openMeteoClient,
            WeatherRecordRepository weatherRecordRepository,
            PrefectureService prefectureService,
            WeatherMapper weatherMapper
    ) {
        this.openMeteoClient = openMeteoClient;
        this.weatherRecordRepository = weatherRecordRepository;
        this.prefectureService = prefectureService;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public WeatherDetailDto getWeatherByPrefectureId(Long prefectureId) {
        log.info("天気情報取得開始: prefectureId={}", prefectureId);

        Prefecture prefecture = prefectureService.findEntityById(prefectureId);
        log.debug("都道府県: {}", prefecture.getName());

        OpenMeteoResponseDto apiResponse = openMeteoClient.fetchWeather(
                prefecture.getLatitude().doubleValue(),
                prefecture.getLongitude().doubleValue()
        );

        WeatherRecord weatherRecord = buildWeatherRecord(apiResponse, prefecture);
        List<DailyForecast> dailyForecasts = buildDailyForecasts(apiResponse, weatherRecord);
        dailyForecasts.forEach(weatherRecord::addDailyForecast);

        WeatherRecord savedRecord = weatherRecordRepository.save(weatherRecord);
        log.info("天気情報保存完了: recordId={}", savedRecord.getId());

        return weatherMapper.toDetailDto(savedRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDetailDto getLatestWeatherFromDb(Long prefectureId) {
        return weatherRecordRepository
                .findLatestWithForecasts(prefectureId)
                .map(weatherMapper::toDetailDto)
                .orElse(null);
    }

    private WeatherRecord buildWeatherRecord(
            OpenMeteoResponseDto apiResponse,
            Prefecture prefecture
    ) {
        var current = apiResponse.getCurrent();

        BigDecimal temperature = current.getTemperature_2m() != null
                ? BigDecimal.valueOf(current.getTemperature_2m())
                : null;

        BigDecimal windSpeed = current.getWindspeed_10m() != null
                ? BigDecimal.valueOf(current.getWindspeed_10m())
                : null;

        BigDecimal apparentTemperature = current.getApparentTemperature() != null
                ? BigDecimal.valueOf(current.getApparentTemperature())
                : null;

        BigDecimal precipitation = current.getPrecipitation() != null
                ? BigDecimal.valueOf(current.getPrecipitation())
                : null;

        return WeatherRecord.builder()
                .prefecture(prefecture)
                .fetchedAt(LocalDateTime.now())
                .temperature(temperature)
                .weatherCode(current.getWeathercode())
                .windSpeed(windSpeed)
                .humidity(current.getRelativehumidity_2m())
                .apparentTemperature(apparentTemperature)
                .precipitation(precipitation)
                .cloudCover(current.getCloudCover())
                .build();
    }

    private List<DailyForecast> buildDailyForecasts(
            OpenMeteoResponseDto apiResponse,
            WeatherRecord weatherRecord
    ) {
        List<DailyForecast> forecasts = new ArrayList<>();
        var daily = apiResponse.getDaily();

        for (int i = 0; i < daily.getTime().size(); i++) {

            Double tempMax = getOrNull(daily.getTemperature2mMax(), i);
            Double tempMin = getOrNull(daily.getTemperature2mMin(), i);

            DailyForecast forecast = DailyForecast.builder()
                    .weatherRecord(weatherRecord)
                    .forecastDate(LocalDate.parse(daily.getTime().get(i)))
                    .temperatureMax(tempMax != null ? BigDecimal.valueOf(tempMax) : null)
                    .temperatureMin(tempMin != null ? BigDecimal.valueOf(tempMin) : null)
                    .weatherCode(daily.getWeathercode().get(i))
                    .precipitationSum(BigDecimal.valueOf(daily.getPrecipitationSum().get(i)))
                    .windSpeedMax(BigDecimal.valueOf(getOrNull(daily.getWindspeed10mMax(), i)))
                    .sunrise(parseTime(daily.getSunrise(), i))
                    .sunset(parseTime(daily.getSunset(), i))
                    .build();

            forecasts.add(forecast);
        }

        return forecasts;
    }

    private <T> T getOrNull(List<T> list, int index) {
        if (list == null || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    private LocalTime parseTime(List<String> timeList, int index) {
        String timeStr = getOrNull(timeList, index);
        if (timeStr == null) {
            return null;
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(timeStr);
            return dateTime.toLocalTime();
        } catch (Exception e) {
            log.warn("時刻パース失敗: {}", timeStr, e);
            return null;
        }
    }
}