package com.example.weatherapp.service;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * PrefectureServiceの統合テスト
 * 実際のデータベースを使用
 */
@SpringBootTest
@Transactional
@Sql("/test-data.sql")
class PrefectureServiceIntegrationTest {

    @Autowired
    private PrefectureService prefectureService;

    @Test
    @DisplayName("全都道府県を取得できる（統合テスト）")
    void testFindAll_Integration() {
        // When
        List<PrefectureDto> result = prefectureService.findAll();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSizeGreaterThanOrEqualTo(11);
    }

    @Test
    @DisplayName("東京都を取得できる（統合テスト）")
    void testFindById_Tokyo() {
        // When
        PrefectureDto tokyo = prefectureService.findById(13L);

        // Then
        assertThat(tokyo).isNotNull();
        assertThat(tokyo.getName()).isEqualTo("東京都");
        assertThat(tokyo.getNameEn()).isEqualTo("Tokyo");
        assertThat(tokyo.getRegion()).isEqualTo("関東");
    }

    @Test
    @DisplayName("関東地方の都道府県を取得できる")
    void testFindByRegion_Kanto() {
        // When
        List<PrefectureDto> kanto = prefectureService.findByRegion("関東");

        // Then
        assertThat(kanto).hasSize(7);
        assertThat(kanto).extracting(PrefectureDto::getRegion)
                .containsOnly("関東");
    }

    @Test
    @DisplayName("地域別グループ化が正しく動作する")
    void testFindAllGroupedByRegion_Integration() {
        // When
        Map<String, List<PrefectureDto>> grouped =
                prefectureService.findAllGroupedByRegion();

        // Then
        assertThat(grouped).isNotEmpty();
        assertThat(grouped.get("関東")).hasSizeGreaterThanOrEqualTo(7);
        assertThat(grouped.get("関西")).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("存在しないIDで例外が発生する（統合テスト）")
    void testFindById_NotFound_Integration() {
        // When & Then
        assertThatThrownBy(() -> prefectureService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}