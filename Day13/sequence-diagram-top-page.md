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
