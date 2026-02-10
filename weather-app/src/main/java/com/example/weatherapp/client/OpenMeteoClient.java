package com.example.weatherapp.client;

import com.example.weatherapp.dto.api.OpenMeteoResponseDto;
import com.example.weatherapp.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class OpenMeteoClient {

    private static final String API_BASE_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestTemplate restTemplate;

    public OpenMeteoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenMeteoResponseDto fetchWeather(Double latitude, Double longitude) {
        String url = buildUrl(latitude, longitude);

        log.info("Open-Meteo API呼び出し: lat={}, lon={}", latitude, longitude);
        log.debug("URL: {}", url);

        try {
            OpenMeteoResponseDto response = restTemplate.getForObject(
                    url,
                    OpenMeteoResponseDto.class
            );

            if (response == null) {
                throw new ExternalApiException("APIレスポンスがnullです");
            }

            log.info("API呼び出し成功");
            return response;

        } catch (ResourceAccessException e) {
            log.error("APIタイムアウト: {}", e.getMessage());
            throw new ExternalApiException("天気APIへの接続がタイムアウトしました", e);

        } catch (HttpClientErrorException e) {
            log.error("APIクライアントエラー: status={}", e.getStatusCode());
            throw new ExternalApiException("天気APIエラー: " + e.getStatusCode(), e);

        } catch (HttpServerErrorException e) {
            log.error("APIサーバーエラー: status={}", e.getStatusCode());
            throw new ExternalApiException("天気APIサーバーエラー", e);

        } catch (Exception e) {
            log.error("API呼び出し失敗", e);
            throw new ExternalApiException("天気情報の取得に失敗しました", e);
        }
    }

    private String buildUrl(Double latitude, Double longitude) {
        return UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current",
                        "temperature_2m,weathercode,windspeed_10m,relativehumidity_2m," +
                                "apparent_temperature,precipitation,cloud_cover"
                )
                .queryParam("daily",
                        "temperature_2m_max,temperature_2m_min,weathercode," +
                                "precipitation_sum,windspeed_10m_max,sunrise,sunset"
                )
                .queryParam("timezone", "Asia/Tokyo")
                .queryParam("forecast_days", 7)
                .toUriString();
    }
}