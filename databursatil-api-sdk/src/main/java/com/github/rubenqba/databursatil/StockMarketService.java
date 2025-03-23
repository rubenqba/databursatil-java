package com.github.rubenqba.databursatil;

import com.github.rubenqba.databursatil.models.HistoricalPriceDetail;
import com.github.rubenqba.databursatil.models.Issuer;
import com.github.rubenqba.databursatil.models.Quote;
import com.github.rubenqba.databursatil.models.StockExchange;
import com.github.rubenqba.databursatil.models.api.HistoricalResponse;
import com.github.rubenqba.databursatil.models.api.IssuersResponse;
import com.github.rubenqba.databursatil.models.api.QuoteResponse;
import com.github.rubenqba.databursatil.models.error.ApiException;
import com.github.rubenqba.databursatil.models.filter.HistoricalFilter;
import com.github.rubenqba.databursatil.models.filter.QuoteFilter;
import com.github.rubenqba.databursatil.models.filter.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class StockMarketService {
    private static final Logger log = LoggerFactory.getLogger(StockMarketService.class);

    private final RestClient client;

    public StockMarketService(RestClient client) {
        this.client = client;
    }

    public List<Issuer> searchIssuers(SearchFilter filter) {
        try {
            final var body = client.get()
                    .uri(builder -> {
                        builder.path("/emisoras");
                        Optional.ofNullable(filter)
                                .ifPresent(f -> {
                                    if (StringUtils.hasText(f.query())) {
                                        builder.queryParam("letra", f.query());
                                    }
                                    if (Objects.nonNull(f.scopes())) {
                                        final var scopes = String.join(",", f.scopes().stream()
                                                .map(Enum::name)
                                                .map(String::toLowerCase)
                                                .toList());
                                        builder.queryParam("mercado", scopes);
                                    }
                                });
                        return builder.build();
                    })
                    .retrieve()
                    .body(IssuersResponse.class);

            log.trace("Search Issuers: {}", body);
            return Objects.nonNull(body) ? body.issuers() : Collections.emptyList();
        } catch (ApiException e) {
            return Collections.emptyList();
        }
    }

    public List<Quote> getQuoteDetails(QuoteFilter filter) {
        Objects.requireNonNull(filter, "Filter cannot be null");
        try {
            final var tickers = String.join(",", filter.tickers());
            final var exchanges = filter.exchanges().stream()
                    .map(StockExchange::code)
                    .collect(Collectors.joining(","));
            final var attributes = "U,P,A,X,N,C,M,V,O,I";
            final var body = client.get()
                    .uri(builder -> {
                        builder.path("/cotizaciones")
                                .queryParam("emisora_serie", tickers)
                                .queryParam("bolsa", exchanges)
                                .queryParam("concepto", attributes);
                        return builder.build();
                    })
                    .retrieve()
                    .body(QuoteResponse.class);

            log.trace("Quotes: {}", body);
            return Objects.nonNull(body) ? body.quotes() : Collections.emptyList();
        } catch (ApiException e) {
            return Collections.emptyList();
        }
    }

    public List<HistoricalPriceDetail> fetchHistoricalData(HistoricalFilter filter) {
        Objects.requireNonNull(filter, "Filter cannot be null");
        try {
            final var body = client.get()
                    .uri(builder -> {
                        builder.path("/historicos")
                                .queryParam("emisora_serie", filter.ticker())
                                .queryParam("inicio", filter.from())
                                .queryParam("final", filter.to());
                        return builder.build();
                    })
                    .retrieve()
                    .body(HistoricalResponse.class);

            log.trace("Historical data: {}", body);
            return Optional.ofNullable(body)
                    .map(HistoricalResponse::prices)
                    .orElseThrow(() -> new IllegalStateException("No data found"));
        } catch (Exception e) {
            log.error("Error fetching historical data", e);
        }
        return Collections.emptyList();
    }
}
