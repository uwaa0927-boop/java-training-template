package com.example.weatherapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * 天気詳細ページのE2Eテスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
class WeatherDetailPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("天気詳細ページが表示される")
    void testWeatherDetailPage() throws Exception {
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(content().string(containsString("東京都の天気")))
                .andExpect(content().string(containsString("現在の天気")))
                .andExpect(content().string(containsString("7日間の天気予報")));
    }

    @Test
    @DisplayName("現在の天気情報が表示される")
    void testCurrentWeatherDisplay() throws Exception {
        mockMvc.perform(get("/weather/13"))
                .andExpect(content().string(containsString("体感温度")))
                .andExpect(content().string(containsString("湿度")))
                .andExpect(content().string(containsString("風速")));
    }
}