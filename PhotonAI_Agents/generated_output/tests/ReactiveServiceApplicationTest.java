package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for ReactiveServiceApplication.
 * Tests the reactive web endpoints using WebTestClient.
 */
@WebFluxTest(ReactiveServiceApplication.class)
@DisplayName("Reactive Service Application Tests")
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should return greeting message for root endpoint")
    void testRootEndpoint() {
        webTestClient
            .get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should return personalized greeting for name endpoint")
    void testNameEndpoint() {
        String testName = "John";
        
        webTestClient
            .get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + testName + "!");
    }

    @Test
    @DisplayName("Should return personalized greeting for different names")
    void testNameEndpointWithDifferentNames() {
        // Test with different names
        String[] testNames = {"Alice", "Bob", "Charlie", "Diana"};
        
        for (String name : testNames) {
            webTestClient
                .get()
                .uri("/{name}", name)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, " + name + "!");
        }
    }

    @Test
    @DisplayName("Should return fallback message for unknown endpoints")
    void testFallbackEndpoint() {
        webTestClient
            .get()
            .uri("/unknown/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should return fallback for nested unknown paths")
    void testFallbackEndpointNestedPath() {
        webTestClient
            .get()
            .uri("/some/deeply/nested/unknown/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should handle special characters in name parameter")
    void testNameEndpointWithSpecialCharacters() {
        String nameWithSpecialChars = "test-user_123";
        
        webTestClient
            .get()
            .uri("/{name}", nameWithSpecialChars)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + nameWithSpecialChars + "!");
    }
}