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

        try {
            // 天気情報を取得（API呼び出し → DB保存 → DTO返却）
            WeatherDetailDto weather = weatherService.getWeatherByPrefectureId(id);

            // Modelに設定
            model.addAttribute("weather", weather);

            log.debug("天気情報取得成功: {}", weather.getPrefecture().getName());

            return "weather-detail";

        } catch (ResourceNotFoundException e) {
            // 都道府県が存在しない
            log.warn("都道府県が見つかりません: id={}", id);
            model.addAttribute("errorMessage", "指定された都道府県が見つかりません");
            return "error/404";

        } catch (ExternalApiException e) {
            // API呼び出し失敗
            log.error("天気API呼び出し失敗: id={}", id, e);
            model.addAttribute("errorMessage", "天気情報の取得に失敗しました");
            model.addAttribute("prefectureId", id);
            return "error/api-error";

        } catch (Exception e) {
            // その他の予期しないエラー
            log.error("予期しないエラー: id={}", id, e);
            model.addAttribute("errorMessage", "システムエラーが発生しました");
            return "error/500";
        }
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

        try {
            WeatherDetailDto weather = weatherService.getLatestWeatherFromDb(id);

            if (weather == null) {
                log.warn("天気データが存在しません: id={}", id);
                model.addAttribute("errorMessage", "天気データがまだ取得されていません");
                return "error/404";
            }

            model.addAttribute("weather", weather);
            return "weather-detail";

        } catch (Exception e) {
            log.error("エラー: id={}", id, e);
            model.addAttribute("errorMessage", "エラーが発生しました");
            return "error/500";
        }
    }
}