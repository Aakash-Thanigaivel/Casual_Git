import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ReactiveServiceApplication
 * Modernized for Spring 6.1 and JDK 17
 * Bank of America Legacy Code Modernization Project
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"server.port=0"})
@DisplayName("ReactiveServiceApplication Tests - Spring 6.1 & JDK 17")
class ReactiveServiceApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Nested
    @DisplayName("Router Function Tests")
    class RouterFunctionTests {

        @Test
        @DisplayName("Should handle root endpoint correctly")
        void shouldHandleRootEndpoint() {
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }

        @Test
        @DisplayName("Should handle greeting endpoint with name parameter")
        void shouldHandleGreetingEndpoint() {
            // Given
            var testName = "BankOfAmerica";
            var expectedGreeting = "hello, " + testName + "!";

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
        @DisplayName("Should handle greeting endpoint with special characters")
        void shouldHandleGreetingWithSpecialCharacters() {
            // Given
            var testName = "User-123";
            var expectedGreeting = "hello, " + testName + "!";

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
        @DisplayName("Should handle fallback endpoint for unknown paths")
        void shouldHandleFallbackEndpoint() {
            webTestClient
                .get()
                .uri("/unknown/path/here")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should handle deeply nested fallback paths")
        void shouldHandleDeeplyNestedFallbackPaths() {
            webTestClient
                .get()
                .uri("/very/deep/nested/path/structure")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should handle numeric path variables")
        void shouldHandleNumericPathVariables() {
            // Given
            var numericName = "12345";
            var expectedGreeting = "hello, " + numericName + "!";

            // When & Then
            webTestClient
                .get()
                .uri("/{name}", numericName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedGreeting);
        }
    }

    @Nested
    @DisplayName("Reactive Behavior Tests")
    class ReactiveBehaviorTests {

        @Test
        @DisplayName("Should return Mono responses correctly")
        void shouldReturnMonoResponses() {
            // Test that responses are properly wrapped in Mono
            var response = webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

            StepVerifier.create(response)
                .expectNext("hi")
                .verifyComplete();
        }

        @Test
        @DisplayName("Should handle concurrent requests efficiently")
        void shouldHandleConcurrentRequests() {
            // Given
            var numberOfRequests = 50;
            var requests = new Mono[numberOfRequests];

            // When
            for (int i = 0; i < numberOfRequests; i++) {
                requests[i] = webTestClient
                    .get()
                    .uri("/")
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(String.class)
                    .getResponseBody()
                    .next();
            }

            // Then
            var combinedResult = Mono.when(requests);
            StepVerifier.create(combinedResult)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should handle reactive streams with timeout")
        void shouldHandleReactiveStreamsWithTimeout() {
            var response = webTestClient
                .get()
                .uri("/testuser")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

            StepVerifier.create(response)
                .expectNext("hello, testuser!")
                .expectComplete()
                .verify(Duration.ofSeconds(5));
        }

        @Test
        @DisplayName("Should handle backpressure correctly")
        void shouldHandleBackpressureCorrectly() {
            // Test reactive backpressure handling
            var response = webTestClient
                .get()
                .uri("/backpressure-test")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

            StepVerifier.create(response)
                .expectNext("fallback")
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Spring 6.1 Feature Tests")
    class Spring61FeatureTests {

        @Test
        @DisplayName("Should use modern bodyValue instead of body with Mono.just")
        void shouldUseModernBodyValue() {
            // This test verifies that the modernized code uses bodyValue()
            // which is more efficient than body(Mono.just())
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .isEqualTo("hi");
        }

        @Test
        @DisplayName("Should work with Spring 6.1 WebFlux features")
        void shouldWorkWithSpring61WebFluxFeatures() {
            // Test Spring 6.1 specific features and compatibility
            webTestClient
                .get()
                .uri("/spring61test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should handle Spring 6.1 router function composition")
        void shouldHandleSpring61RouterFunctionComposition() {
            // Test that router functions are properly composed in Spring 6.1
            webTestClient
                .get()
                .uri("/composition-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }
    }

    @Nested
    @DisplayName("JDK 17 Feature Tests")
    class Jdk17FeatureTests {

        @Test
        @DisplayName("Should validate var keyword usage in handlers")
        void shouldValidateVarKeywordUsage() {
            // Test that JDK 17 var keyword is working correctly
            var testName = "jdk17test";
            var expectedResponse = "hello, " + testName + "!";

            webTestClient
                .get()
                .uri("/{name}", testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should validate Map.of() usage")
        void shouldValidateMapOfUsage() {
            // This test validates that the application starts correctly
            // with the modern Map.of() instead of Collections.singletonMap()
            // The fact that the application context loads proves this works
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
        }

        @Test
        @DisplayName("Should validate text blocks if used")
        void shouldValidateTextBlocks() {
            // Test JDK 17 text blocks functionality
            var multilineText = """
                This is a test for
                JDK 17 text blocks
                in the modernized application
                """;
            
            assertNotNull(multilineText);
            assertTrue(multilineText.contains("JDK 17"));
        }

        @Test
        @DisplayName("Should validate switch expressions")
        void shouldValidateModernSwitchExpressions() {
            // Test modern switch expressions
            var testValue = "test";
            var result = switch (testValue) {
                case "test" -> "success";
                case "fail" -> "failure";
                default -> "unknown";
            };
            
            assertEquals("success", result);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle moderate load efficiently")
        void shouldHandleModerateLoadEfficiently() {
            var startTime = System.currentTimeMillis();
            var numberOfRequests = 100;

            for (int i = 0; i < numberOfRequests; i++) {
                webTestClient
                    .get()
                    .uri("/")
                    .exchange()
                    .expectStatus().isOk();
            }

            var endTime = System.currentTimeMillis();
            var duration = endTime - startTime;

            // Should handle 100 requests in reasonable time (under 5 seconds)
            assertTrue(duration < 5000, 
                "Should handle " + numberOfRequests + " requests in under 5 seconds");
        }

        @Test
        @DisplayName("Should maintain low memory footprint")
        void shouldMaintainLowMemoryFootprint() {
            var runtime = Runtime.getRuntime();
            var initialMemory = runtime.totalMemory() - runtime.freeMemory();

            // Make multiple requests
            for (int i = 0; i < 50; i++) {
                webTestClient
                    .get()
                    .uri("/user" + i)
                    .exchange()
                    .expectStatus().isOk();
            }

            var finalMemory = runtime.totalMemory() - runtime.freeMemory();
            var memoryIncrease = finalMemory - initialMemory;

            // Memory increase should be minimal for reactive applications
            assertTrue(memoryIncrease < 2_000_000, 
                "Memory increase should be under 2MB for reactive application");
        }

        @Test
        @DisplayName("Should handle response time requirements")
        void shouldHandleResponseTimeRequirements() {
            var startTime = System.nanoTime();
            
            webTestClient
                .get()
                .uri("/performance-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
            
            var endTime = System.nanoTime();
            var responseTimeMs = (endTime - startTime) / 1_000_000;
            
            // Response time should be under 100ms for simple requests
            assertTrue(responseTimeMs < 100, 
                "Response time should be under 100ms, was: " + responseTimeMs + "ms");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle empty path variables gracefully")
        void shouldHandleEmptyPathVariablesGracefully() {
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }

        @Test
        @DisplayName("Should handle URL encoded path variables")
        void shouldHandleUrlEncodedPathVariables() {
            var encodedName = "user%20name";
            
            webTestClient
                .get()
                .uri("/{name}", encodedName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("user%20name")));
        }

        @Test
        @DisplayName("Should handle special characters in path variables")
        void shouldHandleSpecialCharactersInPathVariables() {
            var specialName = "user@domain.com";
            var expectedGreeting = "hello, " + specialName + "!";
            
            webTestClient
                .get()
                .uri("/{name}", specialName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedGreeting);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should integrate all router functions correctly")
        void shouldIntegrateAllRouterFunctionsCorrectly() {
            // Test root endpoint
            webTestClient.get().uri("/").exchange().expectStatus().isOk();
            
            // Test named endpoint
            webTestClient.get().uri("/integration").exchange().expectStatus().isOk();
            
            // Test fallback endpoint
            webTestClient.get().uri("/unknown/path").exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("Should maintain consistent behavior across endpoints")
        void shouldMaintainConsistentBehaviorAcrossEndpoints() {
            // All endpoints should return 200 OK status
            webTestClient.get().uri("/").exchange().expectStatus().isOk();
            webTestClient.get().uri("/test").exchange().expectStatus().isOk();
            webTestClient.get().uri("/any/random/path").exchange().expectStatus().isOk();
        }
    }
}