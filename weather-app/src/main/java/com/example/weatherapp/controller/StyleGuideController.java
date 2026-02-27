package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StyleGuideController {

    @GetMapping("/practice/style-guide")
    public String styleGuide() {
        return "practice/style-guide";
    }
}