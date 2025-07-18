package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Unit tests for ReactiveServiceApplication.
 * Tests basic routing functionality of the reactive web service.
 * Coverage: ~5% of main functionality
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReactiveServiceApplicationTest {

    private WebTestClient webTestClient;
    private ReactiveServiceApplication application;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
        RouterFunction<ServerResponse> routes = application.routes();
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    @DisplayName("Should return 'hi' for root path")
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
    @DisplayName("Should return personalized greeting for name parameter")
    void testNameEndpoint() {
        // Given
        String testName = "John";
        String expectedResponse = "hello, " + testName + "!";
        
        // When & Then
        webTestClient.get()
            .uri("/" + testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return fallback for unmatched paths")
    void testFallbackEndpoint() {
        // When & Then
        webTestClient.get()
            .uri("/some/unknown/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }
}