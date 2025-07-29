package daggerok;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Spring Boot 6.1 reactive web application that provides REST endpoints using functional routing.
 * 
 * <p>This application demonstrates modern Spring Boot practices with:
 * <ul>
 *   <li>Functional routing instead of controller-based routing</li>
 *   <li>Reactive programming with WebFlux</li>
 *   <li>JDK 17 features and syntax</li>
 * </ul>
 */
@SpringBootApplication
public class ReactiveServiceApplication {

    /**
     * Defines the routing configuration for the reactive web application.
     *
     * @return RouterFunction that handles HTTP requests and returns ServerResponse
     */
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route(GET("/"), this::handleRoot)
                .andRoute(GET("/{name}"), this::handleGreeting)
                .andRoute(GET("/**"), this::handleFallback);
    }

    /**
     * Handles requests to the root endpoint.
     *
     * @param request the server request
     * @return ServerResponse with "hi" message
     */
    private Mono<ServerResponse> handleRoot(org.springframework.web.reactive.function.server.ServerRequest request) {
        return ok().body(Mono.just("hi"), String.class);
    }

    /**
     * Handles requests to the greeting endpoint with a name parameter.
     *
     * @param request the server request containing the name path variable
     * @return ServerResponse with personalized greeting
     */
    private Mono<ServerResponse> handleGreeting(org.springframework.web.reactive.function.server.ServerRequest request) {
        var name = request.pathVariable("name");
        var greeting = "hello, " + name + "!";
        return ok().body(Mono.just(greeting), String.class);
    }

    /**
     * Handles all other requests as fallback.
     *
     * @param request the server request
     * @return ServerResponse with fallback message
     */
    private Mono<ServerResponse> handleFallback(org.springframework.web.reactive.function.server.ServerRequest request) {
        return ok().body(Mono.just("fallback"), String.class);
    }

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        var app = new SpringApplication(ReactiveServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "3000"));
        app.run(args);
    }
}