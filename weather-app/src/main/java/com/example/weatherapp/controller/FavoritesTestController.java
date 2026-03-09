package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FavoritesTestController {

    @GetMapping("/practice/favorites-test")
    public String favoritesTest() {
        return "practice/favorites-test";
    }
}