package com.example.weatherapp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * WMO天気コードと説明文・アイコンのマッピング
 */
public class WeatherCodeMapper {

    private static final Map<Integer, String> DESCRIPTIONS = new HashMap<>();
    private static final Map<Integer, String> ICONS = new HashMap<>();

    static {
        // 晴れ系（0-3）
        DESCRIPTIONS.put(0, "快晴");
        DESCRIPTIONS.put(1, "晴れ");
        DESCRIPTIONS.put(2, "一部曇り");
        DESCRIPTIONS.put(3, "曇り");

        ICONS.put(0, "☀️");
        ICONS.put(1, "🌤️");
        ICONS.put(2, "⛅");
        ICONS.put(3, "☁️");

        // 霧（45-48）
        DESCRIPTIONS.put(45, "霧");
        DESCRIPTIONS.put(48, "霧氷");

        ICONS.put(45, "🌫️");
        ICONS.put(48, "🌫️");

        // 霧雨（51-55）
        DESCRIPTIONS.put(51, "軽い霧雨");
        DESCRIPTIONS.put(53, "霧雨");
        DESCRIPTIONS.put(55, "激しい霧雨");

        ICONS.put(51, "🌦️");
        ICONS.put(53, "🌦️");
        ICONS.put(55, "🌦️");

        // 雨（61-67）
        DESCRIPTIONS.put(61, "小雨");
        DESCRIPTIONS.put(63, "雨");
        DESCRIPTIONS.put(65, "大雨");
        DESCRIPTIONS.put(66, "凍雨");
        DESCRIPTIONS.put(67, "激しい凍雨");

        ICONS.put(61, "🌧️");
        ICONS.put(63, "🌧️");
        ICONS.put(65, "🌧️");
        ICONS.put(66, "🌧️");
        ICONS.put(67, "🌧️");

        // 雪（71-77）
        DESCRIPTIONS.put(71, "小雪");
        DESCRIPTIONS.put(73, "雪");
        DESCRIPTIONS.put(75, "大雪");
        DESCRIPTIONS.put(77, "雪粒");

        ICONS.put(71, "🌨️");
        ICONS.put(73, "❄️");
        ICONS.put(75, "❄️");
        ICONS.put(77, "🌨️");

        // にわか雨・雪（80-86）
        DESCRIPTIONS.put(80, "にわか雨");
        DESCRIPTIONS.put(81, "強いにわか雨");
        DESCRIPTIONS.put(82, "激しいにわか雨");
        DESCRIPTIONS.put(85, "にわか雪");
        DESCRIPTIONS.put(86, "強いにわか雪");

        ICONS.put(80, "🌦️");
        ICONS.put(81, "🌦️");
        ICONS.put(82, "🌦️");
        ICONS.put(85, "🌨️");
        ICONS.put(86, "🌨️");

        // 雷雨（95-99）
        DESCRIPTIONS.put(95, "雷雨");
        DESCRIPTIONS.put(96, "雷雨（雹）");
        DESCRIPTIONS.put(99, "激しい雷雨（雹）");

        ICONS.put(95, "⛈️");
        ICONS.put(96, "⛈️");
        ICONS.put(99, "⛈️");
    }

    /**
     * 天気コードから説明文を取得
     */
    public static String getDescription(Integer code) {
        if (code == null) {
            return "不明";
        }
        return DESCRIPTIONS.getOrDefault(code, "不明");
    }

    /**
     * 天気コードからアイコンを取得
     */
    public static String getIcon(Integer code) {
        if (code == null) {
            return "❓";
        }
        return ICONS.getOrDefault(code, "❓");
    }
}
