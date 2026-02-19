package com.example.weatherapp.exception;

import com.example.weatherapp.controller.WeatherController;
import com.example.weatherapp.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GlobalExceptionHandlerのテスト
 */
@WebMvcTest(WeatherController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    @DisplayName("ResourceNotFoundExceptionが404エラーページを表示する")
    void testResourceNotFoundException() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(999L))
                .thenThrow(new ResourceNotFoundException("都道府県が見つかりません: id=999"));

        // When & Then
        mockMvc.perform(get("/weather/999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("statusCode", 404));
    }

    @Test
    @DisplayName("ExternalApiExceptionがAPIエラーページを表示する")
    void testExternalApiException() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(13L))
                .thenThrow(new ExternalApiException("API呼び出し失敗"));

        // When & Then
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(view().name("error/api-error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("statusCode", 503))
                .andExpect(model().attribute("prefectureId", "13"))
                .andExpect(model().attribute("retryUrl", "/weather/13"));
    }

    @Test
    @DisplayName("予期しない例外が500エラーページを表示する")
    void testUnexpectedException() throws Exception {
        // Given
        when(weatherService.getWeatherByPrefectureId(13L))
                .thenThrow(new RuntimeException("予期しないエラー"));

        // When & Then
        mockMvc.perform(get("/weather/13"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("statusCode", 500));
    }
}