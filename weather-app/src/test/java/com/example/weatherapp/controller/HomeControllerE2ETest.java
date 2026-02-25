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
 * トップページのE2Eテスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
class HomeControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("トップページが正しく表示される")
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("全国天気予報")))
                .andExpect(content().string(containsString("北海道")))
                .andExpect(content().string(containsString("東京都")))
                .andExpect(content().string(containsString("沖縄県")));
    }

    @Test
    @DisplayName("地域フィルタが動作する")
    void testRegionFilter() throws Exception {
        mockMvc.perform(get("/").param("region", "関東"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedRegion", "関東"))
                .andExpect(content().string(containsString("東京都")))
                .andExpect(content().string(containsString("神奈川県")));
    }
}