package com.example.weatherapp.exception;

/**
 * ビジネスロジックエラーの基底例外
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}