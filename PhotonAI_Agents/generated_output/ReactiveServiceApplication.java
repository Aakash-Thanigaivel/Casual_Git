package daggerok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Spring Boot 6.1 reactive web service application.
 * 
 * <p>This application demonstrates functional routing with Spring WebFlux,
 * following Google Java Style Guidelines.
 */
@SpringBootApplication
public class ReactiveServiceApplication {

  /**
   * Defines the reactive routes for the application.
   *
   * @return router function with defined routes
   */
  @Bean
  public RouterFunction<ServerResponse> routes() {
    return route(GET("/"), request -> 
            ok().bodyValue("hi"))
        .andRoute(GET("/{name}"), request -> {
          var name = request.pathVariable("name");
          return ok().bodyValue("hello, " + name + "!");
        })
        .andRoute(GET("/**"), request -> 
            ok().bodyValue("fallback"));
  }

  /**
   * Main method to start the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    new SpringApplicationBuilder(ReactiveServiceApplication.class)
        .properties(Map.of("server.port", "3000"))
        .run(args);
  }
}