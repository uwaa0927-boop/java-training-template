# エラーハンドリングフロー

## エラー種類と対応

### 1. 404エラー（ページが見つからない）

#### 発生ケース
- 存在しないURLにアクセス
- 存在しない都道府県IDを指定

#### 処理フロー
リクエスト
  ↓
Controller
  ↓
@ExceptionHandler(ResourceNotFoundException)
  ↓
error/404.html表示

#### 実装
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }
}

---

### 2. 500エラー（サーバーエラー）

#### 発生ケース
- Open-Meteo APIタイムアウト
- データベース接続エラー
- 予期しない例外

#### 処理フロー
API呼び出し
  ↓
タイムアウト発生
  ↓
ExternalApiException throw
  ↓
@ExceptionHandler(ExternalApiException)
  ↓
error/500.html表示

#### 実装
@ExceptionHandler(ExternalApiException.class)
public String handleApiError(ExternalApiException e, Model model) {
    model.addAttribute("errorMessage", "天気情報の取得に失敗しました");
    return "error/500";
}

@ExceptionHandler(Exception.class)
public String handleGeneralError(Exception e, Model model) {
    log.error("Unexpected error", e);
    model.addAttribute("errorMessage", "予期しないエラーが発生しました");
    return "error/500";
}

---

### 3. バリデーションエラー

#### 発生ケース
- 無効な都道府県ID（1-47以外）

#### 処理フロー
/weather/999 アクセス
  ↓
@PathVariable validation
  ↓
MethodArgumentNotValidException
  ↓
400 Bad Request

---

## ユーザーへの表示

### 404エラーページ
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>404 - ページが見つかりません</title>
</head>
<body>
    <h1>404</h1>
    <p>お探しのページが見つかりませんでした。</p>
    <p th:text="${errorMessage}"></p>
    <a href="/">トップページへ戻る</a>
</body>
</html>

### 500エラーページ
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>500 - エラーが発生しました</title>
</head>
<body>
    <h1>500</h1>
    <p>申し訳ございません。エラーが発生しました。</p>
    <p th:text="${errorMessage}"></p>
    <a href="/">トップページへ戻る</a>
</body>
</html>

