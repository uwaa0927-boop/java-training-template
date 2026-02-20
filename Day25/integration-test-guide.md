# Spring Boot 統合テストガイド

## @SpringBootTestの使い方

アプリケーション全体を起動してテスト。

@SpringBootTest
@Transactional  // テスト後に自動ロールバック
class MyIntegrationTest {
    
    @Autowired
    private MyService service;
    
    @Test
    void testServiceMethod() {
        // 実際のDBを使用
        Result result = service.doSomething();
        assertThat(result).isNotNull();
    }
}

## テストデータの準備

### 1. @SQL アノテーション

@SpringBootTest
@Sql("/test-data.sql")  // テスト前に実行
class MyTest {
    // テストメソッド
}

### 2. @beforeeachでセットアップ

@BeforeEach
void setUp() {
    Prefecture tokyo = Prefecture.builder()
        .name("東京都")
        .build();
    prefectureRepository.save(tokyo);
}

## @transactionalの役割

テストメソッド終了後、自動的にロールバック。

@SpringBootTest
@Transactional  // 重要！
class MyTest {
    
    @Test
    void testInsert() {
        // データを挿入
        repository.save(entity);
        
        // テスト終了後、自動ロールバック
        // DBは元の状態に戻る
    }
}

## テストの階層化

// 1. Repository層
@DataJpaTest
class PrefectureRepositoryTest { }

// 2. Service層（Repositoryをモック）
@ExtendWith(MockitoExtension.class)
class PrefectureServiceTest { }

// 3. 統合テスト（全体）
@SpringBootTest
class PrefectureIntegrationTest { }

## MockMvcとRestAssured

### MockMvc（コントローラーテスト）

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void test() throws Exception {
        mockMvc.perform(get("/api/data"))
            .andExpect(status().isOk());
    }
}

### RestAssured（REST API テスト）

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApiTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    void test() {
        given()
            .port(port)
        .when()
            .get("/api/data")
        .then()
            .statusCode(200);
    }
}
