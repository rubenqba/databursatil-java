package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.QuoteDetails;
import com.github.rubenqba.databursatil.models.api.BenchmarkIndexResponse;
import com.github.rubenqba.databursatil.models.api.CommoditiesResponse;
import com.github.rubenqba.databursatil.models.api.HistoricalResponse;
import com.github.rubenqba.databursatil.models.api.IssuersResponse;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class DataMappingTest {
    final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void emisoras() {
        try (final var is = new FileInputStream("src/test/resources/data/emisoras_globales_am.json")) {
            final var data = mapper.readValue(is, IssuersResponse.class);
            assertThat(data.issuers()).hasSize(25);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void quotes() {

        try (final var is = new FileInputStream("src/test/resources/data/quote_detail_letters.json")) {
            final var quote = mapper.readValue(is, QuoteDetails.class);
            /**
             * {
             *   "U": 11.95,
             *   "P": 11.95,
             *   "A": 12.22,
             *   "X": 12.24,
             *   "N": 11.92,
             *   "C": -2.21,
             *   "M": -0.27,
             *   "V": 129142454.0,
             *   "O": 19576.0,
             *   "I": 1544373864.25,
             *   "F": "2025-03-21 14:01:00"
             * }
             */

            final var tz = ZoneId.of("America/Mexico_City").getRules().getOffset(Instant.now());


            final var date = OffsetDateTime.of(LocalDateTime.of(2025, 3, 21, 14, 1), tz);
            assertThat(quote.lastPrice()).isEqualTo(new BigDecimal("11.95"));
            assertThat(quote.volumeWeightedAveragePrice()).isEqualTo(new BigDecimal("11.95"));
            assertThat(quote.previousClose()).isEqualTo(new BigDecimal("12.22"));
            assertThat(quote.dayHigh()).isEqualTo(new BigDecimal("12.24"));
            assertThat(quote.dayLow()).isEqualTo(new BigDecimal("11.92"));
            assertThat(quote.percentageChange()).isEqualTo(new BigDecimal("-2.21"));
            assertThat(quote.currencyChange()).isEqualTo(new BigDecimal("-0.27"));
            assertThat(quote.volume()).isEqualTo(129142454L);
            assertThat(quote.trades()).isEqualTo(19576);
            assertThat(quote.totalValue()).isEqualTo(new BigDecimal("1544373864.25"));
            assertThat(quote.timestamp()).isEqualTo(date);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try (final var is = new FileInputStream("src/test/resources/data/quote_detail_reduced.json")) {
            final var quote = mapper.readValue(is, QuoteDetails.class);
            /**
             * {
             *   "ultimo": 3968.0,
             *   "ppp": 3963.3,
             *   "cambio%": 1.03,
             *   "cambio$": 40.28,
             *   "tiempo": "2025-03-21 14:02:00"
             * }
             */

            final var tz = ZoneId.of("America/Mexico_City").getRules().getOffset(Instant.now());


            final var date = OffsetDateTime.of(LocalDateTime.of(2025, 3, 21, 14, 2), tz);
            assertThat(quote.lastPrice()).isEqualTo(new BigDecimal("3968.0"));
            assertThat(quote.volumeWeightedAveragePrice()).isEqualTo(new BigDecimal("3963.3"));
            assertThat(quote.percentageChange()).isEqualTo(new BigDecimal("1.03"));
            assertThat(quote.currencyChange()).isEqualTo(new BigDecimal("40.28"));
            assertThat(quote.timestamp()).isEqualTo(date);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void historicalPrices() {
        try (final var is = new FileInputStream("src/test/resources/data/historical_prices.json")) {
            final var data = mapper.readValue(is, HistoricalResponse.class);
            assertThat(data.prices())
                    .hasSizeGreaterThan(1)
                    .allSatisfy(price -> {
                        assertThat(price.date()).isNotNull();
                        assertThat(price.closePrice()).isGreaterThan(BigDecimal.ZERO);
                        assertThat(price.totalValue()).isGreaterThan(BigDecimal.ZERO);
                    });

            // verify that the prices are sorted by date
            var previous = data.prices().getFirst().date();
            var latest = data.prices().getLast().date();
            assertThat(previous).isBeforeOrEqualTo(latest);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void benchmarkIndexes() {
        try (final var is = new FileInputStream("src/test/resources/data/benchmark_indexes.json")) {
            final var data = mapper.readValue(is, BenchmarkIndexResponse.class);
            assertThat(data.indexes())
                    .hasSizeGreaterThan(1)
                    .allSatisfy(index -> {
                        assertThat(index.code()).isNotBlank();
                        assertThat(index.name()).isNotBlank();
                        assertThat(index.lastPrice()).isGreaterThan(BigDecimal.ZERO);
                        assertThat(index.openingPrice()).isGreaterThan(BigDecimal.ZERO);
                        assertThat(index.highPrice()).isGreaterThan(BigDecimal.ZERO);
                        assertThat(index.lowPrice()).isGreaterThan(BigDecimal.ZERO);
                        assertThat(index.percentChange()).isNotNull();
                        assertThat(index.absoluteChange()).isNotNull();
                        assertThat(index.volume()).isGreaterThan(0);
                        assertThat(index.percentChangeYTD()).isNotNull();
                        assertThat(index.timestamp()).isNotNull();
                    });
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void commodities() {
        try (final var is = new FileInputStream("src/test/resources/data/commodities.json")) {
            final var data = mapper.readValue(is, CommoditiesResponse.class);
            assertThat(data).isNotNull();
            assertThat(data.groups())
                    .describedAs("No groups loaded")
                    .hasSize(7)
                    .allSatisfy(group -> {
                        assertThat(group.name()).isNotBlank();
                        assertThat(group.commodities())
                                .describedAs("No commodities in group")
                                .isNotEmpty()
                                .allSatisfy(commodity -> {
                                    assertThat(commodity.code()).isNotBlank();
                                    assertThat(commodity.unit()).isNotBlank();
                                    assertThat(commodity.lastPrice()).isNotNull();
                                    assertThat(commodity.absoluteChange()).isNotNull();
                                    assertThat(commodity.percentChange()).isNotNull();
                                    assertThat(commodity.weeklyChangePercent()).isNotNull();
                                    assertThat(commodity.monthlyChangePercent()).isNotNull();
                                    assertThat(commodity.annualChangePercent()).isNotNull();
                                    assertThat(commodity.percentChangeYTD()).isNotNull();
                                    assertThat(commodity.date()).isNotNull();
                                });
                    });
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
