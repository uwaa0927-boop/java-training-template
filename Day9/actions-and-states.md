# アクションと状態の定義

## ユーザーアクション一覧

| アクションID | アクション名 | 発生画面 | 詳細 |
|-------------|------------|---------|------|
| ACT-001 | 都道府県クリック | HOME-001 | カードをクリック |
| ACT-002 | 戻るボタンクリック | DETAIL-001 | ヘッダーの戻るボタン |
| ACT-003 | トップへ戻るクリック | ERROR-XXX | エラーページから復帰 |
| ACT-004 | ページ更新 | 任意 | F5キーまたは更新ボタン |
| ACT-005 | お気に入り追加 | DETAIL-001 | お気に入りボタン（任意） |

---

## システム状態一覧

| 状態ID | 状態名 | 説明 | 表示 |
|--------|--------|------|------|
| STATE-001 | 初期表示 | ページ読み込み完了 | 都道府県一覧 |
| STATE-002 | ローディング | API呼び出し中 | スピナー |
| STATE-003 | データ表示 | API取得成功 | 天気情報 |
| STATE-004 | エラー表示 | API取得失敗 | エラーメッセージ |
| STATE-005 | 404エラー | 存在しないURL | 404ページ |

---

## 状態遷移

### トップページ

初期状態 → データ取得 → 表示完了

### 詳細ページ

初期状態
  ↓
ローディング
  ↓
  ├─成功 → データ表示
  │
  └─失敗 → エラー表示

---

## バックエンド処理フロー

### 詳細ページアクセス時

1. **Controller**: リクエスト受付
   @GetMapping("/weather/{prefectureId}")
   public String getWeather(@PathVariable Long prefectureId, Model model)

2. **Service**: ビジネスロジック
   // 1. 都道府県情報取得
   Prefecture pref = prefectureService.findById(prefectureId);
   
   // 2. API呼び出し
   OpenMeteoResponseDto apiResponse = 
       openMeteoClient.fetchWeather(pref.getLatitude(), pref.getLongitude());
   
   // 3. データ保存
   weatherRecordService.save(apiResponse);
   
   // 4. DTOに変換
   WeatherDetailDto dto = mapToDto(apiResponse);

3. **Controller**: Viewに渡す
   model.addAttribute("weather", dto);
   return "weather-detail";

4. **View**: Thymeleafで表示
   <div th:text="${weather.currentWeather.temperature2m}"></div>
