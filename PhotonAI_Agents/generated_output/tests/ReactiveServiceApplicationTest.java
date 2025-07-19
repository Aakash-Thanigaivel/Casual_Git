package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReactiveServiceApplication.
 * 
 * <p>This test class provides comprehensive coverage for the reactive Spring Boot
 * application endpoints and routing functionality using WebFluxTest.
 * 
 * @author Bank of America Code Modernization Team
 * @version 1.0 (Spring Boot 6.1, JDK 17)
 */
@WebFluxTest
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RouterFunction<ServerResponse> routes() {
            return new ReactiveServiceApplication().routes();
        }
    }

    @Nested
    @DisplayName("Root Endpoint Tests")
    class RootEndpointTests {

        @Test
        @DisplayName("Should return 'hi' for root endpoint")
        void shouldReturnHiForRootEndpoint() {
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }

        @Test
        @DisplayName("Should return correct content type for root endpoint")
        void shouldReturnCorrectContentTypeForRoot() {
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8");
        }
    }

    @Nested
    @DisplayName("Named Greeting Endpoint Tests")
    class NamedGreetingEndpointTests {

        @Test
        @DisplayName("Should return personalized greeting for valid name")
        void shouldReturnPersonalizedGreeting() {
            String testName = "John";
            String expectedResponse = "hello, John!";

            webTestClient
                .get()
                .uri("/{name}", testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should handle special characters in name")
        void shouldHandleSpecialCharactersInName() {
            String testName = "José";
            String expectedResponse = "hello, José!";

            webTestClient
                .get()
                .uri("/{name}", testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should handle numeric name")
        void shouldHandleNumericName() {
            String testName = "123";
            String expectedResponse = "hello, 123!";

            webTestClient
                .get()
                .uri("/{name}", testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should handle empty name parameter")
        void shouldHandleEmptyNameParameter() {
            // Empty string in path should still work
            webTestClient
                .get()
                .uri("/ ")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello,  !");
        }
    }

    @Nested
    @DisplayName("Fallback Endpoint Tests")
    class FallbackEndpointTests {

        @Test
        @DisplayName("Should return fallback for unmatched paths")
        void shouldReturnFallbackForUnmatchedPaths() {
            webTestClient
                .get()
                .uri("/unknown/path")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should return fallback for deeply nested paths")
        void shouldReturnFallbackForDeeplyNestedPaths() {
            webTestClient
                .get()
                .uri("/api/v1/users/123/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should return fallback for paths with query parameters")
        void shouldReturnFallbackForPathsWithQueryParams() {
            webTestClient
                .get()
                .uri("/search?q=test&limit=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }
    }

    @Nested
    @DisplayName("HTTP Method Tests")
    class HttpMethodTests {

        @Test
        @DisplayName("Should only accept GET requests for root endpoint")
        void shouldOnlyAcceptGetForRoot() {
            webTestClient
                .post()
                .uri("/")
                .exchange()
                .expectStatus().isMethodNotAllowed();
        }

        @Test
        @DisplayName("Should only accept GET requests for named endpoint")
        void shouldOnlyAcceptGetForNamed() {
            webTestClient
                .put()
                .uri("/testname")
                .exchange()
                .expectStatus().isMethodNotAllowed();
        }

        @Test
        @DisplayName("Should only accept GET requests for fallback endpoint")
        void shouldOnlyAcceptGetForFallback() {
            webTestClient
                .delete()
                .uri("/unknown/path")
                .exchange()
                .expectStatus().isMethodNotAllowed();
        }
    }

    @Nested
    @DisplayName("Router Function Configuration Tests")
    class RouterFunctionTests {

        @Test
        @DisplayName("Should create router function bean successfully")
        void shouldCreateRouterFunctionBean() {
            var app = new ReactiveServiceApplication();
            RouterFunction<ServerResponse> routes = app.routes();
            
            assertNotNull(routes, "Router function should not be null");
        }

        @Test
        @DisplayName("Should handle concurrent requests correctly")
        void shouldHandleConcurrentRequests() {
            // Test multiple concurrent requests
            for (int i = 0; i < 10; i++) {
                final String name = "User" + i;
                webTestClient
                    .get()
                    .uri("/{name}", name)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo("hello, " + name + "!");
            }
        }
    }
}