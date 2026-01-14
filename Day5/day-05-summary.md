# Day 5 学習まとめ

## 学んだこと

### Spring Bootの仕組み
- @SpringBootApplicationの3つの役割
- DIコンテナの仕組み
- 自動設定の動作原理

### アノテーション
- @Controller, @Service, @Repository, @Component
- @GetMapping, @PostMapping
- @RequestParam, @PathVariable
- @ResponseBody

### Modelの使い方
- 基本データ型の渡し方
- リスト、Mapの渡し方
- オブジェクトの渡し方

### application.properties
- サーバー設定
- データベース設定
- JPA設定
- ログ設定
- プロファイル別設定

### ロギング
- SLF4Jの使い方
- ログレベルの使い分け
- Lombokの@Slf4j

## 成果物

1. spring-boot-architecture.md
2. PracticeController.java
3. ModelPracticeController.java
4. LoggingController.java
5. 各種テンプレートファイル
6. application-dev.properties
7. application-prod.properties
8. application-properties-guide.md

## 感想・気づき

初めは何がどこで動いているのかについていくことが難しい場面もありましたが、ファイルを1つずつ追うことで
少しずつではありますが全体の流れが見えてきたように感じました。
アノテーションの役割や記述にはまだ苦手意識があるため、繰り返し復習して定着させたいです。

## 明日への準備

- Open-Meteo APIの公式ドキュメントを読む
- 要件定義の書き方を調べる

##本日のまとめ

1. Spring Bootのどの機能が最も便利だと思いましたか？
　アプリを起動するだけで、すぐにブラウザから動作の確認ができるところが、今の変更で何が変わったのかを
　すぐに試せるため便利に感じました。

2. アノテーションの使い分けで困ったことは？
　どのアノテーションがどんな役割を持っているのかを理解するのが苦手で、どうしてここにこのアノテーションが当てはまるのかを
　整理しながら使い分けるのが難しいと感じました。

3. application.propertiesで設定したい項目は他にありますか？
　エラーが起きた際の原因が分かりやすくなるように、ログのレベルを調節してコントロールできるようになりたいと思いました。

4. 明日からの開発で活用したい知識は？
　ログを見る方法を学んだため、何が起こっているのか動きを自分で確認しながら進めるように意識したいです。
