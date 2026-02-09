package com.example.weatherapp.service;

import com.example.weatherapp.dto.WeatherDetailDto;

public interface WeatherService {

    WeatherDetailDto getWeatherByPrefectureId(Long prefectureId);

    WeatherDetailDto getLatestWeatherFromDb(Long prefectureId);
}
