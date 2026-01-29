# Entity実装サマリー

## 実装したEntity

### 1. Prefecture（都道府県マスタ）

#### フィールド
- id: 主キー（AUTO_INCREMENT）
- name: 都道府県名
- nameEn: 英語名
- latitude: 緯度
- longitude: 経度
- region: 地域区分
- createdAt: 作成日時
- updatedAt: 更新日時

#### リレーション
- なし（マスタテーブル）

---

### 2. WeatherRecord（天気記録）

#### フィールド
- id: 主キー
- prefecture: 都道府県（外部キー）
- fetchedAt: 取得日時
- temperature: 気温
- weatherCode: 天気コード
- windSpeed: 風速
- humidity: 湿度
- apparentTemperature: 体感温度
- precipitation: 降水量
- cloudCover: 雲量
- createdAt: 作成日時
- dailyForecasts: 日次予報リスト

#### リレーション
- @ManyToOne: Prefecture（多対1）
- @OneToMany: DailyForecast（1対多）

---

### 3. DailyForecast（日次予報）

#### フィールド
- id: 主キー
- weatherRecord: 天気記録（外部キー）
- forecastDate: 予報日
- temperatureMax: 最高気温
- temperatureMin: 最低気温
- weatherCode: 天気コード
- precipitationSum: 降水量合計
- windSpeedMax: 最大風速
- sunrise: 日の出時刻
- sunset: 日の入り時刻
- createdAt: 作成日時

#### リレーション
- @ManyToOne: WeatherRecord（多対1）

---

## ER図

Prefecture (1) ←──── () WeatherRecord (1) ←──── () DailyForecast

---

## 実装のポイント

### Lombokアノテーション
- @Data: getter/setter/toString/equals/hashCode
- @Builder: ビルダーパターン
- @NoArgsConstructor / @AllArgsConstructor: コンストラクタ
- @ToString.Exclude: toString()から除外（循環参照防止）

### JPAアノテーション
- @Entity: Entityクラス
- @Table: テーブル名指定
- @Id: 主キー
- @GeneratedValue: 主キー生成戦略
- @Column: カラム属性
- @ManyToOne / @OneToMany: リレーション
- @JoinColumn: 外部キー
- @PrePersist / @PreUpdate: ライフサイクルコールバック

### パフォーマンス考慮
- FetchType.LAZY: 遅延読み込み
- CascadeType.ALL: カスケード設定
- orphanRemoval: 孤児削除

---

## テスト観点

### 単体テスト
- [ ] オブジェクトが正しく生成される
- [ ] Builderパターンが動作する
- [ ] @PrePersistで日時が設定される
- [ ] @PreUpdateで更新日時が更新される
- [ ] リレーションが正しく設定される

### 統合テスト（Day 25で実施）
- [ ] データベースに保存できる
- [ ] データベースから取得できる
- [ ] リレーションが正しく動作する
- [ ] カスケードが動作する
