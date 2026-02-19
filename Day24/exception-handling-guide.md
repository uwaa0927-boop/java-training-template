# Spring例外ハンドリングガイド

## @ControllerAdviceの基本

全Controllerで共通の例外処理を定義できる。

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }
}

## @ExceptionHandlerの優先順位

1. Controller内の@ExceptionHandler（最優先）
2. @ControllerAdvice内の@ExceptionHandler
3. Spring Bootのデフォルトエラーハンドリング

## @ResponseStatusの使い方

@ExceptionHandler(ResourceNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)  // 404ステータスを返す
public String handleNotFound(...) {
    return "error/404";
}

## ログ出力のベストプラクティス

- warn: ユーザーエラー（404など）
- error: システムエラー（500など）
- debug: 詳細なデバッグ情報

@ExceptionHandler(Exception.class)
public String handleError(Exception e, Model model) {
    log.error("予期しないエラー", e);  // スタックトレース込み
    return "error/500";
}
