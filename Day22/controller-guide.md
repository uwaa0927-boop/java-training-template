# Spring MVC Controller 実装ガイド

## 基本的なController

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello World");
        return "index";  // templates/index.html を表示
    }
}


## Modelの使い方

Modelはビューにデータを渡すための入れ物。

@GetMapping("/users")
public String listUsers(Model model) {
    List<User> users = userService.findAll();
    model.addAttribute("users", users);
    model.addAttribute("count", users.size());
    return "users/list";
}

Thymeleafで受け取る:

<p>ユーザー数: <span th:text="${count}"></span></p>
<ul>
    <li th:each="user : ${users}" th:text="${user.name}"></li>
</ul>


## RequestParamの使い方

@GetMapping("/search")
public String search(
    @RequestParam String keyword,           // 必須パラメータ
    @RequestParam(required = false) String category,  // 任意
    @RequestParam(defaultValue = "0") int page,       // デフォルト値
    Model model
) {
    // ?keyword=Java&category=tech&page=1
    return "search";
}


## PathVariableの使い方

@GetMapping("/users/{id}")
public String showUser(
    @PathVariable Long id,
    Model model
) {
    User user = userService.findById(id);
    model.addAttribute("user", user);
    return "users/detail";
}


## リダイレクト

@PostMapping("/users/create")
public String createUser(@ModelAttribute User user) {
    userService.save(user);
    return "redirect:/users";  // リダイレクト
}
