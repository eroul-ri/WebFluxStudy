package hr.flux.webfluxdemo.handler;

import hr.flux.webfluxdemo.model.Greeting;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Handler
 * - 라우터를 전달된 요청을 처리하는 역할
 *
 * Mono, Flux
 * - 비동기적인 데이터 스트림 처리
 * - 응답을 담아서 반환
 * - Mono는 0~1개의 결과를 처리하기위한 Reactor 객체
 * - Flux의 경우 0~N개의 결과를 처리하기위한 객체
 */
@Component
public class GreetingHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Greeting("Hello, Spring Flux!")));
    }
}
