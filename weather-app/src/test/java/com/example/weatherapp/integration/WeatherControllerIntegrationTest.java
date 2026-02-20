package com.example.weatherapp.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * WeatherControllerの統合テスト
 * 実際のAPIを呼び出すため時間がかかる
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("天気詳細ページが表示される")
    void testWeatherDetailPage() throws Exception {
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(model().attributeExists("weather"))
                .andExpect(model().attribute("weather",
                        hasProperty("prefecture",
                                hasProperty("name", is("東京都"))
                        )
                ));
    }

    @Test
    @DisplayName("存在しない都道府県で404エラー")
    void testWeatherDetail_NotFound() throws Exception {
        mockMvc.perform(get("/weather/999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }
}