package com.example.weatherapp.integration;

import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.WeatherRecord;
import com.example.weatherapp.repository.PrefectureRepository;
import com.example.weatherapp.repository.WeatherRecordRepository;
import com.example.weatherapp.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * WeatherServiceの統合テスト
 * 実際のAPIを呼び出すため、テストに時間がかかる
 */
@SpringBootTest
@Transactional
@Sql("/test-data.sql")
class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRecordRepository weatherRecordRepository;

    @Autowired
    private PrefectureRepository prefectureRepository;

    @Test
    @DisplayName("天気情報を取得してDBに保存できる")
    void testGetWeatherByPrefectureId() {
        // Given
        Long tokyoId = 13L;

        // When
        WeatherDetailDto result = weatherService.getWeatherByPrefectureId(tokyoId);

        // Then
        // DTOの検証
        assertThat(result).isNotNull();
        assertThat(result.getPrefecture()).isNotNull();
        assertThat(result.getPrefecture().getName()).isEqualTo("東京都");

        // 現在の天気
        assertThat(result.getCurrent()).isNotNull();
        assertThat(result.getCurrent().getTemperature_2m()).isNotNull();
        assertThat(result.getCurrent().getWeathercode()).isNotNull();

        // 日次予報（7日分）
        assertThat(result.getDailyForecasts()).isNotNull();
        assertThat(result.getDailyForecasts()).hasSize(7);

        // DBに保存されているか確認
        Prefecture tokyo = prefectureRepository.findById(tokyoId).orElseThrow();

        Optional<WeatherRecord> saved = weatherRecordRepository
                .findFirstByPrefectureOrderByFetchedAtDesc(tokyo);

        assertThat(saved).isPresent();
        assertThat(saved.get().getPrefecture().getId()).isEqualTo(tokyoId);
        assertThat(saved.get().getDailyForecasts()).hasSize(7);
    }

    @Test
    @DisplayName("DB保存済みの最新天気を取得できる")
    void testGetLatestWeatherFromDb() {
        // Given: 事前に天気データを保存
        weatherService.getWeatherByPrefectureId(13L);

        // When: DB から取得（API呼び出しなし）
        WeatherDetailDto result = weatherService.getLatestWeatherFromDb(13L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPrefecture().getName()).isEqualTo("東京都");
        assertThat(result.getCurrent()).isNotNull();
        assertThat(result.getDailyForecasts()).hasSize(7);
    }

    @Test
    @DisplayName("天気データがない場合はnullを返す")
    void testGetLatestWeatherFromDb_NoData() {
        // When: データが存在しない都道府県
        WeatherDetailDto result = weatherService.getLatestWeatherFromDb(1L);

        // Then
        assertThat(result).isNull();
    }
}