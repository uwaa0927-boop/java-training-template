# RestTemplate 設定方針

## RestTemplateとは

Spring Frameworkが提供するHTTPクライアント
- RESTful APIの呼び出し
- JSON↔Javaオブジェクトの変換
- エラーハンドリング

## 設定クラスの作成

### RestTemplateConfig.java
```java
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(5))  // 接続タイムアウト
            .setReadTimeout(Duration.ofSeconds(5))     // 読み取りタイムアウト
            .build();
    }
}


## OpenMeteoClientの設計

### 責務
- Open-Meteo APIとの通信
- レスポンスのDTOへの変換
- エラーハンドリング

### 実装イメージ
```java
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
        
        log.info("Fetching weather from Open-Meteo API: lat={}, lon={}", latitude, longitude);
        
        try {
            OpenMeteoResponseDto response = restTemplate.getForObject(url, OpenMeteoResponseDto.class);
            log.info("Successfully fetched weather data");
            return response;
            
        } catch (HttpClientErrorException e) {
            log.error("Client error when calling API: {}", e.getMessage());
            throw new ExternalApiException("Failed to fetch weather data", e);
            
        } catch (HttpServerErrorException e) {
            log.error("Server error when calling API: {}", e.getMessage());
            throw new ExternalApiException("Weather API server error", e);
            
        } catch (Exception e) {
            log.error("Unexpected error when calling API: {}", e.getMessage());
            throw new ExternalApiException("Unexpected error", e);
        }
    }
    
    private String buildUrl(Double latitude, Double longitude) {
        return UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
            .queryParam("latitude", latitude)
            .queryParam("longitude", longitude)
            .queryParam("current", "temperature_2m,weathercode,windspeed_10m,relativehumidity_2m")
            .queryParam("daily", "temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum")
            .queryParam("timezone", "Asia/Tokyo")
            .queryParam("forecast_days", 7)
            .toUriString();
    }
}


## エラーハンドリング

### カスタム例外
```java
public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

### GlobalExceptionHandlerでの処理
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ExternalApiException.class)
    public String handleExternalApiException(ExternalApiException e, Model model) {
        model.addAttribute("error", "天気情報の取得に失敗しました");
        return "error/api-error";
    }
}


## テスト方針

### ユニットテスト
- MockRestTemplateを使用
- 正常系・異常系のテスト

```java
@ExtendWith(MockitoExtension.class)
class OpenMeteoClientTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private OpenMeteoClient openMeteoClient;
    
    @Test
    void fetchWeather_Success() {
        // Given
        OpenMeteoResponseDto mockResponse = new OpenMeteoResponseDto();
        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponseDto.class)))
            .thenReturn(mockResponse);
        
        // When
        OpenMeteoResponseDto result = openMeteoClient.fetchWeather(35.6895, 139.6917);
        
        // Then
        assertNotNull(result);
    }
    
    @Test
    void fetchWeather_ThrowsException() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponseDto.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // When & Then
        assertThrows(ExternalApiException.class, () -> {
            openMeteoClient.fetchWeather(200.0, 139.6917);
        });
    }
}
