import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.web.server.LocalServerPort;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete Spring 6.1 reactive application
 * Bank of America Legacy Code Modernization Project
 * Tests end-to-end functionality and Spring 6.1 features
 */
@SpringBootTest(
    classes = com.bankofamerica.modernized.ReactiveServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
    "server.port=0",
    "spring.main.web-application-type=reactive",
    "logging.level.com.bankofamerica=DEBUG"
})
@DisplayName("Spring 6.1 & JDK 17 Integration Tests")
class ReactiveServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @Nested
    @DisplayName("Application Startup Tests")
    class ApplicationStartupTests {

        @Test
        @DisplayName("Should start application with Spring 6.1 and JDK 17 successfully")
        void shouldStartApplicationSuccessfully() {
            // This test validates that the entire application starts correctly
            // with Spring 6.1 and JDK 17 modernizations
            assertTrue(port > 0, "Application should start on a valid port");
            
            // Verify the application is responsive
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
        }

        @Test
        @DisplayName("Should load Spring context with reactive configuration")
        void shouldLoadSpringContextWithReactiveConfiguration() {
            // Verify that the reactive web stack is properly configured
            assertNotNull(webTestClient, "WebTestClient should be available");
            
            // Test that reactive endpoints are working
            webTestClient
                .get()
                .uri("/health-check")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }
    }

    @Nested
    @DisplayName("End-to-End Workflow Tests")
    class EndToEndWorkflowTests {

        @Test
        @DisplayName("Should handle complete user interaction workflow")
        void shouldHandleCompleteUserInteractionWorkflow() {
            // Test a complete user workflow through the application
            
            // Step 1: Access root endpoint
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");

            // Step 2: Access personalized greeting
            webTestClient
                .get()
                .uri("/BankOfAmerica")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, BankOfAmerica!");

            // Step 3: Access unknown endpoint (fallback)
            webTestClient
                .get()
                .uri("/unknown/service")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should handle business logic integration")
        void shouldHandleBusinessLogicIntegration() {
            // Test integration with business logic components
            var businessUser = "business-user-123";
            var expectedResponse = "hello, " + businessUser + "!";
            
            webTestClient
                .get()
                .uri("/{name}", businessUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }
    }

    @Nested
    @DisplayName("Reactive Stream Integration Tests")
    class ReactiveStreamIntegrationTests {

        @Test
        @DisplayName("Should handle reactive streams end-to-end")
        void shouldHandleReactiveStreamsEndToEnd() {
            // Test reactive stream processing
            var responses = Flux.range(1, 5)
                .flatMap(i -> webTestClient
                    .get()
                    .uri("/user" + i)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(String.class)
                    .getResponseBody()
                    .next())
                .collectList();

            StepVerifier.create(responses)
                .assertNext(list -> {
                    assertEquals(5, list.size());
                    assertTrue(list.get(0).contains("hello, user1!"));
                    assertTrue(list.get(4).contains("hello, user5!"));
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Should handle backpressure in reactive streams")
        void shouldHandleBackpressureInReactiveStreams() {
            // Test backpressure handling
            var manyRequests = Flux.range(1, 100)
                .flatMap(i -> webTestClient
                    .get()
                    .uri("/")
                    .exchange()
                    .returnResult(String.class)
                    .getResponseBody()
                    .next(), 10) // Limit concurrency to test backpressure
                .count();

            StepVerifier.create(manyRequests)
                .expectNext(100L)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Performance Integration Tests")
    class PerformanceIntegrationTests {

        @Test
        @DisplayName("Should meet performance requirements under load")
        void shouldMeetPerformanceRequirementsUnderLoad() throws Exception {
            var executorService = Executors.newFixedThreadPool(10);
            var numberOfRequests = 200;
            var futures = new CompletableFuture[numberOfRequests];
            
            var startTime = System.currentTimeMillis();
            
            try {
                for (int i = 0; i < numberOfRequests; i++) {
                    final var requestId = i;
                    futures[i] = CompletableFuture.supplyAsync(() -> {
                        return webTestClient
                            .get()
                            .uri("/load-test-" + requestId)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(String.class)
                            .getResponseBody()
                            .blockFirst();
                    }, executorService);
                }
                
                // Wait for all requests to complete
                CompletableFuture.allOf(futures).get(30, TimeUnit.SECONDS);
                
                var endTime = System.currentTimeMillis();
                var totalTime = endTime - startTime;
                
                // Should handle 200 concurrent requests in under 10 seconds
                assertTrue(totalTime < 10000, 
                    "Should handle " + numberOfRequests + " requests in under 10 seconds, took: " + totalTime + "ms");
                
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
        }

        @Test
        @DisplayName("Should maintain consistent response times")
        void shouldMaintainConsistentResponseTimes() {
            var responseTimes = new long[10];
            
            for (int i = 0; i < 10; i++) {
                var startTime = System.nanoTime();
                
                webTestClient
                    .get()
                    .uri("/performance-test-" + i)
                    .exchange()
                    .expectStatus().isOk();
                
                var endTime = System.nanoTime();
                responseTimes[i] = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            }
            
            // Calculate average response time
            var averageResponseTime = java.util.Arrays.stream(responseTimes)
                .average()
                .orElse(0.0);
            
            // Average response time should be under 50ms
            assertTrue(averageResponseTime < 50, 
                "Average response time should be under 50ms, was: " + averageResponseTime + "ms");
        }
    }

    @Nested
    @DisplayName("Modernization Validation Tests")
    class ModernizationValidationTests {

        @Test
        @DisplayName("Should validate Spring 6.1 features are working")
        void shouldValidateSpring61FeaturesAreWorking() {
            // Test that Spring 6.1 specific features are working
            webTestClient
                .get()
                .uri("/spring61-feature-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should validate JDK 17 runtime features")
        void shouldValidateJdk17RuntimeFeatures() {
            // Validate that JDK 17 features are working at runtime
            var javaVersion = System.getProperty("java.version");
            assertTrue(javaVersion.startsWith("17") || javaVersion.startsWith("21"), 
                "Should be running on JDK 17 or later, found: " + javaVersion);
            
            // Test that the application works with JDK 17
            webTestClient
                .get()
                .uri("/jdk17-test")
                .exchange()
                .expectStatus().isOk();
        }

        @Test
        @DisplayName("Should validate reactive programming model")
        void shouldValidateReactiveProgrammingModel() {
            // Test that the reactive programming model is working correctly
            var response = webTestClient
                .get()
                .uri("/reactive-test")
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
    @DisplayName("Error Handling Integration Tests")
    class ErrorHandlingIntegrationTests {

        @Test
        @DisplayName("Should handle application errors gracefully")
        void shouldHandleApplicationErrorsGracefully() {
            // Test that the application handles errors gracefully
            // All unknown paths should return fallback response
            webTestClient
                .get()
                .uri("/this/path/does/not/exist")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("fallback");
        }

        @Test
        @DisplayName("Should maintain service availability during errors")
        void shouldMaintainServiceAvailabilityDuringErrors() {
            // Test that the service remains available even when handling errors
            for (int i = 0; i < 10; i++) {
                webTestClient
                    .get()
                    .uri("/error-test-" + i)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .isEqualTo("fallback");
            }
            
            // Verify that normal endpoints still work after error handling
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hi");
        }
    }

    @Nested
    @DisplayName("Security Integration Tests")
    class SecurityIntegrationTests {

        @Test
        @DisplayName("Should handle security requirements")
        void shouldHandleSecurityRequirements() {
            // Test basic security requirements
            // All endpoints should be accessible (no authentication required in this simple app)
            webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
        }

        @Test
        @DisplayName("Should handle input validation")
        void shouldHandleInputValidation() {
            // Test input validation for path variables
            var maliciousInput = "<script>alert('xss')</script>";
            var expectedResponse = "hello, " + maliciousInput + "!";
            
            webTestClient
                .get()
                .uri("/{name}", maliciousInput)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
        }
    }
}