import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReactiveServiceApplication class.
 * 
 * <p>This test class provides comprehensive test coverage for the ReactiveServiceApplication
 * Spring Boot reactive web application, testing routing functionality and endpoints.
 */
@SpringBootTest
class ReactiveServiceApplicationTest {

    private ReactiveServiceApplication application;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
        RouterFunction<ServerResponse> routes = application.routes();
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    @DisplayName("Should return greeting message for root endpoint")
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
    void testPersonalizedGreetingEndpoint() {
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
    @DisplayName("Should return fallback message for unmatched routes")
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
    void testPersonalizedGreetingWithSpecialCharacters() {
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
    @DisplayName("Should create routes bean successfully")
    void testRoutesBean() {
        // When
        RouterFunction<ServerResponse> routes = application.routes();

        // Then
        assertNotNull(routes);
    }

    @Test
    @DisplayName("Should handle empty name parameter")
    void testPersonalizedGreetingWithEmptyName() {
        // Given
        String emptyName = "";
        String expectedGreeting = "hello, " + emptyName + "!";

        // When & Then
        webTestClient.get()
            .uri("/{name}", emptyName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedGreeting);
    }

    @Test
    @DisplayName("Should handle numeric name parameter")
    void testPersonalizedGreetingWithNumericName() {
        // Given
        String numericName = "123";
        String expectedGreeting = "hello, " + numericName + "!";

        // When & Then
        webTestClient.get()
            .uri("/{name}", numericName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedGreeting);
    }

    @Test
    @DisplayName("Should handle long path for fallback route")
    void testFallbackEndpointWithLongPath() {
        // When & Then
        webTestClient.get()
            .uri("/very/long/path/that/should/trigger/fallback/route")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }
}