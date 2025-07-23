package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReactiveServiceApplication
 * Provides 5% code coverage for basic functionality testing
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig(ReactiveServiceApplication.class)
class ReactiveServiceApplicationTest {

    @Test
    @DisplayName("Should start Spring Boot application context")
    void contextLoads() {
        // Basic context loading test
        assertDoesNotThrow(() -> {
            ReactiveServiceApplication.main(new String[]{});
        });
    }

    @Test
    @DisplayName("Should create routes bean")
    void testRoutesBean() {
        ReactiveServiceApplication app = new ReactiveServiceApplication();
        var routes = app.routes();
        assertNotNull(routes, "Routes bean should not be null");
    }

    @Test
    @DisplayName("Should handle root endpoint")
    void testRootEndpoint() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(new ReactiveServiceApplication().routes())
            .build();
            
        client.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hi");
    }

    @Test
    @DisplayName("Should handle named endpoint")
    void testNamedEndpoint() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(new ReactiveServiceApplication().routes())
            .build();
            
        client.get()
            .uri("/test")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("hello, test!");
    }

    @Test
    @DisplayName("Should handle fallback endpoint")
    void testFallbackEndpoint() {
        WebTestClient client = WebTestClient
            .bindToRouterFunction(new ReactiveServiceApplication().routes())
            .build();
            
        client.get()
            .uri("/any/random/path")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("fallback");
    }
}