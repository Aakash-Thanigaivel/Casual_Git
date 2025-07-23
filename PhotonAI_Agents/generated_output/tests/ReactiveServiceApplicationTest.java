package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Integration tests for ReactiveServiceApplication
 * Tests cover all reactive routes and responses
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveServiceApplication application;

    @Test
    @DisplayName("Should return 'hi' for root path")
    void shouldReturnHiForRootPath() {
        webTestClient
            .get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should return personalized greeting for name path")
    void shouldReturnPersonalizedGreeting() {
        // Given
        String testName = "John";
        String expectedGreeting = "hello, " + testName + "!";

        // When & Then
        webTestClient
            .get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedGreeting);
    }

    @Test
    @DisplayName("Should return fallback for any other path")
    void shouldReturnFallbackForOtherPaths() {
        webTestClient
            .get()
            .uri("/some/random/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should have routes bean configured")
    void shouldHaveRoutesBeanConfigured() {
        // When
        RouterFunction<ServerResponse> routes = application.routes();

        // Then
        assertNotNull(routes);
    }
}