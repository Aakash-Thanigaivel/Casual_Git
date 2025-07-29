package daggerok;

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
 * Spring Boot 6.1 reactive web application.
 * 
 * <p>This application demonstrates modern Spring Boot reactive programming
 * using WebFlux with functional routing. It provides three endpoints:
 * a greeting endpoint, a personalized greeting, and a fallback route.
 */
@SpringBootApplication
public class ReactiveServiceApplication {

  /**
   * Configures the reactive routes for the application.
   *
   * @return RouterFunction containing all application routes
   */
  @Bean
  public RouterFunction<ServerResponse> routes() {
    return route(GET("/"), this::handleGreeting)
        .andRoute(GET("/{name}"), this::handlePersonalizedGreeting)
        .andRoute(GET("/**"), this::handleFallback);
  }

  /**
   * Handles the root greeting endpoint.
   *
   * @param request the server request
   * @return a ServerResponse with greeting message
   */
  private Mono<ServerResponse> handleGreeting(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    return ok().body(Mono.just("hi"), String.class);
  }

  /**
   * Handles personalized greeting with path variable.
   *
   * @param request the server request containing the name path variable
   * @return a ServerResponse with personalized greeting
   */
  private Mono<ServerResponse> handlePersonalizedGreeting(
      org.springframework.web.reactive.function.server.ServerRequest request) {
    var name = request.pathVariable("name");
    var greeting = "hello, " + name + "!";
    return ok().body(Mono.just(greeting), String.class);
  }

  /**
   * Handles fallback route for unmatched requests.
   *
   * @param request the server request
   * @return a ServerResponse with fallback message
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
    app.setDefaultProperties(java.util.Map.of("server.port", "3000"));
    app.run(args);
  }
}