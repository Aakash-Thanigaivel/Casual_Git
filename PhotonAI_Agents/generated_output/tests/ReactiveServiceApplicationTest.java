package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for ReactiveServiceApplication.
 * 
 * <p>This test class provides comprehensive test coverage for the reactive web
 * endpoints including root, parameterized, and fallback routes.
 * 
 * <p>Following Google Java Style Guidelines and Spring Boot testing best practices.
 */
@WebFluxTest(ReactiveServiceApplication.class)
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should return 'hi' for root endpoint")
    void testRootEndpoint() {
        // When & Then
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should return personalized greeting for name endpoint")
    void testNameEndpoint() {
        // Given
        String testName = "John";
        String expectedResponse = "hello, " + testName + "!";
        
        // When & Then
        webTestClient.get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return 'fallback' for unmatched endpoints")
    void testFallbackEndpoint() {
        // When & Then
        webTestClient.get()
            .uri("/some/random/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should handle special characters in name parameter")
    void testNameEndpointWithSpecialCharacters() {
        // Given
        String testName = "test-user_123";
        String expectedResponse = "hello, " + testName + "!";
        
        // When & Then
        webTestClient.get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should handle empty name parameter")
    void testNameEndpointWithEmptyName() {
        // Given
        String emptyName = "";
        String expectedResponse = "hello, " + emptyName + "!";
        
        // When & Then
        webTestClient.get()
            .uri("/{name}", emptyName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }
}