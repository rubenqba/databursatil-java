package com.github.rubenqba.databursatil;

import com.github.rubenqba.databursatil.models.filter.HistoricalFilter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class HistoricalPriceTest extends BaseStockMarketServiceTest{

    @Test
    public void testGetHistoricalPrices() {
        // Given
        HistoricalFilter filter = HistoricalFilter.builder()
                .ticker("AAPL")
                .from(LocalDate.of(2021, 1, 1))
                .to(LocalDate.of(2021, 1, 31))
                .build();

        server.expect(requestTo("/historicos?emisora_serie=AAPL&inicio=2021-01-01&final=2021-01-31"))
                .andRespond(withSuccess(loadJsonFile("data/historical_prices.json"), MediaType.APPLICATION_JSON));

        // When
        assertThat(service.fetchHistoricalData(filter)).isNotEmpty();
    }
}
