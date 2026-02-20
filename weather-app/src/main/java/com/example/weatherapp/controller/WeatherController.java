package com.example.weatherapp.controller;

import com.example.weatherapp.dto.WeatherDetailDto;
import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        // 天気情報を取得（存在しない場合は例外が投げられる）
        WeatherDetailDto weather = weatherService.getWeatherByPrefectureId(id);

        // Modelに設定
        model.addAttribute("weather", weather);

        log.debug("天気情報取得成功: {}", weather.getPrefecture().getName());

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
        WeatherDetailDto weather =
                weatherService.getLatestWeatherFromDb(id);

        if (weather == null) {
            throw new ResourceNotFoundException(
                    "まだ天気情報が取得されていません"
            );
        }

        model.addAttribute("weather", weather);
        return "weather-detail";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(
            ResourceNotFoundException e,
            Model model
    ) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

}