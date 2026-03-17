# 天気予報アプリ

## 概要

47都道府県の天気情報を提供するWebアプリケーション。Open-Meteo APIを使用してリアルタイムの天気データを取得し、見やすいUIで表示します。

## 主な機能

- 🌤️ 47都道府県の天気情報表示
- 📊 7日間の天気予報
- 🔍 都道府県検索・地域フィルタ
- ⭐ お気に入り機能
- 📱 レスポンシブデザイン
- ♿ アクセシビリティ対応（WCAG 2.1準拠）

## 技術スタック

### バックエンド
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Gradle

### フロントエンド
- Thymeleaf
- HTML5/CSS3
- JavaScript (ES6+)
- レスポンシブデザイン

### テスト
- JUnit 5
- Mockito
- Spring Boot Test
- JaCoCo（カバレッジ82%）

### 外部API
- [Open-Meteo API](https://open-meteo.com/)

## セットアップ

### 前提条件

- Java 17以上
- MySQL 8.0以上
- Gradle 8.0以上

### インストール手順

1. リポジトリのクローン
git clone https://github.com/uwaa0927-boop/java-training-template.git
cd weather-app

2. データベース作成
CREATE DATABASE weather_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

3. 設定ファイル編集
`application.properties`を編集：

spring.datasource.url=jdbc:mysql://localhost:3306/weather_app
spring.datasource.username=your_username
spring.datasource.password=your_password

4. アプリケーション起動
./gradlew bootRun

5. ブラウザでアクセス
http://localhost:8080

## ディレクトリ構成

weather-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/weatherapp/
│   │   │       ├── client/          # 外部API連携
│   │   │       ├── config/          # 設定
│   │   │       ├── controller/      # コントローラー
│   │   │       ├── dto/             # データ転送オブジェクト
│   │   │       ├── entity/          # エンティティ
│   │   │       ├── exception/       # 例外
│   │   │       ├── mapper/          # マッパー
│   │   │       ├── repository/      # リポジトリ
│   │   │       ├── service/         # サービス
│   │   │       └── util/            # ユーティリティ
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/             # スタイルシート
│   │       │   ├── js/              # JavaScript
│   │       │   └── images/          # 画像
│   │       └── templates/           # Thymeleafテンプレート
│   └── test/                        # テストコード
├── docs/                            # ドキュメント
├── build.gradle                     # Gradle設定
└── README.md

## API仕様

### エンドポイント一覧

| メソッド | パス | 説明 |
|---------|------|------|
| GET | / | トップページ |
| GET | /weather/{id} | 都道府県別天気詳細 |
| GET | /?region={region} | 地域フィルタ |

## テスト

### テスト実行

./gradlew test

### カバレッジレポート

./gradlew jacocoTestReport

レポート：build/reports/jacoco/test/html/index.html

## デプロイ

### 本番ビルド

./gradlew clean build

### Jarファイル実行

java -jar build/libs/weather-app-0.0.1-SNAPSHOT.jar

## ライセンス

MIT License

## 作者

新入社員研修プロジェクト

## 謝辞

- Open-Meteo API
- Spring Boot
- Thymeleaf
