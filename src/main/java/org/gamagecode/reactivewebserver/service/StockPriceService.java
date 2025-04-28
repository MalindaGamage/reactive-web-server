package org.gamagecode.reactivewebserver.service;

import org.gamagecode.reactivewebserver.model.StockPrice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StockPriceService {

    private static final String SYMBOL = "AAPL";

    public Flux<StockPrice> getPriceStream() {
        return Flux.interval(Duration.ofSeconds(1)) // Emits a tick every second
                .map(tick -> generateMultiplePrices()) // Now generate multiple prices
                .flatMapIterable(prices -> prices) // Flatten List<StockPrice> into Flux<StockPrice>
                .filter(price -> price.price() > 148) // Java Streams + Reactive: Filter prices > 148
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2))) // Resilient: retry on error
                .share(); // Allows broadcasting to multiple subscribers
        // Ensures the stream is hot and shared across multiple subscribers (clients)
    }

    private double randomPrice() {
        return 150 + ThreadLocalRandom.current().nextDouble(-5, 5);
    }

    private List<StockPrice> generateMultiplePrices() {
        return IntStream.range(0, 5) // generate 5 stock prices each second
                .mapToObj(i -> new StockPrice(
                        SYMBOL,
                        randomPrice(),
                        Instant.now()))
                .collect(Collectors.toList());
    }

}
