package com.github.rubenqba.databursatil.models.filter;

import com.github.rubenqba.databursatil.models.StockExchange;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

public record QuoteFilter(Set<String> tickers, EnumSet<StockExchange> exchanges) {

    public QuoteFilter(Set<String> tickers) {
        this(tickers, EnumSet.of(StockExchange.BMV));
    }

    public QuoteFilter {
        if (tickers == null || tickers.isEmpty()) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (exchanges == null) {
            exchanges = EnumSet.allOf(StockExchange.class);
        }
    }

    public static QuoteFilter.Builder builder() {
        return new QuoteFilter.Builder();
    }

    public static final class Builder {
        private Set<String> tickers = new TreeSet<>();
        private EnumSet<StockExchange> exchanges;

        public Builder() {
        }

        public Builder ticker(String val) {
            tickers.add(val);
            return this;
        }

        public Builder tickers(Set<String> val) {
            tickers = new TreeSet<>(val);
            return this;
        }

        public Builder useOnlyBMVExchange() {
            exchanges = EnumSet.of(StockExchange.BMV);
            return this;
        }

        public Builder useOnlyBIVAExchange() {
            exchanges = EnumSet.of(StockExchange.BIVA);
            return this;
        }

        public Builder useAllExchanges() {
            exchanges = EnumSet.allOf(StockExchange.class);
            return this;
        }

        public QuoteFilter build() {
            return new QuoteFilter(this.tickers, this.exchanges);
        }
    }
}
