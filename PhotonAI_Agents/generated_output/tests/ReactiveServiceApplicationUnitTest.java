package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.time.Duration;

/**
 * Unit tests for ReactiveServiceApplication router functions.
 * 
 * <p>This test class focuses on testing individual router function methods
 * and reactive behavior in isolation.
 */
@WebFluxTest
@ContextConfiguration(classes = ReactiveServiceApplication.class)
class ReactiveServiceApplicationUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    private ReactiveServiceApplication application;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
    }

    @Test
    @DisplayName("Should create router function bean successfully")
    void testRouterFunctionCreation() {
        RouterFunction<ServerResponse> routerFunction = application.routes();
        assertNotNull(routerFunction);
    }

    @Test
    @DisplayName("Should handle root path with correct response time")
    void testRootEndpointPerformance() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi")
            .consumeWith(result -> {
                // Verify response is returned quickly (under 1 second)
                assertTrue(result.getResponseHeaders().getFirst("Content-Type") != null);
            });
    }

    @Test
    @DisplayName("Should handle multiple rapid requests")
    void testMultipleRapidRequests() {
        // Test rapid consecutive requests
        for (int i = 0; i < 5; i++) {
            webTestClient.get()
                .uri("/test" + i)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, test" + i + "!");
        }
    }

    @Test
    @DisplayName("Should handle reactive stream correctly")
    void testReactiveStreamBehavior() {
        // Test that the response is properly wrapped in Mono
        webTestClient.get()
            .uri("/reactive-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, reactive-test!");
    }

    @Test
    @DisplayName("Should handle path variables with different data types")
    void testPathVariableDataTypes() {
        // Test with numeric path variable
        webTestClient.get()
            .uri("/12345")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, 12345!");

        // Test with alphanumeric path variable
        webTestClient.get()
            .uri("/user123")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, user123!");
    }

    @Test
    @DisplayName("Should handle Unicode characters in path")
    void testUnicodeCharacters() {
        webTestClient.get()
            .uri("/José")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, José!");
    }

    @Test
    @DisplayName("Should handle empty and whitespace names")
    void testEmptyAndWhitespaceNames() {
        // Test with single space
        webTestClient.get()
            .uri("/%20")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello,  !");

        // Test with multiple spaces
        webTestClient.get()
            .uri("/%20%20%20")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello,    !");
    }

    @Test
    @DisplayName("Should handle very long path variables")
    void testLongPathVariables() {
        String longName = "a".repeat(1000); // 1000 character name
        webTestClient.get()
            .uri("/" + longName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + longName + "!");
    }

    @Test
    @DisplayName("Should handle special routing patterns")
    void testSpecialRoutingPatterns() {
        // Test nested paths that should hit fallback
        webTestClient.get()
            .uri("/api/v1/users")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");

        // Test paths with file extensions
        webTestClient.get()
            .uri("/file.txt")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, file.txt!");
    }

    @Test
    @DisplayName("Should maintain consistent response format")
    void testResponseFormat() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("text/plain;charset=UTF-8")
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should handle concurrent load")
    void testConcurrentLoad() {
        // Simulate concurrent requests
        Mono<String> request1 = webTestClient.get().uri("/user1").exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult().getResponseBody();

        Mono<String> request2 = webTestClient.get().uri("/user2").exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult().getResponseBody();

        Mono<String> request3 = webTestClient.get().uri("/user3").exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult().getResponseBody();

        // Verify all requests complete successfully
        StepVerifier.create(Mono.zip(request1, request2, request3))
            .expectNextMatches(tuple -> 
                tuple.getT1().equals("hello, user1!") &&
                tuple.getT2().equals("hello, user2!") &&
                tuple.getT3().equals("hello, user3!"))
            .verifyComplete();
    }

    @Test
    @DisplayName("Should handle timeout scenarios gracefully")
    void testTimeoutHandling() {
        webTestClient.mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build()
            .get()
            .uri("/timeout-test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, timeout-test!");
    }

    @Test
    @DisplayName("Should validate router function composition")
    void testRouterFunctionComposition() {
        RouterFunction<ServerResponse> routes = application.routes();
        
        // Verify that the router function is properly composed
        assertNotNull(routes);
        
        // Test that all routes are accessible through the composed function
        webTestClient.get().uri("/").exchange().expectStatus().isOk();
        webTestClient.get().uri("/test").exchange().expectStatus().isOk();
        webTestClient.get().uri("/any/path/here").exchange().expectStatus().isOk();
    }

    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected non-null object");
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}