# Thymeleaf練習問題

## 問題1: テキスト表示

以下のデータをHTMLに表示してください。

**Controller:**
model.addAttribute("productName", "Spring Boot入門");
model.addAttribute("price", 3000);

**期待される表示：**
商品名: Spring Boot入門
価格: 3,000円

**解答欄**
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>問題1</title>
</head>
<body>

<p>商品名: <span th:text="${productName}">商品名</span></p>
<p>価格: <span th:text="${#numbers.formatInteger(price, 3, 'COMMA')}"></span>円</p>

</body>
</html>


## 問題2: リスト表示

以下のリストをテーブルで表示してください。

**Controller:**
List<Book> books = Arrays.asList(
    new Book("Java入門", 2500),
    new Book("Spring実践", 3500),
    new Book("Thymeleaf基礎", 2000)
);
model.addAttribute("books", books);

**期待されるHTML:**
| 書籍名 | 価格 |
|--------|------|
| Java入門 | 2,500円 |
| Spring実践 | 3,500円 |
| Thymeleaf基礎 | 2,000円 |

**解答欄**
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>問題2</title>
</head>
<body>
    <table>
        <thead>
        <tr>
            <th>書籍名</th>
            <th>価格</th>
        </tr>
        </thead>

        <tbody>
        <tr th:each="book : ${books}">
            <td th:text="${book.name}">書籍名</td>
            <td>
                <span th:text="${#numbers.formatInteger(book.price, 3, 'COMMA')}"></span>円
            </td>
        </tr>
        </tbody>
    </table>
</body>
</html>


## 問題3: 条件分岐

在庫数に応じてメッセージを表示してください。

**条件:**
- 在庫 > 10: 在庫あり（緑）
- 在庫 1-10: 残りわずか（オレンジ）
- 在庫 = 0: 売り切れ（赤）

**解答欄**
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>問題3</title>
</head>
<body>
    <p th:if="${stock > 10}" style="color: green;">在庫あり</p>

    <p th:if="${stock >= 1 and stock <= 10}" style="color: orange;">残りわずか</p>

    <p th:if="${stock == 0}" style="color: red;">売り切れ</p>
</body>
</html>


## 問題4: 日付フォーマット

現在日時を以下の形式で表示してください。

- 形式1: 2024年1月5日 15時30分
- 形式2: 2024/01/05
- 形式3: 15:30

**解答欄**
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>問題4</title>
</head>
<body>
    <p>形式1：<span th:text="${#temporals.format(now, 'yyyy年M月d日 H時mm分')}"></span></p>

    <p>形式2：<span th:text="${#temporals.format(now, 'yyyy/MM/dd')}"></span></p>

    <p>形式3：<span th:text="${#temporals.format(now, 'HH:mm')}"></span></p>
</body>
</html>


## 問題5: URLリンク

以下のリンクを作成してください。

1.トップページへのリンク
2.都道府県ID=13の天気ページへのリンク
3.地域=関東でフィルタするリンク

**解答欄**
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>問題5</title>
</head>
<body>
    <p><a th:href="@{/}">トップページ</a></p>

    <p><a th:href="@{/weather/13}">東京の天気</a></p>

    <p><a th:href="@{/weather(region='関東')}">関東の天気</a></p>
</body>
</html>
