import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Test configuration for reactive service application tests.
 * 
 * Provides test-specific beans and configurations for unit testing
 * the reactive endpoints and routing functionality.
 */
@TestConfiguration
public class ReactiveServiceTestConfig {

    /**
     * Creates a WebTestClient bean for testing reactive endpoints.
     *
     * @param routerFunction the router function to bind to the test client
     * @return configured WebTestClient for testing
     */
    @Bean
    public WebTestClient webTestClient(RouterFunction<ServerResponse> routerFunction) {
        return WebTestClient.bindToRouterFunction(routerFunction)
            .configureClient()
            .baseUrl("http://localhost:3000")
            .build();
    }
}