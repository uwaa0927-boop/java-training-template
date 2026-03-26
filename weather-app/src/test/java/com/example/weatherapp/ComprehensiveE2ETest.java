package com.example.weatherapp;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import com.example.weatherapp.service.WeatherService;
import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.dto.CurrentWeatherDto;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/test-data.sql")
@DisplayName("総合E2Eテスト")
class ComprehensiveE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @BeforeEach
    void setup() {
        PrefectureDto prefecture = new PrefectureDto();
        prefecture.setName("東京都");

        CurrentWeatherDto current = new CurrentWeatherDto();
        current.setTemperature_2m(20.0);

        WeatherDetailDto mockWeather = new WeatherDetailDto();
        mockWeather.setPrefecture(prefecture);
        mockWeather.setCurrent(current);

        when(weatherService.getWeatherByPrefectureId(13L))
                .thenReturn(mockWeather);

        when(weatherService.getWeatherByPrefectureId(999L))
                .thenThrow(new ResourceNotFoundException("not found"));
    }

    @Test
    @DisplayName("シナリオ1: 基本的な利用フロー")
    void testBasicUserFlow() throws Exception {
        // 1. トップページアクセス
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("groupedPrefectures"));

        // 2. 地域フィルタ
        mockMvc.perform(get("/").param("region", "関東"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedRegion", "関東"))
                .andExpect(model().attributeExists("prefectures"));

        // 3. 天気詳細ページ
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(model().attributeExists("weather"))
                .andExpect(content().string(containsString("東京都")));
    }

    @Test
    @DisplayName("シナリオ2: エラーハンドリング")
    void testErrorHandling() throws Exception {
        // 存在しない都道府県
        mockMvc.perform(get("/weather/999"))
                .andExpect(status().isNotFound());

        // 存在しないページ
        mockMvc.perform(get("/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("シナリオ3: レスポンシブ対応")
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/")
                        .header("User-Agent", "Mobile Safari"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewport")));
    }
}