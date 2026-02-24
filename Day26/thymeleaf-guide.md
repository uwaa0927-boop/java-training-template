# Thymeleaf 基本ガイド

## 1. テキスト表示（th:text）

<!-- Controller -->
model.addAttribute("username", "太郎");

<!-- HTML -->
<p th:text="${username}">ダミーテキスト</p>

<!-- 結果 -->
<p>太郎</p>

HTMLエスケープ
- th:text: エスケープする（安全）
- th:utext: エスケープしない（XSS注意）


## 2. 繰り返し（th:each）

<!-- Controller -->
List<String> fruits = List.of("りんご", "バナナ", "みかん");
model.addAttribute("fruits", fruits);

<!-- HTML -->
<ul>
    <li th:each="fruit : ${fruits}" th:text="${fruit}"></li>
</ul>

<!-- 結果 -->
<ul>
    <li>りんご</li>
    <li>バナナ</li>
    <li>みかん</li>
</ul>

繰り返しステータス：
<li th:each="item, stat : ${items}">
    インデックス: <span th:text="${stat.index}"></span>
    カウント: <span th:text="${stat.count}"></span>
    最初?: <span th:text="${stat.first}"></span>
    最後?: <span th:text="${stat.last}"></span>
</li>


## 3. 条件分岐（th:if / th:unless）

<!-- Controller -->
model.addAttribute("isLoggedIn", true);

<!-- HTML -->
<div th:if="${isLoggedIn}">
    ようこそ、ログイン中です
</div>

<div th:unless="${isLoggedIn}">
    ログインしてください
</div>

th:switchによる複数分岐:
<div th:switch="${user.role}">
    <p th:case="'admin'">管理者</p>
    <p th:case="'user'">一般ユーザー</p>
    <p th:case="*">ゲスト</p>
</div>


## 4. URL式（@{...}）

<!-- 絶対パス -->
<a th:href="@{/weather/13}">東京の天気</a>

<!-- パラメータ付き -->
<a th:href="@{/search(keyword='天気')}">検索</a>
<!-- 結果: /search?keyword=天気 -->

<!-- パスパラメータ -->
<a th:href="@{/weather/{id}(id=${prefecture.id})}">詳細</a>
<!-- 結果: /weather/13 -->


## 5. オブジェクトのプロパティアクセス

<!-- Controller -->
Prefecture prefecture = new Prefecture();
prefecture.setName("東京都");
model.addAttribute("pref", prefecture);

<!-- HTML -->
<p th:text="${pref.name}">都道府県名</p>
<!-- または -->
<p th:text="${pref.getName()}">都道府県名</p>


## 6. 日付フォーマット（#temporals）

<!-- Controller -->
model.addAttribute("now", LocalDateTime.now());

<!-- HTML -->
<p th:text="${#temporals.format(now, 'yyyy/MM/dd HH:mm')}"></p>
<!-- 結果: 2024/01/05 15:30 -->


## 7. 数値フォーマット（#numbers）

<!-- Controller -->
model.addAttribute("temperature", 15.678);

<!-- HTML -->
<p th:text="${#numbers.formatDecimal(temperature, 1, 1)}"></p>
<!-- 結果: 15.7 -->


## 8. フラグメント（共通部品）

header.html:
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="head(title)">
    <meta charset="UTF-8">
    <title th:text="${title}">タイトル</title>
</head>
</html>

使用側:
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{header :: head('トップページ')}"></head>
<body>
    コンテンツ
</body>
</html>
