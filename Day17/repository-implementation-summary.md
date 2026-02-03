# Day17-Repository層の実装まとめ

## 実施したこと
- 下記のファイルを作成
　・PrefectureRepository.java
　・WeatherRecordRepository.java
　・DailyForecastRepository.java
- テスト用の下記のファイルを作成、テストの実行
　・PrefectureRepositoryTest.java
　・test-data.sql
　
## 発生した問題
- 「./gradlew test」コマンドを実行するとテストに失敗してしまう

## 原因
- テスト用のデータ数と、テストでチェックしようとしているデータ数がズレていた
- テスト用に必要な設定が足りていなかった
