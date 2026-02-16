package com.example.weatherapp.controller;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.service.PrefectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * HomeControllerのテスト
 *
 * @WebMvcTest: Controller層のみをテスト
 * MockMvcを使ってHTTPリクエストをシミュレート
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrefectureService prefectureService;

    private PrefectureDto tokyo;
    private PrefectureDto osaka;
    private Map<String, List<PrefectureDto>> groupedPrefectures;

    @BeforeEach
    void setUp() {
        // テストデータ準備
        tokyo = PrefectureDto.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .region("関東")
                .build();

        osaka = PrefectureDto.builder()
                .id(27L)
                .name("大阪府")
                .nameEn("Osaka")
                .region("関西")
                .build();

        // 地域別グループ化データ
        groupedPrefectures = new LinkedHashMap<>();
        groupedPrefectures.put("関東", List.of(tokyo));
        groupedPrefectures.put("関西", List.of(osaka));
    }

    @Test
    @DisplayName("トップページが正しく表示される")
    void testIndex_NoFilter() throws Exception {
        // Given
        when(prefectureService.findAllGroupedByRegion())
                .thenReturn(groupedPrefectures);

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("groupedPrefectures"))
                .andExpect(model().attributeExists("regions"))
                .andExpect(model().attribute("groupedPrefectures", groupedPrefectures));

        verify(prefectureService, times(1)).findAllGroupedByRegion();
    }

    @Test
    @DisplayName("地域フィルタが正しく動作する")
    void testIndex_WithRegionFilter() throws Exception {
        // Given
        List<PrefectureDto> kantoPrefectures = List.of(tokyo);
        when(prefectureService.findByRegion("関東"))
                .thenReturn(kantoPrefectures);

        // When & Then
        mockMvc.perform(get("/")
                        .param("region", "関東"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("prefectures"))
                .andExpect(model().attribute("selectedRegion", "関東"))
                .andExpect(model().attribute("prefectures", hasSize(1)))
                .andExpect(model().attribute("prefectures", hasItem(
                        hasProperty("name", is("東京都"))
                )));

        verify(prefectureService, times(1)).findByRegion("関東");
        verify(prefectureService, never()).findAllGroupedByRegion();
    }

    @Test
    @DisplayName("空文字列の地域フィルタは無視される")
    void testIndex_WithEmptyRegion() throws Exception {
        // Given
        when(prefectureService.findAllGroupedByRegion())
                .thenReturn(groupedPrefectures);

        // When & Then
        mockMvc.perform(get("/")
                        .param("region", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groupedPrefectures"))
                .andExpect(model().attributeDoesNotExist("selectedRegion"));

        verify(prefectureService, times(1)).findAllGroupedByRegion();
    }

    @Test
    @DisplayName("ヘルスチェックエンドポイントが正しく動作する")
    void testHealth() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(view().name("health"))
                .andExpect(model().attribute("status", "OK"))
                .andExpect(model().attribute("message", "Application is running"));
    }
}