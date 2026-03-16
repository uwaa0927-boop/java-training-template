package com.example.weatherapp.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * WeatherCodeUtilの単体テスト
 */
@DisplayName("WeatherCodeUtil単体テスト")
class WeatherCodeUtilTest {

    @ParameterizedTest
    @CsvSource({
            "0, 快晴",
            "1, 晴れ",
            "2, 一部曇り",
            "3, 曇り",
            "45, 霧",
            "61, 小雨",
            "71, 小雪",
            "95, 雷雨"
    })
    @DisplayName("天気コードから説明文を取得")
    void testGetDescription(int code, String expected) {
        assertThat(WeatherCodeUtil.getDescription(code))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("不明なコードの場合")
    void testGetDescription_Unknown() {
        assertThat(WeatherCodeUtil.getDescription(999))
                .isEqualTo("不明");
    }

    @ParameterizedTest
    @CsvSource({
            "0, ☀️",
            "1, 🌤️",
            "61, 🌧️",
            "95, ⛈️"
    })
    @DisplayName("天気コードから絵文字を取得")
    void testGetEmoji(int code, String expected) {
        assertThat(WeatherCodeUtil.getEmoji(code))
                .isEqualTo(expected);
    }
}