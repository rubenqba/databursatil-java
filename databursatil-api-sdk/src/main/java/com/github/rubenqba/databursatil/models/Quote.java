package com.github.rubenqba.databursatil.models;

public record Quote(String ticker, StockExchange exchange, QuoteDetails details) {
}
