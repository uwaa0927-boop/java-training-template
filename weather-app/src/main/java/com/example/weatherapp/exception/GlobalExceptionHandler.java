package com.example.weatherapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * グローバル例外ハンドラー
 * すべてのControllerで発生した例外を統一的に処理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ResourceNotFoundExceptionのハンドリング
     * 都道府県が見つからない場合など
     *
     * @param e 例外
     * @param model モデル
     * @param request HTTPリクエスト
     * @return エラーページ
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(
            ResourceNotFoundException e,
            Model model,
            HttpServletRequest request
    ) {
        log.warn("リソースが見つかりません: path={}, message={}",
                request.getRequestURI(),
                e.getMessage()
        );

        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("requestUrl", request.getRequestURI());
        model.addAttribute("statusCode", 404);

        return "error/404";
    }

    /**
     * ExternalApiExceptionのハンドリング
     * 外部API（Open-Meteo）呼び出し失敗時
     *
     * @param e 例外
     * @param model モデル
     * @param request HTTPリクエスト
     * @return エラーページ
     */
    @ExceptionHandler(ExternalApiException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleExternalApiError(
            ExternalApiException e,
            Model model,
            HttpServletRequest request
    ) {
        log.error("外部API呼び出しエラー: path={}, message={}",
                request.getRequestURI(),
                e.getMessage(),
                e
        );

        model.addAttribute("errorMessage", "天気情報の取得に失敗しました");
        model.addAttribute("errorDetail", e.getMessage());
        model.addAttribute("requestUrl", request.getRequestURI());
        model.addAttribute("statusCode", 503);

        // リトライ用のパラメータを抽出
        String prefectureId = extractPrefectureId(request.getRequestURI());
        if (prefectureId != null) {
            model.addAttribute("prefectureId", prefectureId);
            model.addAttribute("retryUrl", "/weather/" + prefectureId);
        }

        return "error/api-error";
    }

    /**
     * BusinessExceptionのハンドリング
     * ビジネスロジックエラー
     *
     * @param e 例外
     * @param model モデル
     * @return エラーページ
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessError(
            BusinessException e,
            Model model,
            HttpServletRequest request
    ) {
        log.warn("ビジネスエラー: path={}, message={}",
                request.getRequestURI(),
                e.getMessage()
        );

        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("statusCode", 400);

        return "error/business-error";
    }

    /**
     * NoHandlerFoundExceptionのハンドリング
     * 存在しないURLにアクセスした場合
     *
     * @param e 例外
     * @param model モデル
     * @return エラーページ
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(
            NoHandlerFoundException e,
            Model model
    ) {
        log.warn("ページが見つかりません: {}", e.getRequestURL());

        model.addAttribute("errorMessage", "お探しのページは見つかりませんでした");
        model.addAttribute("requestUrl", e.getRequestURL());
        model.addAttribute("statusCode", 404);

        return "error/404";
    }

    /**
     * すべての予期しない例外のハンドリング
     * 最終的なセーフティネット
     *
     * @param e 例外
     * @param model モデル
     * @param request HTTPリクエスト
     * @return エラーページ
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(
            Exception e,
            Model model,
            HttpServletRequest request
    ) {
        log.error("予期しないエラーが発生しました: path={}",
                request.getRequestURI(),
                e  // スタックトレースも出力
        );

        model.addAttribute("errorMessage", "システムエラーが発生しました");
        model.addAttribute("requestUrl", request.getRequestURI());
        model.addAttribute("statusCode", 500);

        // 開発環境のみエラー詳細を表示
        if (isDevelopmentMode()) {
            model.addAttribute("errorDetail", e.getMessage());
            model.addAttribute("stackTrace", getStackTraceAsString(e));
        }

        return "error/500";
    }

    /**
     * URLから都道府県IDを抽出
     * /weather/13 → "13"
     */
    private String extractPrefectureId(String uri) {
        if (uri == null || !uri.startsWith("/weather/")) {
            return null;
        }

        String[] parts = uri.split("/");
        if (parts.length >= 3) {
            return parts[2].split("\\?")[0];  // クエリパラメータを除去
        }

        return null;
    }

    /**
     * 開発モードかどうか判定
     */
    private boolean isDevelopmentMode() {
        String profile = System.getProperty("spring.profiles.active", "");
        return profile.contains("dev") || profile.isEmpty();
    }

    /**
     * スタックトレースを文字列化
     */
    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
            if (sb.length() > 2000) {  // 長すぎる場合は切り詰め
                sb.append("...(省略)");
                break;
            }
        }
        return sb.toString();
    }
}