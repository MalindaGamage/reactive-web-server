package org.gamagecode.reactivewebserver.service;

import org.gamagecode.reactivewebserver.model.StockPrice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class StockPriceService {

    private static final String SYMBOL = "AAPL";

    public Flux<StockPrice> getPriceStream() {
        return Flux.interval(Duration.ofSeconds(1)) // Emits a tick every second
                .map(tick -> new StockPrice( // Maps each tick to a StockPrice object
                        SYMBOL,
                        randomPrice(), // Creates a simulated price with random fluctuations
                        Instant.now()
                ))
                .share(); // Allows broadcasting to multiple subscribers
        // Ensures the stream is hot and shared across multiple subscribers (clients)
    }

    private double randomPrice() {
        return 150 + ThreadLocalRandom.current().nextDouble(-5, 5);
    }

}
