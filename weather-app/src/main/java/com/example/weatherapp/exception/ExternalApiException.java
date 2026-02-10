package com.example.weatherapp.exception;

public class ExternalApiException extends RuntimeException {
    // 引数が2つ
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
    // 引数が1つ
    public ExternalApiException(String message) {
        super(message);
    }
}