package hr.flux.webfluxdemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

public class ReactorTest {
    @Test
    public void testMonoJust() {
        Mono.just("Greeting")
                .map(v-> v.toUpperCase())
                .map(v-> "HI ".concat(v).concat("!!"))
                .subscribe(System.out::println);
    }

    @Test
    public void createFluxJust() {
        Flux<String> fruit = Flux.just("Apple", "Orange", "Grape");

        StepVerifier.create(fruit)
                    .expectNext("Apple")
                    .expectNext("Orange")
                    .expectNext("Grape")
                    .verifyComplete();
    }

    public String [] getFruitArray() {
        return new String[] {"Apple", "Orange", "Grape"};
    }
    @Test
    public void createFluxFromArray() {
        Flux<String> flux = Flux.fromArray(getFruitArray());

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .verifyComplete();
    }

    @Test
    public void createFluxFromIterable() {
        Flux<String> flux = Flux.fromIterable(Arrays.asList(getFruitArray()));

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .verifyComplete();
    }

    @Test
    public void createFluxFromStream() {
        Flux<String> flux = Flux.fromStream(Arrays.stream(getFruitArray()));

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .verifyComplete();
    }

    @Test
    public void createFluxRange() {
        Flux<Integer> flux = Flux.range(1, 4);

        StepVerifier.create(flux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void createFluxInterval() {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1))
                                                        .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }
}
