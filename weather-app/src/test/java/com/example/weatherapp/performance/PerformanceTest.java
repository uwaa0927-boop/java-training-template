package com.example.weatherapp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * パフォーマンステスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("パフォーマンステスト")
class PerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("並行リクエスト処理性能")
    void testConcurrentRequests() throws Exception {
        int threadCount = 100;
        int requestsPerThread = 10;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount * requestsPerThread);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < requestsPerThread; j++) {
                    try {
                        mockMvc.perform(get("/"));
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        errorCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("総リクエスト数: " + (threadCount * requestsPerThread));
        System.out.println("成功: " + successCount.get());
        System.out.println("失敗: " + errorCount.get());
        System.out.println("所要時間: " + duration + "ms");
        System.out.println("平均応答時間: " + (duration / (threadCount * requestsPerThread)) + "ms");

        // 成功率95%以上を期待
        double successRate = (double) successCount.get() / (threadCount * requestsPerThread);
        assertThat(successRate).isGreaterThan(0.95);
    }
}