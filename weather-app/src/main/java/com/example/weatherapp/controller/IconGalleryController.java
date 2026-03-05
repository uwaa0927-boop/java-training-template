package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IconGalleryController {
    @GetMapping("/practice/icon-gallery")
    public String iconGallery() {
        return "practice/icon-gallery";
    }
}
