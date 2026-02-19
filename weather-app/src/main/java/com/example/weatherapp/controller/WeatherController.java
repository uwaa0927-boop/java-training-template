package com.example.weatherapp.controller;

import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 天気詳細画面のController
 */
@Controller
@RequestMapping("/weather")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * 天気詳細ページ表示
     *
     * URL: /weather/{id}
     * 例: /weather/13 → 東京都の天気
     *
     * @param id 都道府県ID
     * @param model ビューに渡すデータ
     * @return テンプレート名
     */
    @GetMapping("/{id}")
    public String showWeather(
            @PathVariable Long id,
            Model model
    ) {
        log.info("天気詳細ページ表示: prefectureId={}", id);

        WeatherDetailDto weather =
                weatherService.getWeatherByPrefectureId(id);

        model.addAttribute("weather", weather);
        return "weather-detail";
    }

    /**
     * DB保存済みの最新天気を表示（API呼び出しなし）
     *
     * URL: /weather/{id}/latest
     *
     * @param id 都道府県ID
     * @param model ビューに渡すデータ
     * @return テンプレート名
     */
    @GetMapping("/{id}/latest")
    public String showLatestWeather(
            @PathVariable Long id,
            Model model
    ) {
        log.info("最新天気表示（DB）: prefectureId={}", id);

        WeatherDetailDto weather =
                weatherService.getLatestWeatherFromDb(id);

        if (weather == null) {
            // ここは「自分で例外を投げる」
            throw new ResourceNotFoundException(
                    "天気データがまだ取得されていません"
            );
        }

        model.addAttribute("weather", weather);
        return "weather-detail";
    }
}