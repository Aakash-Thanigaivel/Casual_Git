import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ReactiveServiceApplication.
 * Tests cover the reactive web endpoints and routing functionality.
 */
@SpringBootTest
@DisplayName("ReactiveServiceApplication Tests")
class ReactiveServiceApplicationTest {

    private WebTestClient webTestClient;
    private ReactiveServiceApplication application;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
        RouterFunction<ServerResponse> routes = application.routes();
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    @DisplayName("Should return 'hi' for root endpoint")
    void shouldReturnHiForRootEndpoint() {
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
    void shouldReturnPersonalizedGreeting() {
        // Given
        String testName = "John";
        String expectedGreeting = "hello, " + testName + "!";

        // When & Then
        webTestClient.get()
                .uri("/{name}", testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedGreeting);
    }

    @Test
    @DisplayName("Should return fallback for unmatched paths")
    void shouldReturnFallbackForUnmatchedPaths() {
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
    void shouldHandleSpecialCharactersInName() {
        // Given
        String specialName = "test-user_123";
        String expectedGreeting = "hello, " + specialName + "!";

        // When & Then
        webTestClient.get()
                .uri("/{name}", specialName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedGreeting);
    }

    @Test
    @DisplayName("Should create routes bean successfully")
    void shouldCreateRoutesBeanSuccessfully() {
        // When
        RouterFunction<ServerResponse> routes = application.routes();

        // Then
        assertNotNull(routes, "Routes should not be null");
    }
}