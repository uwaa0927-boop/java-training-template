# 1 初期データファイルの作成
ファイル：data.sql

prefecturesテーブルに登録するデータをdata.sqlファイルに記載しました。

---

# 2 データ投入の実行
MySQL Workbenchからdata.sqlを実行し、prefecturesテーブルにdata.sqlファイルに記載した
47件の都道府県データが登録されていることを確認しました。

---

# 3 Spring Bootでの起動確認
アプリケーションを起動し、起動時のログから下記を確認しました。
- データベースに接続して、data.sqlのデータが投入されていること

---

# 4 Repositoryでのデータ取得確認
下記のURLからデータベースに登録した都道府県のデータをRepositoryを使って取得できるかを確認しました。

- http://localhost:8080/api/test/prefectures
　→47都道府県のデータが取得できること
- http://localhost:8080/api/test/prefectures/region/関東
　→regionが関東のデータのみが取得できること
