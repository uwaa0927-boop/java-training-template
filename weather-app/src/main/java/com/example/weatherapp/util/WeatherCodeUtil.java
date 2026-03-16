package com.example.weatherapp.util;

import java.util.Map;

/**
 * 天気コード変換ユーティリティ
 * WMO天気コードを日本語と絵文字に変換
 */
public class WeatherCodeUtil {

    private static final Map<Integer, String> WEATHER_DESCRIPTIONS = Map.ofEntries(
            Map.entry(0, "快晴"),
            Map.entry(1, "晴れ"),
            Map.entry(2, "一部曇り"),
            Map.entry(3, "曇り"),
            Map.entry(45, "霧"),
            Map.entry(48, "霧氷"),
            Map.entry(51, "小雨"),
            Map.entry(53, "雨"),
            Map.entry(55, "大雨"),
            Map.entry(61, "小雨"),
            Map.entry(63, "雨"),
            Map.entry(65, "大雨"),
            Map.entry(71, "小雪"),
            Map.entry(73, "雪"),
            Map.entry(75, "大雪"),
            Map.entry(80, "にわか雨"),
            Map.entry(81, "にわか雨"),
            Map.entry(82, "激しいにわか雨"),
            Map.entry(85, "にわか雪"),
            Map.entry(86, "にわか雪"),
            Map.entry(95, "雷雨"),
            Map.entry(96, "雷雨（雹）"),
            Map.entry(99, "激しい雷雨（雹）")
    );

    private static final Map<Integer, String> WEATHER_EMOJIS = Map.ofEntries(
            Map.entry(0, "☀️"),
            Map.entry(1, "🌤️"),
            Map.entry(2, "⛅"),
            Map.entry(3, "☁️"),
            Map.entry(45, "🌫️"),
            Map.entry(48, "🌫️"),
            Map.entry(51, "🌦️"),
            Map.entry(53, "🌧️"),
            Map.entry(55, "🌧️"),
            Map.entry(61, "🌧️"),
            Map.entry(63, "🌧️"),
            Map.entry(65, "🌧️"),
            Map.entry(71, "🌨️"),
            Map.entry(73, "❄️"),
            Map.entry(75, "❄️"),
            Map.entry(80, "🌦️"),
            Map.entry(81, "🌦️"),
            Map.entry(82, "⛈️"),
            Map.entry(85, "🌨️"),
            Map.entry(86, "🌨️"),
            Map.entry(95, "⛈️"),
            Map.entry(96, "⛈️"),
            Map.entry(99, "⛈️")
    );

    public static String getDescription(Integer weatherCode) {
        return WEATHER_DESCRIPTIONS.getOrDefault(weatherCode, "不明");
    }

    public static String getEmoji(Integer weatherCode) {
        return WEATHER_EMOJIS.getOrDefault(weatherCode, "❓");
    }
}