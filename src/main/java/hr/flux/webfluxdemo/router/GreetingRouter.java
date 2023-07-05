package hr.flux.webfluxdemo.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import hr.flux.webfluxdemo.handler.GreetingHandler;

/**
 * Router
 * - URI 상의 path를 listen 하고, 핸들러에서 제공하는 값을 리턴
 */
@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

	@Bean
	public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {

		return RouterFunctions.route(GET("/greeting")
			.and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello);
	}
}
