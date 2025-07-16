package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

/**
 * Reactive application method coverage tests
 * Ensures all code paths in the Spring Boot application are tested
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveApplicationCoverageTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ReactiveServiceApplication reactiveServiceApplication;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .configureClient()
                .responseTimeout(Duration.ofSeconds(3))
                .build();
    }

    @Test
    @DisplayName("Should test routes() method bean creation")
    void testRoutesMethodBeanCreation() {
        // Verify that the routes() method creates a proper RouterFunction
        RouterFunction<ServerResponse> routes = reactiveServiceApplication.routes();
        assertNotNull(routes);
        
        // Verify the bean is properly registered in the application context
        assertTrue(applicationContext.containsBean("routes"));
        RouterFunction<ServerResponse> contextRoutes = (RouterFunction<ServerResponse>) applicationContext.getBean("routes");
        assertNotNull(contextRoutes);
    }

    @Test
    @DisplayName("Should test handleRoot method coverage")
    void testHandleRootMethodCoverage() {
        // This tests the private handleRoot method through the public endpoint
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertEquals("hi", response);
                    assertNotNull(response);
                    assertFalse(response.isEmpty());
                });
    }

    @Test
    @DisplayName("Should test handleGreeting method coverage")
    void testHandleGreetingMethodCoverage() {
        // Test the private handleGreeting method through the public endpoint
        String testName = "TestUser";
        webTestClient
                .get()
                .uri("/" + testName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertEquals("hello, " + testName + "!", response);
                    assertTrue(response.contains(testName));
                    assertTrue(response.startsWith("hello, "));
                    assertTrue(response.endsWith("!"));
                });
    }

    @Test
    @DisplayName("Should test handleFallback method coverage")
    void testHandleFallbackMethodCoverage() {
        // Test the private handleFallback method through the public endpoint
        webTestClient
                .get()
                .uri("/some/random/path")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertEquals("fallback", response);
                    assertNotNull(response);
                    assertEquals(8, response.length());
                });
    }

    @Test
    @DisplayName("Should test main method application startup")
    void testMainMethodApplicationStartup() {
        // Verify that the application context was started successfully by main method
        assertNotNull(applicationContext);
        assertTrue(applicationContext.isActive());
        
        // Verify that the ReactiveServiceApplication bean exists
        assertTrue(applicationContext.containsBean("reactiveServiceApplication"));
        
        // Verify that the application is running (implicit test of main method)
        assertDoesNotThrow(() -> {
            webTestClient
                    .get()
                    .uri("/")
                    .exchange()
                    .expectStatus().isOk();
        });
    }

    @Test
    @DisplayName("Should test path variable extraction in handleGreeting")
    void testPathVariableExtraction() {
        // Test various path variable scenarios to ensure proper extraction
        String[] testNames = {"user1", "admin", "test-user", "user_123", "αβγ"};
        
        for (String name : testNames) {
            webTestClient
                    .get()
                    .uri("/" + name)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class)
                    .value(response -> {
                        assertTrue(response.contains(name));
                        assertEquals("hello, " + name + "!", response);
                    });
        }
    }

    @Test
    @DisplayName("Should test router function composition")
    void testRouterFunctionComposition() {
        // Test that all routes are properly composed using andRoute
        RouterFunction<ServerResponse> routes = reactiveServiceApplication.routes();
        assertNotNull(routes);
        
        // Verify that the router function handles different patterns correctly
        // This indirectly tests the andRoute composition
        
        // Test root route
        webTestClient.get().uri("/").exchange().expectStatus().isOk();
        
        // Test parameterized route
        webTestClient.get().uri("/test").exchange().expectStatus().isOk();
        
        // Test fallback route
        webTestClient.get().uri("/any/path").exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Should test bodyValue method usage")
    void testBodyValueMethodUsage() {
        // Test that bodyValue() is used correctly in all handlers
        
        // Test root endpoint bodyValue
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .isEqualTo("hi");
        
        // Test greeting endpoint bodyValue
        webTestClient
                .get()
                .uri("/testuser")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .isEqualTo("hello, testuser!");
        
        // Test fallback endpoint bodyValue
        webTestClient
                .get()
                .uri("/api/test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .isEqualTo("fallback");
    }

    @Test
    @DisplayName("Should test application configuration")
    void testApplicationConfiguration() {
        // Test that the application is configured correctly
        // This tests the main method's configuration setup
        
        // Verify Spring Boot application annotation is working
        assertTrue(reactiveServiceApplication.getClass().isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
        
        // Verify that the routes bean method is annotated correctly
        try {
            var routesMethod = ReactiveServiceApplication.class.getMethod("routes");
            assertTrue(routesMethod.isAnnotationPresent(
                org.springframework.context.annotation.Bean.class));
        } catch (NoSuchMethodException e) {
            fail("routes() method should exist and be annotated with @Bean");
        }
    }

    @Test
    @DisplayName("Should test reactive response handling")
    void testReactiveResponseHandling() {
        // Test that all endpoints return proper reactive responses
        
        // Test response timing (should be fast due to reactive nature)
        long startTime = System.currentTimeMillis();
        
        webTestClient
                .get()
                .uri("/performance-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("hello, performance-test!");
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Reactive responses should be fast (under 1000ms)
        assertTrue(responseTime < 1000, "Response took too long: " + responseTime + "ms");
    }
}