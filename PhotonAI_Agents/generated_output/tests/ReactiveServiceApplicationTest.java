package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

/**
 * Integration tests for ReactiveServiceApplication.
 * Tests cover the reactive REST endpoints functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ReactiveServiceApplicationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  @DisplayName("Should return 'hi' for root endpoint")
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
  void testNameEndpoint() {
    String name = "John";
    
    webTestClient.get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, John!");
  }

  @Test
  @DisplayName("Should return 'fallback' for unknown paths")
  void testFallbackEndpoint() {
    webTestClient.get()
        .uri("/unknown/path/here")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }

  @Test
  @DisplayName("Should handle special characters in name parameter")
  void testNameEndpointWithSpecialCharacters() {
    String name = "John%20Doe";
    
    webTestClient.get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, John%20Doe!");
  }

  @Test
  @DisplayName("Should return fallback for nested unknown paths")
  void testNestedFallbackEndpoint() {
    webTestClient.get()
        .uri("/api/v1/unknown")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }
}