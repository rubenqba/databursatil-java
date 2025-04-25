package com.github.rubenqba.databursatil;

import com.github.rubenqba.databursatil.models.*;
import com.github.rubenqba.databursatil.models.api.*;
import com.github.rubenqba.databursatil.models.error.ApiException;
import com.github.rubenqba.databursatil.models.filter.FinancialReportFilter;
import com.github.rubenqba.databursatil.models.filter.HistoricalFilter;
import com.github.rubenqba.databursatil.models.filter.QuoteFilter;
import com.github.rubenqba.databursatil.models.filter.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public FinancialReports fetchFinancialReports(FinancialReportFilter filter) {
        Objects.requireNonNull(filter, "Filter cannot be null");
        try {
            final var body = client.get()
                    .uri(builder -> {
                        builder.path("/financieros")
                                .queryParam("emisora", filter.issuerCode())
                                .queryParam("periodo", filter.period())
                                .queryParam("financieros", filter.include().stream()
                                        .map(FinancialStatementType::code)
                                        .map(String::toLowerCase)
                                        .collect(Collectors.joining(",")));
                        return builder.build();
                    })
                    .retrieve()
                    .body(FinancialStatementResponse.class);

            log.trace("Financial reports: {}", body);
            return Optional.ofNullable(body)
                    .map(reports -> {
                        final var currentPeriod = filter.period();
                        final var previousPeriod = computePreviousPeriod(currentPeriod);
                        final var previous = new ReportStatements(
                                previousPeriod,
                                reports.balanceSheet().previous(),
                                reports.incomeQuarterly().previous(),
                                reports.incomeYearToDate().previous(),
                                reports.cashFlow().previous()
                        );
                        final var current = new ReportStatements(
                                currentPeriod,
                                reports.balanceSheet().current(),
                                reports.incomeQuarterly().current(),
                                reports.incomeYearToDate().current(),
                                reports.cashFlow().current()
                        );
                        return new FinancialReports(previous, current);
                    })
                    .orElseThrow(() -> new IllegalStateException("No data found"));
        } catch (Exception e) {
            log.error("Error fetching historical data", e);
            throw e;
        }
    }

    private static String computePreviousPeriod(String periodoActual) {
        // Valida y extrae el trimestre y el año del período actual
        Pattern pattern = Pattern.compile("^([1-4])T_(2\\d{3})$");
        Matcher matcher = pattern.matcher(periodoActual);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de período inválido: " + periodoActual);
        }

        int trimestre = Integer.parseInt(matcher.group(1));
        int year = Integer.parseInt(matcher.group(2));

        // Calcula el año anterior
        int prevYear = year - 1;

        // Formatea el nuevo período
        return String.format("%dT_%04d", trimestre, prevYear);
    }

    public List<BenchmarkIndex> fetchBenchmarkIndexes() {
        try {
            final var body = client.get()
                    .uri("/indices")
                    .retrieve()
                    .body(BenchmarkIndexResponse.class);

            log.trace("Benchmark indexes: {}", body);
            return Optional.ofNullable(body)
                    .map(BenchmarkIndexResponse::indexes)
                    .orElseThrow(() -> new IllegalStateException("No data found"));
        } catch (ApiException e) {
            log.error("Error fetching indexes", e);
        }
        return Collections.emptyList();
    }

    public List<CommodityGroup> fetchCommodities() {
        try {
            final var body = client.get()
                    .uri("/commodities")
                    .retrieve()
                    .body(CommoditiesResponse.class);

            log.trace("Commodities: {}", body);
            return Optional.ofNullable(body)
                    .map(CommoditiesResponse::groups)
                    .orElseThrow(() -> new IllegalStateException("No data found"));
        } catch (ApiException e) {
            log.error("Error fetching commodities", e);
        }
        return Collections.emptyList();
    }
}
