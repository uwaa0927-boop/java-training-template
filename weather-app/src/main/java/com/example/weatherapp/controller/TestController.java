package com.example.weatherapp.controller;

import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.entity.TestMessage;
import com.example.weatherapp.repository.PrefectureRepository;
import com.example.weatherapp.service.TestMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PrefectureRepository prefectureRepository;

    @GetMapping("/prefectures")
    public List<Prefecture> getAllPrefectures() {
        return prefectureRepository.findAll();
    }

    @GetMapping("/prefectures/region/{region}")
    public List<Prefecture> getByRegion(@PathVariable String region) {
        return prefectureRepository.findByRegion(region);
    }
}
