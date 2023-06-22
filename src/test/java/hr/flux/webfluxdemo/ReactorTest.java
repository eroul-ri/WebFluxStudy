package hr.flux.webfluxdemo;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

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
    public String [] getColorArray() {
        return new String[] {"RED", "BLACK", "BLUE", "PINK"};
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

    public Flux<String> arrayToFlux(String [] arr) {
        return Flux.fromArray(arr);
    }

    @Test
    public void mergeFluxes() {
        String [] colors = getColorArray();
        String [] fruit = getFruitArray();

        Flux<String> colorFlux = arrayToFlux(colors);
        Flux<String> fruitFlux = arrayToFlux(fruit);

        Flux<String> mergedFlux = colorFlux.mergeWith(fruitFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("RED")
                .expectNext("BLACK")
                .expectNext("BLUE")
                .expectNext("PINK")
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .verifyComplete();
    }

    @Test
    public void mergeFluxesDelay() {
        String [] colors = getColorArray();
        String [] fruit = getFruitArray();

        Flux<String> colorFlux = arrayToFlux(colors)
                                    .delayElements(Duration.ofMillis(500));
        Flux<String> fruitFlux = arrayToFlux(fruit)
                                    .delaySubscription(Duration.ofMillis(250))
                                    .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = colorFlux.mergeWith(fruitFlux);

        // mergedFlux.subscribe(System.out::println); delay를 주게 되면, 다른 스레드에서 delayElements를 가짐. (Default Parallel Scheduler)
        StepVerifier.create(mergedFlux)
                .expectNext("RED")
                .expectNext("Apple")
                .expectNext("BLACK")
                .expectNext("Orange")
                .expectNext("BLUE")
                .expectNext("Grape")
                .expectNext("PINK")
                .verifyComplete();
    }

    @Test
    public void zipFluxes() {
        String [] colors = getColorArray();
        String [] fruit = getFruitArray();

        Flux<String> colorFlux = arrayToFlux(colors);
        Flux<String> fruitFlux = arrayToFlux(fruit);
        // 한 항목씩 번갈아가져 옴.
        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(colorFlux, fruitFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches((pre) -> pre.getT1().equals("RED") && pre.getT2().equals("Apple"))
                .expectNextMatches((pre) -> pre.getT1().equals("BLACK") && pre.getT2().equals("Orange"))
                .expectNextMatches((pre) -> pre.getT1().equals("BLUE") && pre.getT2().equals("Grape"))
                .verifyComplete();
    }
}
