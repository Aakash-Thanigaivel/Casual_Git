import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Performance and load testing suite for ReactiveServiceApplication.
 * Tests application behavior under various load conditions and performance scenarios.
 * Designed for Bank of America performance testing standards.
 */
class ReactiveServiceApplicationPerformanceTest {

    private ReactiveServiceApplication application;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
        RouterFunction<ServerResponse> routes = application.routes();
        webTestClient = WebTestClient.bindToRouterFunction(routes)
            .configureClient()
            .responseTimeout(Duration.ofSeconds(30))
            .build();
    }

    @RepeatedTest(50)
    @DisplayName("Should maintain consistent performance across multiple executions")
    void testPerformanceConsistency() {
        long startTime = System.nanoTime();
        
        webTestClient.get()
            .uri("/performance-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, performance-test!");
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // Each request should complete within 100ms
        assertTrue(duration < 100_000_000, "Request should complete within 100ms");
    }

    @Test
    @DisplayName("Should handle high concurrent load efficiently")
    void testHighConcurrentLoad() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        // Submit 1000 concurrent requests
        for (int i = 0; i < 1000; i++) {
            final String name = "user" + i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                webTestClient.get()
                    .uri("/" + name)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo("hello, " + name + "!");
            }, executor);
            futures.add(future);
        }
        
        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        // 1000 concurrent requests should complete within 10 seconds
        assertTrue(totalDuration < 10000, 
                  "1000 concurrent requests should complete within 10 seconds");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500, 1000})
    @DisplayName("Should scale performance with increasing load")
    void testPerformanceScaling(int requestCount) {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < requestCount; i++) {
            webTestClient.get()
                .uri("/scale-test-" + i)
                .exchange()
                .expectStatus().isOk();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance should scale reasonably (less than 50ms per request on average)
        double avgTimePerRequest = (double) duration / requestCount;
        assertTrue(avgTimePerRequest < 50, 
                  "Average time per request should be less than 50ms");
    }

    @Test
    @DisplayName("Should maintain memory efficiency under load")
    void testMemoryEfficiencyUnderLoad() {
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection before test
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Generate significant load
        for (int i = 0; i < 10000; i++) {
            webTestClient.get()
                .uri("/memory-test-" + (i % 100))
                .exchange()
                .expectStatus().isOk();
        }
        
        // Force garbage collection after test
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be minimal (less than 50MB)
        assertTrue(memoryIncrease < 50 * 1024 * 1024, 
                  "Memory increase should be less than 50MB");
    }

    @Test
    @DisplayName("Should handle burst traffic patterns")
    void testBurstTrafficHandling() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(200);
        
        // Simulate burst traffic - 500 requests in quick succession
        List<CompletableFuture<Void>> burstFutures = new ArrayList<>();
        
        for (int i = 0; i < 500; i++) {
            final int requestId = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                webTestClient.get()
                    .uri("/burst-" + requestId)
                    .exchange()
                    .expectStatus().isOk();
            }, executor);
            burstFutures.add(future);
        }
        
        long startTime = System.currentTimeMillis();
        CompletableFuture.allOf(burstFutures.toArray(new CompletableFuture[0])).join();
        long burstDuration = System.currentTimeMillis() - startTime;
        
        // Wait a bit, then send another burst
        Thread.sleep(1000);
        
        List<CompletableFuture<Void>> secondBurstFutures = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            final int requestId = i + 500;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                webTestClient.get()
                    .uri("/burst-" + requestId)
                    .exchange()
                    .expectStatus().isOk();
            }, executor);
            secondBurstFutures.add(future);
        }
        
        startTime = System.currentTimeMillis();
        CompletableFuture.allOf(secondBurstFutures.toArray(new CompletableFuture[0])).join();
        long secondBurstDuration = System.currentTimeMillis() - startTime;
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        // Both bursts should complete within reasonable time
        assertTrue(burstDuration < 5000, "First burst should complete within 5 seconds");
        assertTrue(secondBurstDuration < 5000, "Second burst should complete within 5 seconds");
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1000",      // 1 thread, 1000 requests
        "10, 100",      // 10 threads, 100 requests each
        "50, 20",       // 50 threads, 20 requests each
        "100, 10"       // 100 threads, 10 requests each
    })
    @DisplayName("Should handle various thread pool configurations")
    void testThreadPoolConfigurations(int threadCount, int requestsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int r = 0; r < requestsPerThread; r++) {
                    webTestClient.get()
                        .uri("/thread-" + threadId + "-request-" + r)
                        .exchange()
                        .expectStatus().isOk();
                }
            }, executor);
            futures.add(future);
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        int totalRequests = threadCount * requestsPerThread;
        double avgTimePerRequest = (double) duration / totalRequests;
        
        // Average time should be reasonable regardless of thread configuration
        assertTrue(avgTimePerRequest < 100, 
                  "Average time per request should be less than 100ms");
    }

    @Test
    @DisplayName("Should maintain response time under sustained load")
    void testSustainedLoadResponseTime() {
        List<Long> responseTimes = new ArrayList<>();
        
        // Run sustained load for 30 seconds
        long testDuration = 30000; // 30 seconds
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < testDuration) {
            long requestStart = System.nanoTime();
            
            webTestClient.get()
                .uri("/sustained-load")
                .exchange()
                .expectStatus().isOk();
            
            long requestEnd = System.nanoTime();
            responseTimes.add((requestEnd - requestStart) / 1_000_000); // Convert to ms
        }
        
        // Calculate statistics
        double avgResponseTime = responseTimes.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);
        
        long maxResponseTime = responseTimes.stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        
        // Performance should remain consistent
        assertTrue(avgResponseTime < 50, "Average response time should be less than 50ms");
        assertTrue(maxResponseTime < 500, "Max response time should be less than 500ms");
    }

    @Test
    @DisplayName("Should handle CPU-intensive operations efficiently")
    void testCpuIntensiveOperations() {
        long startTime = System.currentTimeMillis();
        
        // Simulate CPU-intensive operations with complex name processing
        for (int i = 0; i < 1000; i++) {
            String complexName = "user-" + i + "-with-very-long-name-" + 
                               String.valueOf(i).repeat(10);
            
            webTestClient.get()
                .uri("/" + complexName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains(complexName)));
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should handle CPU-intensive operations within reasonable time
        assertTrue(duration < 10000, 
                  "CPU-intensive operations should complete within 10 seconds");
    }

    @Test
    @DisplayName("Should demonstrate reactive non-blocking behavior")
    void testReactiveNonBlockingBehavior() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        
        // Submit many requests simultaneously
        for (int i = 0; i < 1000; i++) {
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                long requestStart = System.nanoTime();
                
                webTestClient.get()
                    .uri("/non-blocking-test")
                    .exchange()
                    .expectStatus().isOk();
                
                return System.nanoTime() - requestStart;
            }, executor);
            futures.add(future);
        }
        
        // All requests should complete quickly due to non-blocking nature
        List<Long> durations = new ArrayList<>();
        for (CompletableFuture<Long> future : futures) {
            durations.add(future.join());
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        double avgDuration = durations.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0) / 1_000_000; // Convert to ms
        
        // Non-blocking should result in fast average response times
        assertTrue(avgDuration < 100, 
                  "Non-blocking requests should have fast average response time");
    }

    @Test
    @DisplayName("Should verify performance under different payload sizes")
    void testPerformanceWithDifferentPayloadSizes() {
        String[] payloadSizes = {
            "small",
            "medium-sized-payload-with-more-content",
            "large-payload-" + "x".repeat(1000),
            "extra-large-payload-" + "y".repeat(10000)
        };
        
        for (String payload : payloadSizes) {
            long startTime = System.nanoTime();
            
            webTestClient.get()
                .uri("/" + payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains(payload)));
            
            long duration = (System.nanoTime() - startTime) / 1_000_000; // Convert to ms
            
            // Response time should be reasonable regardless of payload size
            assertTrue(duration < 200, 
                      "Response time should be less than 200ms for payload: " + payload.length());
        }
    }
}