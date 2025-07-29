import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReactiveServiceApplication.
 * 
 * <p>This test class provides unit tests for the ReactiveServiceApplication,
 * covering the reactive web endpoints and routing functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig(ReactiveServiceApplication.class)
class ReactiveServiceApplicationTest {

  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:3000")
        .build();
  }

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
  @DisplayName("Should return personalized greeting for name endpoint")
  void testNameEndpoint() {
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
  @DisplayName("Should return fallback message for unmatched routes")
  void testFallbackEndpoint() {
    // When & Then
    webTestClient
        .get()
        .uri("/some/unknown/path")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }

  @Test
  @DisplayName("Should handle special characters in name parameter")
  void testNameEndpointWithSpecialCharacters() {
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
  void testNameEndpointWithEmptyName() {
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
  @DisplayName("Should return fallback for deeply nested paths")
  void testDeeplyNestedFallbackPath() {
    // When & Then
    webTestClient
        .get()
        .uri("/api/v1/users/123/profile/settings")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("fallback");
  }

  @Test
  @DisplayName("Should handle numeric name parameter")
  void testNameEndpointWithNumericName() {
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
  @DisplayName("Should verify application context loads successfully")
  void testApplicationContextLoads() {
    // This test verifies that the Spring Boot application context loads without errors
    // The @SpringBootTest annotation ensures the full application context is loaded
    assertTrue(true, "Application context should load successfully");
  }
}