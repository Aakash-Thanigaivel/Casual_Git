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

@SpringBootApplication
public class ReactiveServiceApplication {

  @Bean
  public RouterFunction<ServerResponse> routes() {
    return route(GET("/"), this::handleRoot)
        .andRoute(GET("/{name}"), this::handleGreeting)
        .andRoute(GET("/**"), this::handleFallback);
  }

  private Mono<ServerResponse> handleRoot(var request) {
    return ok().body(Mono.just("hi"), String.class);
  }

  private Mono<ServerResponse> handleGreeting(var request) {
    var name = request.pathVariable("name");
    var greeting = "hello, " + name + "!";
    return ok().body(Mono.just(greeting), String.class);
  }

  private Mono<ServerResponse> handleFallback(var request) {
    return ok().body(Mono.just("fallback"), String.class);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ReactiveServiceApplication.class)
        .properties(Map.of("server.port", "3000"))
        .run(args);
  }
}