package hr.flux.webfluxdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WebFlux
 * Event Driven 방식으로 요청을 처리
 * Netty - 비동기 non-blocking reactive
 */
@SpringBootApplication
public class WebFluxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxDemoApplication.class, args);
    }

}
