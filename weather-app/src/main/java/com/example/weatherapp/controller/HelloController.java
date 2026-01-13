package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HelloController {

    @GetMapping("/")
    public String hello(Model model) {
        //現在の日時を取得して(yyyy...)の形式に変換している
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        //コントローラー（HelloController.java）からデータをHTMLに渡す準備
        model.addAttribute("currentTime", currentTime);

        //hello.htmlを表示する
        return "hello";
    }
}