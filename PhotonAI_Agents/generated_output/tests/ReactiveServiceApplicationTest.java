import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.test.StepVerifier;

/**
 * Unit tests for ReactiveServiceApplication.
 * 
 * Tests cover the reactive routing functionality and endpoint responses
 * using Spring WebFlux testing utilities.
 */
@SpringBootTest(classes = ReactiveServiceApplication.class)
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
    void shouldReturnPersonalizedGreetingForNameEndpoint() {
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
    @DisplayName("Should handle special characters in name parameter")
    void shouldHandleSpecialCharactersInName() {
        // Given
        String testName = "José";
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
    @DisplayName("Should return fallback for unknown endpoints")
    void shouldReturnFallbackForUnknownEndpoints() {
        // When & Then
        webTestClient.get()
            .uri("/unknown/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should return fallback for nested unknown paths")
    void shouldReturnFallbackForNestedUnknownPaths() {
        // When & Then
        webTestClient.get()
            .uri("/api/v1/unknown")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should verify router function is not null")
    void shouldVerifyRouterFunctionIsNotNull() {
        // When
        RouterFunction<ServerResponse> routes = application.routes();

        // Then
        assertNotNull(routes);
    }

    @Test
    @DisplayName("Should handle empty name parameter")
    void shouldHandleEmptyNameParameter() {
        // Given
        String testName = "";
        String expectedGreeting = "hello, " + testName + "!";

        // When & Then
        webTestClient.get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedGreeting);
    }
}