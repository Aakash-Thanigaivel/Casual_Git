package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Reactive Spring Boot 6.1 application providing REST endpoints.
 * 
 * <p>This application demonstrates modern Spring Boot 6.1 features with reactive programming
 * using WebFlux. It provides three main endpoints for handling HTTP GET requests.
 */
@SpringBootApplication
public class ReactiveServiceApplication {

  /**
   * Response record for structured API responses.
   * 
   * @param message the response message
   * @param timestamp the response timestamp
   * @param path the request path
   */
  public record ApiResponse(String message, long timestamp, String path) {}

  /**
   * Configures reactive router functions for handling HTTP requests.
   * 
   * <p>This method sets up three routes:
   * <ul>
   *   <li>Root endpoint ("/") - returns welcome message</li>
   *   <li>Named endpoint ("/{name}") - returns personalized greeting</li>
   *   <li>Fallback endpoint ("/**") - catches all other requests</li>
   * </ul>
   *
   * @return RouterFunction configured with all application routes
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
   * @return ServerResponse with welcome message
   */
  private Mono<ServerResponse> handleRoot(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var response = new ApiResponse(
        "Welcome to Reactive Service API", 
        System.currentTimeMillis(), 
        request.path());
    
    return ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(response), ApiResponse.class);
  }

  /**
   * Handles requests to the named greeting endpoint.
   *
   * @param request the server request containing path variable
   * @return ServerResponse with personalized greeting
   */
  private Mono<ServerResponse> handleGreeting(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var name = request.pathVariable("name");
    var message = "Hello, %s! Welcome to our reactive service.".formatted(name);
    var response = new ApiResponse(message, System.currentTimeMillis(), request.path());
    
    return ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(response), ApiResponse.class);
  }

  /**
   * Handles all other requests as fallback.
   *
   * @param request the server request
   * @return ServerResponse with fallback message
   */
  private Mono<ServerResponse> handleFallback(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var fallbackMessage = """
        Endpoint not found. Available endpoints:
        - GET / : Welcome message
        - GET /{name} : Personalized greeting
        """;
    var response = new ApiResponse(fallbackMessage, System.currentTimeMillis(), request.path());
    
    return ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(response), ApiResponse.class);
  }

  /**
   * Application entry point.
   * 
   * <p>Starts the Spring Boot application with default configuration.
   * The server will run on port 8080 by default, or can be configured
   * via application.properties or environment variables.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    var app = new SpringApplication(ReactiveServiceApplication.class);
    
    // Configure default properties programmatically
    var defaultProperties = java.util.Map.of(
        "server.port", "3000",
        "spring.application.name", "reactive-service",
        "logging.level.daggerok", "INFO"
    );
    
    app.setDefaultProperties(defaultProperties);
    app.run(args);
  }
}