package com.github.rubenqba.databursatil.models.filter;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

public record HistoricalFilter(String ticker, LocalDate from, LocalDate to) {

    public HistoricalFilter {
        if (!StringUtils.hasText(ticker)) {
            throw new IllegalArgumentException("ticker cannot be empty");
        }
        Objects.requireNonNull(from, "You must specify initial date to filter");
        if (Objects.isNull(to)) {
            to = LocalDate.now();
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Initial date cannot be after final date");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String ticker;
        private LocalDate from;
        private LocalDate to;

        public Builder() {
        }

        public Builder ticker(String val) {
            ticker = val;
            return this;
        }

        public Builder from(LocalDate val) {
            from = val;
            return this;
        }

        public Builder to(LocalDate val) {
            to = val;
            return this;
        }

        public HistoricalFilter build() {
            return new HistoricalFilter(this.ticker, from, to);
        }
    }
}
