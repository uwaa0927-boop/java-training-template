package com.example.weatherapp.controller;

import com.example.weatherapp.exception.ExternalApiException;
import com.example.weatherapp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/error")
public class ErrorTestController {

    @GetMapping("/404")
    public String test404() {
        throw new ResourceNotFoundException("テスト用404エラー");
    }

    @GetMapping("/500")
    public String test500() {
        throw new RuntimeException("テスト用500エラー");
    }

    @GetMapping("/api")
    public String testApi() {
        throw new ExternalApiException("テスト用APIエラー");
    }
}