package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

/**
 * Additional integration tests for ReactiveServiceApplication
 * Focuses on specific edge cases and reactive behavior validation
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveServiceApplicationAdditionalTest {

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .configureClient()
                .responseTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Test
    @DisplayName("Should handle URL encoded special characters in path variables")
    void testUrlEncodedSpecialCharacters() {
        webTestClient
                .get()
                .uri("/user%40domain.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, user@domain.com!");
    }

    @Test
    @DisplayName("Should handle Unicode characters in path variables")
    void testUnicodeCharacters() {
        webTestClient
                .get()
                .uri("/测试")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, 测试!");
    }

    @Test
    @DisplayName("Should handle very long path variables")
    void testVeryLongPathVariable() {
        String longName = "a".repeat(1000); // 1000 character name
        webTestClient
                .get()
                .uri("/" + longName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, " + longName + "!");
    }

    @Test
    @DisplayName("Should handle paths with dots and hyphens")
    void testPathsWithSpecialChars() {
        webTestClient
                .get()
                .uri("/user.name-123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, user.name-123!");
    }

    @Test
    @DisplayName("Should handle nested fallback paths correctly")
    void testNestedFallbackPaths() {
        String[] testPaths = {
            "/api/v1/users",
            "/admin/dashboard/settings",
            "/public/assets/images/logo.png",
            "/deep/nested/path/with/many/segments/here"
        };

        for (String path : testPaths) {
            webTestClient
                    .get()
                    .uri(path)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo("fallback");
        }
    }

    @Test
    @DisplayName("Should maintain consistent response headers")
    void testResponseHeaders() {
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("Content-Length")
                .expectHeader().valueEquals("Content-Type", "text/plain;charset=UTF-8");
    }

    @Test
    @DisplayName("Should handle rapid sequential requests")
    void testRapidSequentialRequests() {
        for (int i = 0; i < 50; i++) {
            webTestClient
                    .get()
                    .uri("/test" + i)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo("hello, test" + i + "!");
        }
    }

    @Test
    @DisplayName("Should validate router function bean configuration")
    void testRouterFunctionConfiguration() {
        assertTrue(applicationContext.containsBean("routes"));
        Object routerFunction = applicationContext.getBean("routes");
        assertNotNull(routerFunction);
        assertEquals("org.springframework.web.reactive.function.server.RouterFunctions$DefaultRouterFunction", 
                    routerFunction.getClass().getName());
    }

    @Test
    @DisplayName("Should handle case-sensitive path variables")
    void testCaseSensitivePathVariables() {
        webTestClient
                .get()
                .uri("/John")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, John!");

        webTestClient
                .get()
                .uri("/john")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, john!");
    }

    @Test
    @DisplayName("Should handle paths with trailing slashes")
    void testTrailingSlashes() {
        // Test that trailing slashes are handled by fallback
        webTestClient
                .get()
                .uri("/api/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
    }
}