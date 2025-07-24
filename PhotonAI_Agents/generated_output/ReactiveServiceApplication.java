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
 * Reactive Spring Boot application providing simple HTTP endpoints.
 * 
 * <p>This application demonstrates modern Spring Boot 6.1 practices with
 * reactive programming using WebFlux and functional routing.
 */
@SpringBootApplication
public class ReactiveServiceApplication {

  /**
   * Defines the reactive routes for the application.
   *
   * @return RouterFunction containing all application routes
   */
  @Bean
  public RouterFunction<ServerResponse> routes() {
    return route(GET("/"), this::handleRoot)
        .andRoute(GET("/{name}"), this::handleGreeting)
        .andRoute(GET("/**"), this::handleFallback);
  }

  /**
   * Handles the root endpoint.
   *
   * @param request the server request
   * @return a Mono containing the server response
   */
  private Mono<ServerResponse> handleRoot(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    return ok().bodyValue("hi");
  }

  /**
   * Handles the greeting endpoint with path variable.
   *
   * @param request the server request containing the name path variable
   * @return a Mono containing the personalized greeting response
   */
  private Mono<ServerResponse> handleGreeting(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var name = request.pathVariable("name");
    var greeting = String.format("hello, %s!", name);
    return ok().bodyValue(greeting);
  }

  /**
   * Handles the fallback endpoint for unmatched routes.
   *
   * @param request the server request
   * @return a Mono containing the fallback response
   */
  private Mono<ServerResponse> handleFallback(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    return ok().bodyValue("fallback");
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