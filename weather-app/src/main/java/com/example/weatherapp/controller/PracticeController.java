package com.example.weatherapp.controller;

import com.example.weatherapp.dto.PrefectureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Thymeleaf練習用Controller
 */
@Controller
@RequestMapping("/practice")
@Slf4j
public class PracticeController {

    @GetMapping("/thymeleaf-basic")
    public String thymeleafBasic(Model model) {
        log.info("Thymeleaf基礎練習ページ表示");

        // 1. テキスト表示用
        model.addAttribute("message", "Thymeleafへようこそ！");
        model.addAttribute("username", "山田太郎");

        // 2. 繰り返し用
        List<PrefectureDto> prefectures = Arrays.asList(
                PrefectureDto.builder().id(13L).name("東京都").region("関東").build(),
                PrefectureDto.builder().id(27L).name("大阪府").region("関西").build(),
                PrefectureDto.builder().id(1L).name("北海道").region("北海道").build()
        );
        model.addAttribute("prefectures", prefectures);

        // 3. 条件分岐用
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("userRole", "admin");

        // 4. URL生成用
        model.addAttribute("samplePrefectureId", 13L);

        // 5. 日付フォーマット用
        model.addAttribute("now", LocalDateTime.now());

        // 6. 数値フォーマット用
        model.addAttribute("temperature", 15.678);
        model.addAttribute("humidity", 65);

        // 7. Elvis演算子用
        model.addAttribute("nickname", null);  // nullの場合のテスト

        // 8. セーフナビゲーション用
        model.addAttribute("prefecture", null);  // nullの場合のテスト

        return "practice/thymeleaf-basic";
    }

    @GetMapping("/layout-example")
    public String layoutExample() {
        return "practice/layout-example";
    }
}