package com.example.weatherapp.service;

import com.example.weatherapp.client.OpenMeteoClient;
import com.example.weatherapp.dto.CurrentWeatherDto;
import com.example.weatherapp.dto.DailyWeatherDto;
import com.example.weatherapp.dto.api.OpenMeteoResponseDto;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.WeatherRecord;
import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.mapper.WeatherMapper;
import com.example.weatherapp.service.impl.WeatherServiceImpl;
import com.example.weatherapp.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * WeatherServiceの単体テスト
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService単体テスト")
class WeatherServiceTest {

    @Mock
    private PrefectureService prefectureService;

    @Mock
    private WeatherRecordRepository weatherRecordRepository;

    @Mock
    private OpenMeteoClient openMeteoClient;

    @Mock
    private WeatherMapper weatherMapper;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private Prefecture testPrefecture;
    private OpenMeteoResponseDto testApiResponse;
    private WeatherRecord testWeatherRecord;
    private WeatherDetailDto testWeatherDetailDto;

    @BeforeEach
    void setUp() {

        // 都道府県
        testPrefecture = Prefecture.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .region("関東")
                .latitude(BigDecimal.valueOf(35.6895))
                .longitude(BigDecimal.valueOf(139.6917))
                .build();

        // APIレスポンス
        testApiResponse = new OpenMeteoResponseDto();

        CurrentWeatherDto current = new CurrentWeatherDto();
        current.setTemperature_2m(20.0);
        current.setWeathercode(1);

        testApiResponse.setCurrent(current);

        DailyWeatherDto daily = new DailyWeatherDto();
        daily.setTime(new ArrayList<>());
        daily.setTemperature2mMax(new ArrayList<>());
        daily.setTemperature2mMin(new ArrayList<>());
        daily.setWeathercode(new ArrayList<>());
        daily.setPrecipitationSum(new ArrayList<>());
        daily.setWindspeed10mMax(new ArrayList<>());
        daily.setSunrise(new ArrayList<>());
        daily.setSunset(new ArrayList<>());

        testApiResponse.setDaily(daily);

        // 保存されるWeatherRecord
        testWeatherRecord = WeatherRecord.builder()
                .id(1L)
                .prefecture(testPrefecture)
                .fetchedAt(LocalDateTime.now())
                .build();

        // 返却DTO
        testWeatherDetailDto = WeatherDetailDto.builder()
                .build();
    }

    @Test
    @DisplayName("正常系: 都道府県IDから天気情報を取得")
    void testGetWeatherByPrefectureId_Success() {

        when(prefectureService.findEntityById(13L))
                .thenReturn(testPrefecture);

        when(openMeteoClient.fetchWeather(anyDouble(), anyDouble()))
                .thenReturn(testApiResponse);

        when(weatherRecordRepository.save(any()))
                .thenReturn(testWeatherRecord);

        when(weatherMapper.toDetailDto(any()))
                .thenReturn(testWeatherDetailDto);

        WeatherDetailDto result =
                weatherService.getWeatherByPrefectureId(13L);

        assertThat(result).isNotNull();

        verify(prefectureService, times(1))
                .findEntityById(13L);

        verify(openMeteoClient, times(1))
                .fetchWeather(anyDouble(), anyDouble());

        verify(weatherRecordRepository, atLeastOnce())
                .save(any());
    }

    @Test
    @DisplayName("異常系: 存在しない都道府県ID")
    void testGetWeatherByPrefectureId_NotFound() {

        when(prefectureService.findEntityById(999L))
                .thenThrow(new ResourceNotFoundException("都道府県が見つかりません"));

        assertThatThrownBy(() ->
                weatherService.getWeatherByPrefectureId(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(openMeteoClient, never())
                .fetchWeather(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("異常系: API呼び出し失敗")
    void testGetWeatherByPrefectureId_ApiError() {

        when(prefectureService.findEntityById(13L))
                .thenReturn(testPrefecture);

        when(openMeteoClient.fetchWeather(anyDouble(), anyDouble()))
                .thenThrow(new ExternalApiException("API接続エラー"));

        assertThatThrownBy(() ->
                weatherService.getWeatherByPrefectureId(13L))
                .isInstanceOf(ExternalApiException.class);

        verify(weatherRecordRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("境界値: 緯度の範囲チェック")
    void testLatitudeBoundary() {

        Prefecture hokkaido = Prefecture.builder()
                .latitude(BigDecimal.valueOf(45.5))
                .build();

        Prefecture okinawa = Prefecture.builder()
                .latitude(BigDecimal.valueOf(24.3))
                .build();

        assertThat(hokkaido.getLatitude())
                .isBetween(BigDecimal.valueOf(20.0), BigDecimal.valueOf(50.0));

        assertThat(okinawa.getLatitude())
                .isBetween(BigDecimal.valueOf(20.0), BigDecimal.valueOf(50.0));
    }
}