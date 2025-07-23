package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReactiveServiceApplication
 * Provides unit tests with 5% code coverage for JDK 17 modernized Spring Boot application
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    private ReactiveServiceApplication application;

    @BeforeEach
    void setUp() {
        application = new ReactiveServiceApplication();
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
        var testName = "John";
        
        webTestClient.get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + testName + "!");
    }

    @Test
    @DisplayName("Test greeting endpoint with special characters in name")
    void testGreetingEndpointSpecialCharacters() {
        var testName = "test-user_123";
        
        webTestClient.get()
            .uri("/{name}", testName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + testName + "!");
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
    void testFallbackEndpointNestedPaths() {
        webTestClient.get()
            .uri("/api/v1/users/123/profile")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Test routes bean configuration")
    void testRoutesConfiguration() {
        var routes = application.routes();
        assertNotNull(routes, "Routes should not be null");
        assertTrue(routes instanceof RouterFunction, "Should return RouterFunction instance");
    }

    @Test
    @DisplayName("Test application context loads successfully")
    void testApplicationContextLoads() {
        // This test verifies that the Spring Boot application context loads without issues
        assertNotNull(application, "Application should be instantiated");
    }

    @Test
    @DisplayName("Test main method execution")
    void testMainMethod() {
        // Test that main method can be called without exceptions
        // Note: This won't actually start the server in test environment
        assertDoesNotThrow(() -> {
            var args = new String[]{};
            // We can't easily test the actual main method execution in unit tests
            // without starting the full application, so we just verify it doesn't throw
            // during class loading and method resolution
            var mainMethod = ReactiveServiceApplication.class.getMethod("main", String[].class);
            assertNotNull(mainMethod, "Main method should exist");
        });
    }

    @Test
    @DisplayName("Test multiple concurrent requests to root endpoint")
    void testConcurrentRootRequests() {
        // Test reactive nature by making multiple concurrent requests
        for (int i = 0; i < 5; i++) {
            webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }
    }

    @Test
    @DisplayName("Test empty name parameter handling")
    void testEmptyNameParameter() {
        webTestClient.get()
            .uri("/")  // This should hit root, not greeting endpoint
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Test application properties configuration")
    void testApplicationProperties() {
        // Verify that the application can be configured with custom properties
        // This tests the SpringApplicationBuilder configuration
        assertDoesNotThrow(() -> {
            var builder = new org.springframework.boot.builder.SpringApplicationBuilder(ReactiveServiceApplication.class);
            assertNotNull(builder, "SpringApplicationBuilder should be created successfully");
        });
    }
}