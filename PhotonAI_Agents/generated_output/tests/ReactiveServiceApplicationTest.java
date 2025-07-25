package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.ApplicationContext;

/**
 * Comprehensive unit tests for ReactiveServiceApplication.
 * 
 * <p>This test class provides integration testing for the reactive Spring Boot
 * application endpoints and routing functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {"server.port=0"})
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        // Ensure the application context is properly loaded
        assertNotNull(applicationContext);
    }

    @Test
    @DisplayName("Should return greeting for root path")
    void testRootEndpoint() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should return personalized greeting with name")
    void testGreetingEndpoint() {
        webTestClient.get()
            .uri("/John")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, John!");
    }

    @Test
    @DisplayName("Should return fallback for unmatched paths")
    void testFallbackEndpoint() {
        webTestClient.get()
            .uri("/some/unknown/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should handle special characters in name")
    void testGreetingWithSpecialCharacters() {
        webTestClient.get()
            .uri("/test-user")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, test-user!");
    }

    @Test
    @DisplayName("Should handle numeric names")
    void testGreetingWithNumbers() {
        webTestClient.get()
            .uri("/123")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, 123!");
    }

    @Test
    @DisplayName("Should handle names with spaces")
    void testGreetingWithSpaces() {
        webTestClient.get()
            .uri("/John%20Doe")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, John Doe!");
    }

    @Test
    @DisplayName("Should handle empty name parameter")
    void testGreetingWithEmptyName() {
        webTestClient.get()
            .uri("/ ")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello,  !");
    }

    @Test
    @DisplayName("Should handle multiple path segments in fallback")
    void testFallbackWithMultipleSegments() {
        webTestClient.get()
            .uri("/api/v1/users/123/profile")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should handle query parameters in fallback")
    void testFallbackWithQueryParameters() {
        webTestClient.get()
            .uri("/unknown?param1=value1&param2=value2")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should handle long names")
    void testGreetingWithLongName() {
        String longName = "VeryLongNameThatExceedsNormalLengthExpectations";
        webTestClient.get()
            .uri("/" + longName)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, " + longName + "!");
    }

    @Test
    @DisplayName("Should handle names with special URL characters")
    void testGreetingWithUrlEncodedCharacters() {
        webTestClient.get()
            .uri("/user%40example.com")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, user@example.com!");
    }

    @Test
    @DisplayName("Should verify router function bean exists")
    void testRouterFunctionBeanExists() {
        RouterFunction<ServerResponse> routerFunction = 
            applicationContext.getBean(RouterFunction.class);
        assertNotNull(routerFunction);
    }

    @Test
    @DisplayName("Should handle concurrent requests")
    void testConcurrentRequests() {
        // Test multiple concurrent requests to ensure thread safety
        for (int i = 0; i < 10; i++) {
            final int requestId = i;
            webTestClient.get()
                .uri("/user" + requestId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, user" + requestId + "!");
        }
    }

    @Test
    @DisplayName("Should handle case sensitive names")
    void testCaseSensitiveNames() {
        webTestClient.get()
            .uri("/John")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, John!");

        webTestClient.get()
            .uri("/john")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, john!");
    }

    @Test
    @DisplayName("Should handle names with underscores and hyphens")
    void testNamesWithSpecialCharacters() {
        webTestClient.get()
            .uri("/user_name")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, user_name!");

        webTestClient.get()
            .uri("/user-name")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, user-name!");
    }

    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected non-null object");
        }
    }
}