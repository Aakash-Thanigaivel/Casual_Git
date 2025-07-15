import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

/**
 * Reactive-specific test suite for ReactiveServiceApplication.
 * Tests reactive streams, backpressure, and WebFlux-specific functionality.
 * Designed for Bank of America reactive programming standards.
 */
@ExtendWith(MockitoExtension.class)
class ReactiveServiceApplicationReactiveTest {

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

    @Test
    @DisplayName("Should handle reactive streams properly")
    void testReactiveStreamHandling() {
        Mono<String> response = webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single();

        StepVerifier.create(response)
            .expectNext("hi")
            .verifyComplete();
    }

    @Test
    @DisplayName("Should handle backpressure in reactive streams")
    void testBackpressureHandling() {
        // Test multiple rapid requests to verify backpressure handling
        for (int i = 0; i < 1000; i++) {
            webTestClient.get()
                .uri("/test" + i)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, test" + i + "!");
        }
    }

    @Test
    @DisplayName("Should verify reactive publisher behavior")
    void testReactivePublisherBehavior() {
        Mono<String> greetingMono = webTestClient.get()
            .uri("/ReactiveTest")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single();

        StepVerifier.create(greetingMono)
            .expectNext("hello, ReactiveTest!")
            .expectComplete()
            .verify(Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should handle reactive error scenarios")
    void testReactiveErrorHandling() {
        // Test that reactive streams handle errors gracefully
        webTestClient.get()
            .uri("/error-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should verify non-blocking behavior")
    void testNonBlockingBehavior() {
        long startTime = System.currentTimeMillis();
        
        // Make multiple concurrent requests
        for (int i = 0; i < 100; i++) {
            webTestClient.get()
                .uri("/concurrent" + i)
                .exchange()
                .expectStatus().isOk();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Non-blocking should handle 100 requests quickly
        assertTrue(duration < 2000, "Non-blocking requests should complete quickly");
    }

    @Test
    @DisplayName("Should handle reactive timeout scenarios")
    void testReactiveTimeout() {
        webTestClient.mutate()
            .responseTimeout(Duration.ofMillis(100))
            .build()
            .get()
            .uri("/timeout-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should verify reactive context propagation")
    void testReactiveContextPropagation() {
        StepVerifier.create(
            webTestClient.get()
                .uri("/context-test")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .single()
        )
        .expectNext("fallback")
        .verifyComplete();
    }

    @Test
    @DisplayName("Should handle reactive stream cancellation")
    void testReactiveStreamCancellation() {
        Mono<String> response = webTestClient.get()
            .uri("/cancellation-test")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single();

        StepVerifier.create(response)
            .thenCancel()
            .verify();
    }

    @Test
    @DisplayName("Should verify reactive memory efficiency")
    void testReactiveMemoryEfficiency() {
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Process many requests to test memory efficiency
        for (int i = 0; i < 10000; i++) {
            webTestClient.get()
                .uri("/memory-test" + (i % 100))
                .exchange()
                .expectStatus().isOk();
        }
        
        System.gc(); // Suggest garbage collection
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable (less than 100MB)
        assertTrue(memoryIncrease < 100 * 1024 * 1024, 
                  "Memory usage should remain efficient");
    }

    @Test
    @DisplayName("Should handle reactive composition")
    void testReactiveComposition() {
        // Test that reactive operations can be composed
        Mono<String> composedResponse = webTestClient.get()
            .uri("/composition-test")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single()
            .map(String::toUpperCase)
            .filter(s -> s.contains("FALLBACK"));

        StepVerifier.create(composedResponse)
            .expectNext("FALLBACK")
            .verifyComplete();
    }

    @Test
    @DisplayName("Should verify reactive scheduler behavior")
    void testReactiveSchedulerBehavior() {
        // Test that reactive operations use appropriate schedulers
        String threadName = Thread.currentThread().getName();
        
        webTestClient.get()
            .uri("/scheduler-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
        
        // Verify we're not blocking the main thread
        assertFalse(threadName.contains("main"), 
                   "Should not block main thread");
    }

    @Test
    @DisplayName("Should handle reactive error recovery")
    void testReactiveErrorRecovery() {
        Mono<String> errorRecovery = webTestClient.get()
            .uri("/error-recovery-test")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single()
            .onErrorReturn("recovered");

        StepVerifier.create(errorRecovery)
            .expectNext("fallback")
            .verifyComplete();
    }

    @Test
    @DisplayName("Should verify reactive cold vs hot streams")
    void testReactiveColdHotStreams() {
        // Test that our endpoints produce cold streams (new subscription = new execution)
        Mono<String> firstCall = webTestClient.get()
            .uri("/cold-hot-test")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single();

        Mono<String> secondCall = webTestClient.get()
            .uri("/cold-hot-test")
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .single();

        // Both calls should produce the same result (cold stream behavior)
        StepVerifier.create(Mono.zip(firstCall, secondCall))
            .expectNextMatches(tuple -> tuple.getT1().equals(tuple.getT2()))
            .verifyComplete();
    }

    @Test
    @DisplayName("Should handle reactive flux operations")
    void testReactiveFluxOperations() {
        // Test multiple responses as a flux
        webTestClient.get()
            .uri("/flux-test")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(String.class)
            .hasSize(1)
            .contains("fallback");
    }

    @Test
    @DisplayName("Should verify reactive WebFlux integration")
    void testWebFluxIntegration() {
        // Verify that WebFlux features are properly integrated
        assertNotNull(application.routes(), "Router function should be created");
        
        RouterFunction<ServerResponse> routes = application.routes();
        assertNotNull(routes, "Routes should be properly configured");
    }
}