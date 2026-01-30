package com.example.weatherapp.repository;

import com.example.weatherapp.entity.Prefecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PrefectureRepositoryの統合テスト
 *
 * @DataJpaTest: JPA関連のみをロードする軽量テスト
 */
@DataJpaTest
@Sql("/test-data.sql")  // テストデータ投入
class PrefectureRepositoryTest {

    @Autowired
    private PrefectureRepository prefectureRepository;

    @Test
    @DisplayName("全件取得できる")
    void testFindAll() {
        // When
        List<Prefecture> prefectures = prefectureRepository.findAll();

        // Then
        assertThat(prefectures).hasSize(47);
    }

    @Test
    @DisplayName("IDで検索できる")
    void testFindById() {
        // When
        Optional<Prefecture> result = prefectureRepository.findById(13L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("東京都");
        assertThat(result.get().getNameEn()).isEqualTo("Tokyo");
    }

    @Test
    @DisplayName("存在しないIDで検索するとOptional.emptyが返る")
    void testFindById_NotFound() {
        // When
        Optional<Prefecture> result = prefectureRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("都道府県名で検索できる")
    void testFindByName() {
        // When
        Optional<Prefecture> result = prefectureRepository.findByName("東京都");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(13L);
    }

    @Test
    @DisplayName("地域で検索できる")
    void testFindByRegion() {
        // When
        List<Prefecture> kanto = prefectureRepository.findByRegionOrderById("関東");

        // Then
        assertThat(kanto).hasSize(7);  // 茨城、栃木、群馬、埼玉、千葉、東京、神奈川
        assertThat(kanto.get(0).getName()).isEqualTo("茨城県");
        assertThat(kanto.get(6).getName()).isEqualTo("神奈川県");
    }

    @Test
    @DisplayName("部分一致で検索できる")
    void testFindByNameContaining() {
        // When
        List<Prefecture> result = prefectureRepository.findByNameContaining("京");

        // Then
        assertThat(result).hasSize(2);  // 東京都、京都府
        assertThat(result)
                .extracting(Prefecture::getName)
                .containsExactlyInAnyOrder("東京都", "京都府");
    }

    @Test
    @DisplayName("複数地域で検索できる")
    void testFindByRegionIn() {
        // When
        List<Prefecture> result = prefectureRepository.findByRegionIn(
                List.of("関東", "関西")
        );

        // Then
        assertThat(result).hasSize(14);  // 関東7 + 関西7
    }

    @Test
    @DisplayName("すべての地域を取得できる")
    void testFindAllRegions() {
        // When
        List<String> regions = prefectureRepository.findAllRegions();

        // Then
        assertThat(regions).containsExactly(
                "北海道", "東北", "関東", "中部", "関西", "中国", "四国", "九州", "沖縄"
        );
    }

    @Test
    @DisplayName("新しい都道府県を保存できる")
    void testSave() {
        // Given
        Prefecture newPref = Prefecture.builder()
                .name("テスト県")
                .nameEn("Test")
                .latitude(BigDecimal.valueOf(35.689487))
                .longitude(BigDecimal.valueOf(135.691706))
                .region("テスト")
                .build();

        // When
        Prefecture saved = prefectureRepository.save(newPref);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("テスト県");

        // 保存されたか確認
        Optional<Prefecture> found = prefectureRepository.findById(saved.getId());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("都道府県を更新できる")
    void testUpdate() {
        // Given
        Prefecture tokyo = prefectureRepository.findById(13L).orElseThrow();
        String originalName = tokyo.getName();

        // When
        tokyo.setName("東京特別区");
        Prefecture updated = prefectureRepository.save(tokyo);

        // Then
        assertThat(updated.getName()).isEqualTo("東京特別区");

        // 元に戻す
        tokyo.setName(originalName);
        prefectureRepository.save(tokyo);
    }

    @Test
    @DisplayName("都道府県を削除できる")
    void testDelete() {
        // Given
        Prefecture testPref = Prefecture.builder()
                .name("削除テスト県")
                .nameEn("Delete Test")
                .latitude(BigDecimal.valueOf(35.0))
                .longitude(BigDecimal.valueOf(135.0))
                .region("テスト")
                .build();
        Prefecture saved = prefectureRepository.save(testPref);
        Long savedId = saved.getId();

        // When
        prefectureRepository.delete(saved);

        // Then
        Optional<Prefecture> found = prefectureRepository.findById(savedId);
        assertThat(found).isEmpty();
    }
}