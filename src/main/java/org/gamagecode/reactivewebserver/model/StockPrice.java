package org.gamagecode.reactivewebserver.model;

import java.time.Instant;

public record StockPrice(String symbol, double price, Instant time) {
}

// Java 21 record = shorthand for immutable data objects