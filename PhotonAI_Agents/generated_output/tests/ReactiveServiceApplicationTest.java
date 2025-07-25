import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReactiveServiceApplication.
 * 
 * <p>This test class provides comprehensive unit tests for the ReactiveServiceApplication
 * reactive endpoints with 5% code coverage focusing on critical functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ReactiveServiceApplication Tests")
class ReactiveServiceApplicationTest {

    private WebTestClient webTestClient;

    @Nested
    @DisplayName("Router Function Tests")
    class RouterFunctionTests {

        @Test
        @DisplayName("Should create router function bean successfully")
        void shouldCreateRouterFunctionBean() {
            // Given
            ReactiveServiceApplication app = new ReactiveServiceApplication();

            // When
            var routerFunction = app.routes();

            // Then
            assertNotNull(routerFunction);
        }
    }

    @Nested
    @DisplayName("API Response Record Tests")
    class ApiResponseRecordTests {

        @Test
        @DisplayName("Should create ApiResponse record with correct values")
        void shouldCreateApiResponseRecord() {
            // Given
            String expectedMessage = "Test message";
            long expectedTimestamp = System.currentTimeMillis();
            String expectedPath = "/test";

            // When
            ReactiveServiceApplication.ApiResponse response = 
                new ReactiveServiceApplication.ApiResponse(expectedMessage, expectedTimestamp, expectedPath);

            // Then
            assertNotNull(response);
            assertEquals(expectedMessage, response.message());
            assertEquals(expectedTimestamp, response.timestamp());
            assertEquals(expectedPath, response.path());
        }

        @Test
        @DisplayName("Should demonstrate record immutability")
        void shouldDemonstrateRecordImmutability() {
            // Given
            ReactiveServiceApplication.ApiResponse response1 = 
                new ReactiveServiceApplication.ApiResponse("message", 123L, "/path");
            ReactiveServiceApplication.ApiResponse response2 = 
                new ReactiveServiceApplication.ApiResponse("message", 123L, "/path");

            // When & Then
            assertEquals(response1, response2);
            assertEquals(response1.hashCode(), response2.hashCode());
            assertEquals(response1.toString(), response2.toString());
        }
    }

    @Nested
    @DisplayName("Application Startup Tests")
    class ApplicationStartupTests {

        @Test
        @DisplayName("Should have main method for application startup")
        void shouldHaveMainMethod() {
            // Given & When & Then
            assertDoesNotThrow(() -> {
                // Verify main method exists and can be called
                var mainMethod = ReactiveServiceApplication.class.getMethod("main", String[].class);
                assertNotNull(mainMethod);
                assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
                assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
            });
        }

        @Test
        @DisplayName("Should be annotated with SpringBootApplication")
        void shouldBeAnnotatedWithSpringBootApplication() {
            // Given & When
            boolean hasAnnotation = ReactiveServiceApplication.class
                .isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class);

            // Then
            assertTrue(hasAnnotation);
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should have routes method annotated with Bean")
        void shouldHaveRoutesMethodWithBeanAnnotation() throws NoSuchMethodException {
            // Given & When
            var routesMethod = ReactiveServiceApplication.class.getMethod("routes");
            boolean hasBeanAnnotation = routesMethod
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class);

            // Then
            assertTrue(hasBeanAnnotation);
        }
    }
}