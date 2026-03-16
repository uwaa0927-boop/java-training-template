package com.example.weatherapp.integration;

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
 * アプリケーション全体の統合テスト
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Sql("/test-data.sql")
@DisplayName("アプリケーション統合テスト")
class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("トップページからデータ取得までの一連の流れ")
    void testCompleteUserFlow() throws Exception {
        // 1. トップページアクセス
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("groupedPrefectures"));

        // 2. 東京都の天気ページアクセス
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-detail"))
                .andExpect(model().attributeExists("weather"));

        // 3. 地域フィルタ
        mockMvc.perform(get("/").param("region", "関東"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedRegion", "関東"));
    }

    @Test
    @DisplayName("エラーハンドリングの動作確認")
    void testErrorHandling() throws Exception {
        // 存在しない都道府県
        mockMvc.perform(get("/weather/999"))
                .andExpect(status().is4xxClientError());

    }
}