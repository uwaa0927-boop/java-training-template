package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoadingDemoController {

    @GetMapping("/practice/loading-demo")
    public String loadingDemo() {
        return "practice/loading-demo";
    }
}