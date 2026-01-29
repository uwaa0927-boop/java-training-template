package com.example.weatherapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prefecture Entityのテスト
 */
class PrefectureTest {

    @Test
    @DisplayName("Prefectureオブジェクトが正しく生成される")
    void testPrefectureCreation() {
        // Given
        Prefecture prefecture = Prefecture.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .latitude(35.689487)
                .longitude(139.691706)
                .region("関東")
                .build();

        // When
        prefecture.onCreate();  // @PrePersistを手動実行

        // Then
        assertThat(prefecture.getId()).isEqualTo(13L);
        assertThat(prefecture.getName()).isEqualTo("東京都");
        assertThat(prefecture.getNameEn()).isEqualTo("Tokyo");
        assertThat(prefecture.getLatitude()).isEqualTo(35.689487);
        assertThat(prefecture.getLongitude()).isEqualTo(139.691706);
        assertThat(prefecture.getRegion()).isEqualTo("関東");
        assertThat(prefecture.getCreatedAt()).isNotNull();
        assertThat(prefecture.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("@PreUpdateで更新日時が更新される")
    void testPreUpdate() throws InterruptedException {
        // Given
        Prefecture prefecture = Prefecture.builder()
                .name("東京都")
                .nameEn("Tokyo")
                .latitude(35.689487)
                .longitude(139.691706)
                .region("関東")
                .build();
        prefecture.onCreate();

        LocalDateTime originalUpdatedAt = prefecture.getUpdatedAt();

        // When
        Thread.sleep(10);  // 時刻の違いを作るため少し待つ
        prefecture.onUpdate();  // @PreUpdateを手動実行

        // Then
        assertThat(prefecture.getUpdatedAt()).isAfter(originalUpdatedAt);
        assertThat(prefecture.getCreatedAt()).isEqualTo(prefecture.getCreatedAt()); // 変わらない
    }
}