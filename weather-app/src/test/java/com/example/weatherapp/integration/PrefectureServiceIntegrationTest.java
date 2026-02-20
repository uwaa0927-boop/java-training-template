package com.example.weatherapp.integration;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.service.PrefectureService;
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
 * 実際のDBを使用してService層をテスト
 */
@SpringBootTest
@Transactional
@Sql("/test-data.sql")  // テストデータ投入
class PrefectureServiceIntegrationTest {

    @Autowired
    private PrefectureService prefectureService;

    @Test
    @DisplayName("全都道府県を取得できる")
    void testFindAll() {
        // When
        List<PrefectureDto> prefectures = prefectureService.findAll();

        // Then
        assertThat(prefectures).isNotNull();
        assertThat(prefectures).hasSize(47);
        assertThat(prefectures).extracting("name")
                .contains("北海道", "東京都", "大阪府", "沖縄県");
    }

    @Test
    @DisplayName("IDで都道府県を取得できる")
    void testFindById() {
        // When
        PrefectureDto tokyo = prefectureService.findById(13L);

        // Then
        assertThat(tokyo).isNotNull();
        assertThat(tokyo.getName()).isEqualTo("東京都");
        assertThat(tokyo.getNameEn()).isEqualTo("Tokyo");
        assertThat(tokyo.getRegion()).isEqualTo("関東");
        assertThat(tokyo.getLatitude()).isCloseTo(35.689, within(0.001));
        assertThat(tokyo.getLongitude()).isCloseTo(139.692, within(0.001));
    }

    @Test
    @DisplayName("存在しないIDで例外が発生する")
    void testFindById_NotFound() {
        // When & Then
        assertThatThrownBy(() -> prefectureService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("都道府県が見つかりません");
    }

    @Test
    @DisplayName("地域別に都道府県を取得できる")
    void testFindByRegion() {
        // When
        List<PrefectureDto> kanto = prefectureService.findByRegion("関東");

        // Then
        assertThat(kanto).isNotNull();
        assertThat(kanto).hasSize(7);  // 東京、神奈川、埼玉、千葉、茨城、栃木、群馬
        assertThat(kanto).extracting("name")
                .contains("東京都", "神奈川県", "埼玉県");
    }

    @Test
    @DisplayName("地域別グループ化が正しく動作する")
    void testFindAllGroupedByRegion() {
        // When
        Map<String, List<PrefectureDto>> grouped =
                prefectureService.findAllGroupedByRegion();

        // Then
        assertThat(grouped).isNotNull();
        assertThat(grouped).hasSize(9);  // 9地域

        assertThat(grouped).containsKeys(
                "北海道", "東北", "関東", "中部",
                "関西", "中国", "四国", "九州", "沖縄"
        );

        // 関東地方の検証
        List<PrefectureDto> kanto = grouped.get("関東");
        assertThat(kanto).hasSize(7);
        assertThat(kanto).extracting("region")
                .containsOnly("関東");
    }

    @Test
    @DisplayName("名前で都道府県を検索できる")
    void testFindByName() {
        // When
        PrefectureDto result = prefectureService.findByName("大阪府");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(27L);
        assertThat(result.getNameEn()).isEqualTo("Osaka");
        assertThat(result.getRegion()).isEqualTo("関西");
    }
}