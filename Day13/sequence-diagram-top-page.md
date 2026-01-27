# トップページのシーケンス図

## 1. 基本フロー（全都道府県表示）

### 処理概要
1. ユーザーがトップページにアクセス
2. データベースから47都道府県を取得
3. 地域別にグループ化して表示

### 参加オブジェクト
- **ブラウザ**: ユーザーインターフェース
- **HomeController**: リクエスト処理
- **PrefectureService**: ビジネスロジック
- **PrefectureRepository**: データアクセス
- **Database**: データ永続化

### 詳細フロー

#### 1. リクエスト受付
@GetMapping("/")
public String index(Model model) {

#### 2. Service呼び出し
List<PrefectureDto> prefectures = prefectureService.getAllPrefectures();

#### 3. Repository呼び出し
public List<PrefectureDto> getAllPrefectures() {
    List<Prefecture> entities = prefectureRepository.findAll();
    return entities.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
}

#### 4. データベースクエリ
SELECT * FROM prefectures ORDER BY id

#### 5. DTO変換
private PrefectureDto toDto(Prefecture entity) {
    return PrefectureDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .latitude(entity.getLatitude())
        .longitude(entity.getLongitude())
        .region(entity.getRegion())
        .build();
}

#### 6. Modelに追加
model.addAttribute("prefectures", prefectures);
return "index";

#### 7. Thymeleafレンダリング
<div th:each="pref : ${prefectures}">
    <a th:href="@{/weather/{id}(id=${pref.id})}" 
       th:text="${pref.name}"></a>
</div>

---

## 2. 地域フィルターフロー（オプション）

### 処理概要
特定の地域の都道府県のみを表示

### URLパラメータ
/?region=関東

### Controllerでの処理
@GetMapping("/")
public String index(
    @RequestParam(required = false) String region,
    Model model
) {
    List<PrefectureDto> prefectures;
    
    if (region != null && !region.isEmpty()) {
        prefectures = prefectureService.getPrefecturesByRegion(region);
    } else {
        prefectures = prefectureService.getAllPrefectures();
    }
    
    model.addAttribute("prefectures", prefectures);
    model.addAttribute("selectedRegion", region);
    return "index";
}

### Repositoryメソッド
List<Prefecture> findByRegion(String region);

### 生成されるSQL
SELECT * FROM prefectures WHERE region = '関東' ORDER BY id

---

## 3. エラーハンドリングフロー

### エラーケース
1.データベース接続エラー
2.データが0件
3.予期しない例外

### 例外処理
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(
        DataAccessException e, 
        Model model
    ) {
        log.error("Database error", e);
        model.addAttribute("errorMessage", "データベースエラーが発生しました");
        return "error/500";
    }
}

---

## 4. パフォーマンス考察

### データ量
- 47都道府県（固定）
- 1リクエストあたり47件のデータ取得

### レスポンスタイム目標
- データベースクエリ: 10ms以内
- DTO変換: 5ms以内
- HTMLレンダリング: 50ms以内
- 合計: 100ms以内

### 最適化ポイント
1.キャッシング: 都道府県マスタは変更されないため、キャッシュ可能
2.インデックス: prefectures.region にインデックス追加
3.N+1問題: 現状は発生しない（リレーションなし）
