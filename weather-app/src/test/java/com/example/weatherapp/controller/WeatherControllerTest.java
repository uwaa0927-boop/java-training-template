package com.example.weatherapp.controller;

import com.example.weatherapp.dto.CurrentWeatherDto;
import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * WeatherControllerのテスト
 */
@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private WeatherDetailDto weatherDetail;

    @BeforeEach
    void setUp() {
        // テストデータ準備
        PrefectureDto prefecture = PrefectureDto.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .region("関東")
                .build();

        CurrentWeatherDto current = CurrentWeatherDto.builder()
                .time(LocalDateTime.now())
                .temperature(15.5)
                .weatherCode(1)
                .weatherDescription("晴れ")
                .build();

        weatherDetail = WeatherDetailDto.builder()
                .prefecture(prefecture)
                .current(current)
                .dailyForecasts(new ArrayList<>())
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("天気詳細ページが正しく表示される")
    void testShowWeather_Success() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(13L))
                .thenReturn(weatherDetail);

        // When & Then
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(model().attributeExists("weather"))
                .andExpect(model().attribute("weather", weatherDetail))
                .andExpect(model().attribute("weather",
                        hasProperty("prefecture",
                                hasProperty("name", is("東京都"))
                        )
                ));

        verify(weatherService, times(1)).getWeatherByPrefectureId(13L);
    }

    @Test
    @DisplayName("存在しない都道府県で404エラーが表示される")
    void testShowWeather_NotFound() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(999L))
                .thenThrow(new ResourceNotFoundException("都道府県が見つかりません"));

        // When & Then
        mockMvc.perform(get("/weather/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage",
                        containsString("見つかりません")));

        verify(weatherService, times(1)).getWeatherByPrefectureId(999L);
    }

    @Test
    @DisplayName("API呼び出し失敗時にエラーページが表示される")
    void testShowWeather_ApiError() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(13L))
                .thenThrow(new ExternalApiException("API呼び出し失敗"));

        // When & Then
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/api-error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("prefectureId", 13L));

        verify(weatherService, times(1)).getWeatherByPrefectureId(13L);
    }

    @Test
    @DisplayName("予期しないエラーで500エラーが表示される")
    void testShowWeather_UnexpectedError() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(13L))
                .thenThrow(new RuntimeException("予期しないエラー"));

        // When & Then
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(weatherService, times(1)).getWeatherByPrefectureId(13L);
    }

    @Test
    @DisplayName("DB保存済みの最新天気を表示できる")
    void testShowLatestWeather_Success() throws Exception {
        // Given
        when(weatherService.getLatestWeatherFromDb(13L))
                .thenReturn(weatherDetail);

        // When & Then
        mockMvc.perform(get("/weather/13/latest"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(model().attributeExists("weather"));

        verify(weatherService, times(1)).getLatestWeatherFromDb(13L);
    }

    @Test
    @DisplayName("DBに天気データがない場合404エラー")
    void testShowLatestWeather_NoData() throws Exception {
        // Given
        when(weatherService.getLatestWeatherFromDb(13L))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(get("/weather/13/latest"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage",
                        containsString("まだ取得されていません")));

        verify(weatherService, times(1)).getLatestWeatherFromDb(13L);
    }
}