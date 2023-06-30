package hr.flux.webfluxdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingClientTest {

    @Autowired
    WebTestClient webTestClient;
    @Test
    public void testGreeting() {
        webTestClient.get()
                     .uri("/greeting")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk();
    }

}
