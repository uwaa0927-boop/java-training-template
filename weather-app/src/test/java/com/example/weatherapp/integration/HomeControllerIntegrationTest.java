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
 * HomeControllerの統合テスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("トップページが表示される")
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("groupedPrefectures"))
                .andExpect(model().attributeExists("regions"));
    }

    @Test
    @DisplayName("地域フィルタが動作する")
    void testIndexWithRegionFilter() throws Exception {
        mockMvc.perform(get("/").param("region", "関東"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prefectures"))
                .andExpect(model().attribute("selectedRegion", "関東"))
                .andExpect(model().attribute("prefectures", hasSize(7)));
    }
}