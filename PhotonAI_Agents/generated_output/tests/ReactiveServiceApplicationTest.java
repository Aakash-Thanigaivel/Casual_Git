import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReactiveServiceApplication.
 * 
 * <p>This test class provides comprehensive unit tests for the ReactiveServiceApplication
 * reactive endpoints with 5% code coverage focusing on critical functionality.
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
    @DisplayName("Test root endpoint returns 'hi'")
    void testRootEndpoint() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Test greeting endpoint with name parameter")
    void testGreetingEndpoint() {
        String testName = "John";
        String expectedResponse = "hello, John!";
        
        webTestClient.get()
            .uri("/" + testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Test greeting endpoint with special characters in name")
    void testGreetingEndpointSpecialCharacters() {
        String testName = "Jane-Doe";
        String expectedResponse = "hello, Jane-Doe!";
        
        webTestClient.get()
            .uri("/" + testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Test fallback endpoint for unmatched routes")
    void testFallbackEndpoint() {
        webTestClient.get()
            .uri("/some/random/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Test fallback endpoint for nested paths")
    void testFallbackEndpointNestedPath() {
        webTestClient.get()
            .uri("/api/v1/users/123")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Test routes bean creation")
    void testRoutesBeanCreation() {
        RouterFunction<ServerResponse> routes = application.routes();
        assertNotNull(routes, "Routes should not be null");
    }

    @Test
    @DisplayName("Test application context loads successfully")
    void testApplicationContextLoads() {
        // This test ensures that the Spring Boot application context loads without errors
        assertNotNull(application, "Application should be instantiated successfully");
    }

    @Test
    @DisplayName("Test greeting endpoint with empty name")
    void testGreetingEndpointEmptyName() {
        // Test with empty string as name parameter
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi"); // Should hit root endpoint, not greeting
    }

    @Test
    @DisplayName("Test greeting endpoint with numeric name")
    void testGreetingEndpointNumericName() {
        String testName = "123";
        String expectedResponse = "hello, 123!";
        
        webTestClient.get()
            .uri("/" + testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Test multiple consecutive requests to same endpoint")
    void testMultipleRequestsToSameEndpoint() {
        // Test that the endpoint handles multiple requests correctly
        for (int i = 0; i < 3; i++) {
            webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }
    }
}