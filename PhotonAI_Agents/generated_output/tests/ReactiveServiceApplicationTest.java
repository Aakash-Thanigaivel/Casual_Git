import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

/**
 * Integration test cases for ReactiveServiceApplication.
 * 
 * <p>This test class provides integration tests for the reactive web endpoints
 * including root, parameterized greeting, and fallback routes.
 */
@SpringBootTest(classes = daggerok.ReactiveServiceApplication.class)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {"server.port=0"})
class ReactiveServiceApplicationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  @DisplayName("Should return greeting message for root endpoint")
  void testRootEndpoint() {
    // When & Then
    webTestClient
        .get()
        .uri("/")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hi");
  }

  @Test
  @DisplayName("Should return personalized greeting for name parameter")
  void testGreetingEndpointWithName() {
    // Given
    String name = "John";
    
    // When & Then
    webTestClient
        .get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, John!");
  }

  @Test
  @DisplayName("Should return fallback message for unmatched paths")
  void testFallbackEndpoint() {
    // When & Then
    webTestClient
        .get()
        .uri("/unknown/path")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }

  @Test
  @DisplayName("Should handle special characters in name parameter")
  void testGreetingEndpointWithSpecialCharacters() {
    // Given
    String name = "test-user_123";
    
    // When & Then
    webTestClient
        .get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, test-user_123!");
  }

  @Test
  @DisplayName("Should handle empty name parameter")
  void testGreetingEndpointWithEmptyName() {
    // Given
    String name = "";
    
    // When & Then
    webTestClient
        .get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, !");
  }

  @Test
  @DisplayName("Should handle numeric name parameter")
  void testGreetingEndpointWithNumericName() {
    // Given
    String name = "12345";
    
    // When & Then
    webTestClient
        .get()
        .uri("/{name}", name)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello, 12345!");
  }

  @Test
  @DisplayName("Should handle deeply nested fallback paths")
  void testDeeplyNestedFallbackPath() {
    // When & Then
    webTestClient
        .get()
        .uri("/api/v1/users/profile/settings")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }

  @Test
  @DisplayName("Should handle path with query parameters")
  void testFallbackEndpointWithQueryParams() {
    // When & Then
    webTestClient
        .get()
        .uri("/unknown?param1=value1&param2=value2")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }
}