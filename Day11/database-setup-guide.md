# 1 テーブル定義書
ファイル：table-definitions.md

- prefectures（都道府県マスタ）
- weather_records（天気記録）
- daily_forecasts（日次予報）

上記3つのテーブルについて、データ型等をまとめています。

---

# 2 DDLスクリプト作成
ファイル：schema.sql

- データベースの作成
- テーブル作成
- インデックス設定

データベースやテーブルを作成するための処理等をまとめています。

---

# 3 MySQL Workbenchでの実行
作成したschema.sqlをMySQL Workbenchから実行して下記を確認しました。

- データベースが作成されること
- 3つのテーブルが作成されること

---

# 4 Spring Bootでの自動実行設定
ファイル：application.properties

Spring Boot起動時にschema.sqlが自動で実行されるように、上記のファイルに設定を追記しました。
アプリ起動時にテーブルが存在する場合は削除 → 作成が自動で行われるようになります。
