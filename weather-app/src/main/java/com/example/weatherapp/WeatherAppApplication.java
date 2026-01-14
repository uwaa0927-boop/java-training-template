package com.example.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//アプリのスタート地点で、Spring Bootを動かすのに必要な準備を自動でやってくれる
@SpringBootApplication
public class WeatherAppApplication {

	public static void main(String[] args) {
		//Spring Bootアプリを起動する
		SpringApplication.run(WeatherAppApplication.class, args);
	}

}