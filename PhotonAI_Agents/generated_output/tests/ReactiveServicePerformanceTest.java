import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance and load testing for the modernized Spring 6.1 reactive application
 * Bank of America Legacy Code Modernization Project
 * Validates performance requirements and scalability
 */
@SpringBootTest(
    classes = com.bankofamerica.modernized.ReactiveServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
    "server.port=0",
    "spring.main.web-application-type=reactive",
    "server.netty.connection-timeout=30s",
    "spring.webflux.multipart.max-in-memory-size=1MB"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
@DisplayName("Performance and Load Tests - Spring 6.1 & JDK 17")
class ReactiveServicePerformanceTest {

    @Autowired
    private WebTestClient webTestClient;

    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(20);
        
        // Configure WebTestClient for performance testing
        webTestClient = webTestClient.mutate()
            .responseTimeout(Duration.ofSeconds(30))
            .build();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        if (executorService != null) {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    @Nested
    @DisplayName("Load Testing")
    class LoadTesting {

        @Test
        @DisplayName("Should handle 500 concurrent requests efficiently")
        void shouldHandle500ConcurrentRequestsEfficiently() throws Exception {
            var numberOfRequests = 500;
            var futures = new CompletableFuture[numberOfRequests];
            var startTime = System.currentTimeMillis();

            try {
                for (int i = 0; i < numberOfRequests; i++) {
                    final var requestId = i;
                    futures[i] = CompletableFuture.supplyAsync(() -> {
                        return webTestClient
                            .get()
                            .uri("/load-test-" + requestId)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(String.class)
                            .getResponseBody()
                            .blockFirst(Duration.ofSeconds(10));
                    }, executorService);
                }

                // Wait for all requests to complete
                CompletableFuture.allOf(futures).get(60, TimeUnit.SECONDS);

                var endTime = System.currentTimeMillis();
                var totalTime = endTime - startTime;

                // Should handle 500 requests in under 30 seconds
                assertTrue(totalTime < 30000,
                    "Should handle " + numberOfRequests + " requests in under 30 seconds, took: " + totalTime + "ms");

                // Verify all responses are correct
                for (var future : futures) {
                    var response = future.get();
                    assertNotNull(response);
                    assertTrue(response.equals("fallback") || response.startsWith("hello,"));
                }

            } finally {
                // Cleanup is handled in @AfterEach
            }
        }

        @Test
        @DisplayName("Should maintain throughput under sustained load")
        void shouldMaintainThroughputUnderSustainedLoad() {
            var requestsPerSecond = 50;
            var durationSeconds = 10;
            var totalRequests = requestsPerSecond * durationSeconds;
            var successfulRequests = new java.util.concurrent.atomic.AtomicInteger(0);

            var startTime = System.currentTimeMillis();

            // Create a flux that emits requests at a controlled rate
            var requestFlux = Flux.interval(Duration.ofMillis(1000 / requestsPerSecond))
                .take(totalRequests)
                .flatMap(i -> {
                    return webTestClient
                        .get()
                        .uri("/sustained-load-" + i)
                        .exchange()
                        .doOnNext(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                successfulRequests.incrementAndGet();
                            }
                        })
                        .then();
                }, 10); // Limit concurrency

            StepVerifier.create(requestFlux)
                .verifyComplete();

            var endTime = System.currentTimeMillis();
            var actualDuration = endTime - startTime;

            // Verify that most requests were successful
            var successRate = (double) successfulRequests.get() / totalRequests;
            assertTrue(successRate > 0.95, 
                "Success rate should be > 95%, was: " + (successRate * 100) + "%");

            // Verify timing is reasonable
            assertTrue(actualDuration < (durationSeconds + 5) * 1000,
                "Test should complete within expected timeframe");
        }
    }

    @Nested
    @DisplayName("Stress Testing")
    class StressTesting {

        @Test
        @DisplayName("Should handle burst traffic gracefully")
        void shouldHandleBurstTrafficGracefully() {
            var burstSize = 100;
            var numberOfBursts = 5;
            var successfulRequests = new java.util.concurrent.atomic.AtomicInteger(0);

            for (int burst = 0; burst < numberOfBursts; burst++) {
                var burstRequests = IntStream.range(0, burstSize)
                    .parallel()
                    .mapToObj(i -> webTestClient
                        .get()
                        .uri("/burst-" + burst + "-" + i)
                        .exchange()
                        .doOnNext(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                successfulRequests.incrementAndGet();
                            }
                        })
                        .then())
                    .toArray(Mono[]::new);

                // Execute burst and wait for completion
                StepVerifier.create(Mono.when(burstRequests))
                    .verifyComplete();

                // Small delay between bursts
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            var totalExpectedRequests = burstSize * numberOfBursts;
            var successRate = (double) successfulRequests.get() / totalExpectedRequests;
            
            assertTrue(successRate > 0.90,
                "Success rate should be > 90% under burst traffic, was: " + (successRate * 100) + "%");
        }

        @Test
        @DisplayName("Should recover from overload conditions")
        void shouldRecoverFromOverloadConditions() {
            // Create overload condition
            var overloadRequests = IntStream.range(0, 200)
                .parallel()
                .mapToObj(i -> webTestClient
                    .get()
                    .uri("/overload-" + i)
                    .exchange()
                    .onErrorResume(throwable -> Mono.empty())
                    .then())
                .toArray(Mono[]::new);

            // Execute overload
            StepVerifier.create(Mono.when(overloadRequests))
                .verifyComplete();

            // Wait for recovery
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Verify system has recovered
            webTestClient
                .get()
                .uri("/recovery-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }
    }

    @Nested
    @DisplayName("Memory and Resource Testing")
    class MemoryAndResourceTesting {

        @Test
        @DisplayName("Should maintain stable memory usage under load")
        void shouldMaintainStableMemoryUsageUnderLoad() {
            var runtime = Runtime.getRuntime();
            
            // Force garbage collection and get baseline
            System.gc();
            var initialMemory = runtime.totalMemory() - runtime.freeMemory();

            // Execute memory-intensive operations
            var memoryTestRequests = IntStream.range(0, 1000)
                .mapToObj(i -> webTestClient
                    .get()
                    .uri("/memory-test-" + i)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(String.class)
                    .getResponseBody()
                    .then())
                .toArray(Mono[]::new);

            StepVerifier.create(Mono.when(memoryTestRequests))
                .verifyComplete();

            // Force garbage collection and measure final memory
            System.gc();
            var finalMemory = runtime.totalMemory() - runtime.freeMemory();
            var memoryIncrease = finalMemory - initialMemory;

            // Memory increase should be reasonable (under 50MB)
            assertTrue(memoryIncrease < 50_000_000,
                "Memory increase should be under 50MB, was: " + (memoryIncrease / 1_000_000) + "MB");
        }

        @Test
        @DisplayName("Should handle resource cleanup properly")
        void shouldHandleResourceCleanupProperly() {
            var threadCountBefore = Thread.activeCount();

            // Execute operations that might create threads/resources
            var resourceTestRequests = IntStream.range(0, 100)
                .mapToObj(i -> webTestClient
                    .get()
                    .uri("/resource-test-" + i)
                    .exchange()
                    .expectStatus().isOk()
                    .then())
                .toArray(Mono[]::new);

            StepVerifier.create(Mono.when(resourceTestRequests))
                .verifyComplete();

            // Allow time for cleanup
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            var threadCountAfter = Thread.activeCount();
            var threadIncrease = threadCountAfter - threadCountBefore;

            // Thread count should not increase significantly
            assertTrue(threadIncrease < 20,
                "Thread count increase should be minimal, was: " + threadIncrease);
        }
    }

    @Nested
    @DisplayName("Latency Testing")
    class LatencyTesting {

        @Test
        @DisplayName("Should maintain low latency under normal load")
        void shouldMaintainLowLatencyUnderNormalLoad() {
            var numberOfTests = 100;
            var latencies = new long[numberOfTests];

            for (int i = 0; i < numberOfTests; i++) {
                var startTime = System.nanoTime();

                webTestClient
                    .get()
                    .uri("/latency-test-" + i)
                    .exchange()
                    .expectStatus().isOk();

                var endTime = System.nanoTime();
                latencies[i] = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            }

            // Calculate statistics
            var averageLatency = java.util.Arrays.stream(latencies).average().orElse(0.0);
            var maxLatency = java.util.Arrays.stream(latencies).max().orElse(0L);
            var p95Latency = calculatePercentile(latencies, 95);

            // Assertions
            assertTrue(averageLatency < 10, 
                "Average latency should be under 10ms, was: " + averageLatency + "ms");
            assertTrue(maxLatency < 100, 
                "Max latency should be under 100ms, was: " + maxLatency + "ms");
            assertTrue(p95Latency < 20, 
                "95th percentile latency should be under 20ms, was: " + p95Latency + "ms");
        }

        private long calculatePercentile(long[] values, int percentile) {
            var sorted = java.util.Arrays.stream(values).sorted().toArray();
            var index = (int) Math.ceil(percentile / 100.0 * sorted.length) - 1;
            return sorted[Math.max(0, Math.min(index, sorted.length - 1))];
        }
    }

    @Nested
    @DisplayName("Scalability Testing")
    class ScalabilityTesting {

        @Test
        @DisplayName("Should scale linearly with increased load")
        void shouldScaleLinearlyWithIncreasedLoad() {
            var loadLevels = new int[]{10, 50, 100, 200};
            var throughputResults = new double[loadLevels.length];

            for (int i = 0; i < loadLevels.length; i++) {
                var load = loadLevels[i];
                var startTime = System.currentTimeMillis();

                var requests = IntStream.range(0, load)
                    .mapToObj(j -> webTestClient
                        .get()
                        .uri("/scale-test-" + load + "-" + j)
                        .exchange()
                        .expectStatus().isOk()
                        .then())
                    .toArray(Mono[]::new);

                StepVerifier.create(Mono.when(requests))
                    .verifyComplete();

                var endTime = System.currentTimeMillis();
                var duration = (endTime - startTime) / 1000.0; // Convert to seconds
                throughputResults[i] = load / duration; // Requests per second
            }

            // Verify that throughput doesn't degrade significantly
            for (int i = 1; i < throughputResults.length; i++) {
                var degradation = (throughputResults[0] - throughputResults[i]) / throughputResults[0];
                assertTrue(degradation < 0.5, 
                    "Throughput degradation should be less than 50% at load level " + loadLevels[i] + 
                    ", was: " + (degradation * 100) + "%");
            }
        }
    }
}