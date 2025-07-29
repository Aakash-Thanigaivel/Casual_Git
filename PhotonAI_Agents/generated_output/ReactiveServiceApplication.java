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
 * Spring Boot 6.1 reactive web application with functional routing.
 * 
 * <p>This application demonstrates modern Spring WebFlux patterns using functional
 * endpoints and reactive programming with Project Reactor.
 */
@SpringBootApplication
public class ReactiveServiceApplication {

  /**
   * Configures the functional routes for the reactive web application.
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
   * Handles the root endpoint request.
   *
   * @param request the server request
   * @return ServerResponse with greeting message
   */
  private Mono<ServerResponse> handleRoot(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    return ok().body(Mono.just("hi"), String.class);
  }

  /**
   * Handles personalized greeting requests.
   *
   * @param request the server request containing path variable
   * @return ServerResponse with personalized greeting
   */
  private Mono<ServerResponse> handleGreeting(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var name = request.pathVariable("name");
    var greeting = "hello, " + name + "!";
    return ok().body(Mono.just(greeting), String.class);
  }

  /**
   * Handles fallback requests for unmatched routes.
   *
   * @param request the server request
   * @return ServerResponse with fallback message
   */
  private Mono<ServerResponse> handleFallback(
      org.springframework.web.reactive.function.server.ServerRequest request) {
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