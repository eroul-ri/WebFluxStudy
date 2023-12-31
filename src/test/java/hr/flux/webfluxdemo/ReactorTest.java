package hr.flux.webfluxdemo;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import hr.flux.webfluxdemo.model.Greeting;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

/**
 * Publisher - 발행자
 * Subscriber - 구독자
 * Subscription - 구독
 * Processor - 프로세서
 */
public class ReactorTest {

	public String[] getFruitArray() {
		return new String[] {"Apple", "Orange", "Grape"};
	}

	public String[] getColorArray() {
		return new String[] {"RED", "BLACK", "BLUE", "PINK"};
	}

	@Test
	public void testMonoJust() {
		Mono.just("Greeting")
			.map(v -> v.toUpperCase())
			.map(v -> "HI ".concat(v).concat("!!"))
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

	public Flux<String> arrayToFlux(String[] arr) {
		return Flux.fromArray(arr);
	}

	@Test
	public void mergeFluxes() {
		String[] colors = getColorArray();
		String[] fruit = getFruitArray();

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
		String[] colors = getColorArray();
		String[] fruit = getFruitArray();

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
		String[] colors = getColorArray();
		String[] fruit = getFruitArray();

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

	@Test
	public void zipFluxesString() {
		String[] colors = getColorArray();
		String[] fruit = getFruitArray();

		Flux<String> colorFlux = arrayToFlux(colors);
		Flux<String> fruitFlux = arrayToFlux(fruit);
		// 한 항목씩 번갈아가져 옴.
		Flux<String> zippedFlux = Flux.zip(colorFlux, fruitFlux, (f, s) -> f.concat(" ").concat(s));

		StepVerifier.create(zippedFlux)
					.expectNext("RED Apple")
					.expectNext("BLACK Orange")
					.expectNext("BLUE Grape")
					.verifyComplete();
	}

	@Test
	public void firstFlux() {
		Flux<String> slowFlux = Flux.just("snail", "turtle")
									.delaySubscription(Duration.ofMillis(300));
		Flux<String> fastFlux = Flux.just("rabbit", "cheetah");

		Flux<String> firstFlux = Flux.firstWithSignal(slowFlux, fastFlux);

		StepVerifier.create(firstFlux)
					.expectNext("rabbit")
					.expectNext("cheetah")
					.verifyComplete();
	}

	@Test
	public void skipFluxItem() {
		Flux<String> flux = Flux.just("one skip", "two skip", "three skip", "four", "five", "six")
								.skip(3);
		//        flux.subscribe(System.out::println);
		StepVerifier.create(flux)
					.expectNext("four")
					.expectNext("five")
					.expectNext("six")
					.verifyComplete();
	}

	@Test
	public void skipFluxDelay() {
		Flux<String> flux = Flux.just("one skip", "two skip", "three skip", "four", "five", "six")
								.delayElements(Duration.ofSeconds(1)) // ns간 지연방출
								.skip(Duration.ofSeconds(3)); // ns skip
		StepVerifier.create(flux)
					.expectNext("three skip")
					.expectNext("four")
					.expectNext("five")
					.expectNext("six")
					.verifyComplete();
	}

	@Test
	public void takeFlux() {
		Flux<String> colorFlux = Flux.just("blue", "red", "violet", "pink")
									 .take(3);

		StepVerifier.create(colorFlux)
					.expectNext("blue", "red", "violet")
					.verifyComplete();
	}

	@Test
	public void takeFluxDelay() {
		Flux<String> colorFlux = Flux.just("blue", "red", "violet", "pink")
									 .delayElements(Duration.ofSeconds(1))
									 .take(Duration.ofMillis(2500));

		StepVerifier.create(colorFlux)
					.expectNext("blue", "red")
					.verifyComplete();
	}

	@Test
	public void filterFlux() {
		Flux<String> messageFlux = Flux.just("Hi Flux!", "hi", "hello", "nice", "good", "perfect", "perfect score")
									   .filter(m -> m.contains(" "));

		StepVerifier.create(messageFlux)
					.expectNext("Hi Flux!", "perfect score")
					.verifyComplete();
	}

	@Test
	public void distinctFlux() {
		Flux<String> messageFlux = Flux.just("hello", "hi", "hi", "perfect", "hello")
									   .distinct();

		StepVerifier.create(messageFlux)
					.expectNext("hello", "hi", "perfect")
					.verifyComplete();
	}

	/**
	 * map - synchronized
	 */
	@Test
	public void mapFlux() {
		Flux<Greeting> greetingFlux = Flux.just("hi", "hello")
										  .map(Greeting::new);

		greetingFlux.subscribe(System.out::println);
	}

	@Test
	public void flatMapFlux() {
		Flux<Greeting> greetingFlux = Flux.just("hi", "hello")
										  .flatMap(
											  v -> Mono.just(v).map(n -> new Greeting(n))
										  )
										  .subscribeOn(Schedulers.parallel());

		List<Greeting> greetings = Arrays.asList(new Greeting("hi"), new Greeting("hello"));

		StepVerifier.create(greetingFlux)
					.expectNextMatches(pre -> greetings.get(0).getMessage().equals(pre.getMessage()))
					.expectNextMatches(pre -> greetings.get(1).getMessage().equals(pre.getMessage()))
					.verifyComplete();
	}
}
