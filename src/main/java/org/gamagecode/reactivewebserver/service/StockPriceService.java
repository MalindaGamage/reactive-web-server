package org.gamagecode.reactivewebserver.service;

import org.gamagecode.reactivewebserver.model.StockPrice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
                .map(stockPrice -> {
                    // Apply transformation using map (Synchronously)
                    return new StockPrice(
                            stockPrice.symbol(),
                            stockPrice.price() + 2, // Simulate price increase
                            stockPrice.time()); // Update the time to now
                })
                .share(); // Allows broadcasting to multiple subscribers
        // Ensures the stream is hot and shared across multiple subscribers (clients)
    }

    // Example of Mono: Returning a single StockPrice (for 0-1 items)
    public Mono<StockPrice> getSinglePrice() {
        return Mono.just(new StockPrice(SYMBOL, randomPrice(), Instant.now())) // Create a Mono with a single StockPrice
                .map(stockPrice -> {
                    // Apply transformation using map (synchronously)
                    return new StockPrice(stockPrice.symbol(), stockPrice.price() + 5, stockPrice.time()); // Modify price
                })
                .filter(price -> price.price() > 150) // Filter prices > 150
                .doOnTerminate(() -> System.out.println("Price request completed")); // Log when the stream is terminated
    }

    // flatMap: Simulate fetching more data asynchronously for each stock price
    public Flux<StockPrice> fetchAdditionalStockData(){
        return Flux.interval(Duration.ofSeconds(1)) // Emits every Seconds
                .map(tick -> generateMultiplePrices()) // Generate multiple prices
                .flatMapIterable(prices -> prices) // Flatten List<StockPrice> into Flux<StockPrice>
                .flatMap(this::simulateAsyncAPI)
                .share(); // Share the stream across multiple subscribers
    }

    private Mono<StockPrice> simulateAsyncAPI(StockPrice price) {
        // Simulate async API call (e.g., fetching additional data for the stock)
        return Mono.just(price)
                .delayElement(Duration.ofMillis(500)) // Simulate async delay (like an external API call)
                .map(p -> new StockPrice(p.symbol(), p.price() + 3, p.time())); // Transform the price asynchronously
    }

    private double randomPrice() {
        return 150 + ThreadLocalRandom.current().nextDouble(-5, 5); // Simulate price fluctuations
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
