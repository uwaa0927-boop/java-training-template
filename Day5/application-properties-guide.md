1. サーバー設定
・server.port
アプリが起動するポート番号を指定する

・server.servlet.context-path
アプリのルートパスを指定する

・server.servlet.session.timeout
有効時間を秒単位で指定する
一定時間がない場合はログアウトする

・server.max-http-header-size
HTTPリクエストのヘッダーサイズの上限を指定する

----------------------

2. データベース設定
・spring.datasource.hikari.maximum-pool-size
同時に使用できるデータベースの最大数を指定する

・spring.datasource.hikari.minimum-idle
常に待機しておくデータベースの最小数を指定する

・spring.datasource.hikari.connection-timeout
データベースとの接続が見つからない場合、接続が確立するまでの待ち時間を指定する

・spring.sql.init.mode
アプリを起動する際にSQLの初期化をするかどうかを指定する

・spring.sql.init.schema-locations
テーブルを定義するSQLファイルの場所を指定する

・spring.sql.init.data-locations
初期のデータを登録するSQLファイルの場所を指定する

----------------------

3. JPA / Hibernate 設定
・spring.jpa.hibernate.ddl-auto
アプリを起動する際のテーブルの操作について指定する

・spring.jpa.show-sql
実行されたSQLをログに表示するかを指定する

・spring.jpa.properties.hibernate.format_sql
SQLを見やすい形式で表示するための設定

・spring.jpa.hibernate.naming.physical-strategy
Javaのクラス名とデータベースのテーブル名の変換ルールを指定する

----------------------

4. ログ設定
・logging.level
システムが出力するログを分類する
- TRACE：DEBUGよりもさらに詳細に記録する
- DEBUG：処理の流れを記録する
- INFO：正常な動作を記録する
- ERROR：何か問題が発生したことを記録する

・logging.file.name
ログをファイルで保存する場合のファイル名を指定する

・logging.file.max-size
ログファイルの最大のサイズを指定する

・logging.file.max-history
保存しておくログファイルの最大の保存数を指定する
