package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsiveTestController {

    @GetMapping("/practice/responsive-test")
    public String responsiveTest() {
        return "practice/responsive-test";
    }
}