package com.example.weatherapp.client;

import com.example.weatherapp.dto.api.OpenMeteoResponseDto;
import com.example.weatherapp.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class OpenMeteoClientTest {

    private OpenMeteoClient openMeteoClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        openMeteoClient = new OpenMeteoClient(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("API呼び出しが成功する")
    void testFetchWeather_Success() {
        String jsonResponse = "{\"latitude\":35.689,\"longitude\":139.692}";

        mockServer.expect(requestTo(containsString("api.open-meteo.com")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("latitude", "35.689"))
                .andExpect(queryParam("longitude", "139.692"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        OpenMeteoResponseDto result = openMeteoClient.fetchWeather(35.689, 139.692);

        assertThat(result).isNotNull();
        mockServer.verify();
    }

    @Test
    @DisplayName("タイムアウトエラー")
    void testFetchWeather_Timeout() {
        mockServer.expect(requestTo(containsString("api.open-meteo.com")))
                .andRespond(withServerError());

        assertThatThrownBy(() -> openMeteoClient.fetchWeather(35.689, 139.692))
                .isInstanceOf(ExternalApiException.class);

        mockServer.verify();
    }
}