package com.example.weatherapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        log.info("Hello endpoint accessed");

        //現在の日時を取得して(yyyy...)の形式に変換している
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        log.debug("Current time: {}", currentTime);

        //コントローラー（HelloController.java）からデータをHTMLに渡す準備
        model.addAttribute("currentTime", currentTime);

        log.info("Returning hello view");

        //hello.htmlを表示する
        return "hello";
    }
}