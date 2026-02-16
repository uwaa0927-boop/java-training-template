package com.example.weatherapp.controller;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.service.PrefectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * ホーム画面のController
 * トップページで47都道府県の一覧を表示
 */
@Controller
@Slf4j
public class HomeController {

    private final PrefectureService prefectureService;

    /**
     * コンストラクタインジェクション
     */
    public HomeController(PrefectureService prefectureService) {
        this.prefectureService = prefectureService;
    }

    /**
     * トップページ表示
     *
     * URL: http://localhost:8080/
     *
     * @param region 地域フィルタ（任意）
     * @param model ビューに渡すデータ
     * @return テンプレート名（index.html）
     */
    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String region,
            Model model
    ) {
        log.info("トップページ表示: region={}", region);

        if (region != null && !region.isEmpty()) {
            // 地域フィルタが指定されている場合
            log.debug("地域フィルタ適用: {}", region);

            List<PrefectureDto> prefectures = prefectureService.findByRegion(region);
            model.addAttribute("prefectures", prefectures);
            model.addAttribute("selectedRegion", region);

        } else {
            // すべての都道府県を地域別にグループ化
            Map<String, List<PrefectureDto>> groupedPrefectures =
                    prefectureService.findAllGroupedByRegion();

            model.addAttribute("groupedPrefectures", groupedPrefectures);
        }

        // 地域リストをドロップダウン用に追加
        List<String> regions = List.of(
                "北海道", "東北", "関東", "中部",
                "関西", "中国", "四国", "九州", "沖縄"
        );
        model.addAttribute("regions", regions);

        log.debug("モデルにデータを設定完了");

        return "index";  // templates/index.html
    }

    /**
     * ヘルスチェック用エンドポイント
     *
     * URL: http://localhost:8080/health
     *
     * @return ステータスメッセージ
     */
    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("status", "OK");
        model.addAttribute("message", "Application is running");
        return "health";
    }
}