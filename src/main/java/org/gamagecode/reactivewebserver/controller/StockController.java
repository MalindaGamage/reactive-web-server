package org.gamagecode.reactivewebserver.controller;

import lombok.RequiredArgsConstructor;
import org.gamagecode.reactivewebserver.model.StockPrice;
import org.gamagecode.reactivewebserver.service.StockPriceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockPriceService stockPriceService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // Tells the browser this is a Server-Sent Event (SSE) stream
    public Flux<StockPrice> streamPrices() { //  Returns a reactive stream of stock prices (not just one object or a list).
        return stockPriceService.getPriceStream()
                .onErrorResume(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                    return Flux.empty(); // Return an empty stream on error, fallback if error (resilient principle)
                });
    }

    // Mono Endpoint : returns a single stock price
    @GetMapping(value = "/singlePrice") // No need for SSE, just a single object
    public Mono<StockPrice> getSinglePrice() {
        return stockPriceService.getSinglePrice()
                .onErrorResume(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                    return Mono.empty(); // Return an empty Mono on error
                });
    }
}
