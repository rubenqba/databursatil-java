package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.filter.QuoteFilter;
import com.github.rubenqba.databursatil.util.ErrorResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Set;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class QuoteStockMarketServiceTest {

    private MockRestServiceServer server;
    private StockMarketService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .defaultStatusHandler(new ErrorResponseHandler(new ObjectMapper()));
        server = MockRestServiceServer.bindTo(builder).build();
        service = new StockMarketService(builder.build());
    }

    @Test
    void singleQuoteWithAllExchanges() {
        assertThat(service).isNotNull();

        final var filter = QuoteFilter.builder()
                .ticker("CEMEXCPO")
                .useAllExchanges()
                .build();

        server.expect(requestToUriTemplate("/cotizaciones?emisora_serie={tickers}&bolsa={exchange}&concepto={attrs}", "CEMEXCPO", "BMV,BIVA", "U,P,A,X,N,C,M,V,O,I"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/quote_single.json"), MediaType.APPLICATION_JSON));

        assertThat(service.getQuoteDetails(filter)).isNotNull()
                .satisfies(list -> {
                    assertThat(list).isNotEmpty();
                    assertThat(list).allSatisfy(quote -> {
                        assertThat(quote.ticker()).isEqualTo("CEMEXCPO");
                        assertThat(quote.exchange()).isNotNull();
                        assertThat(quote.details()).isNotNull();
                    });
                });
    }

    @Test
    void multipleQuoteWithAllExchanges() {
        assertThat(service).isNotNull();

        final var filter = QuoteFilter.builder()
                .tickers(Set.of("CEMEXCPO", "NVDA*", "WALMEX*"))
                .useAllExchanges()
                .build();

        server.expect(requestToUriTemplate("/cotizaciones?emisora_serie={tickers}&bolsa={exchange}&concepto={attrs}", String.join(",", filter.tickers()), "BMV,BIVA", "U,P,A,X,N,C,M,V,O,I"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/quotes_multiple.json"), MediaType.APPLICATION_JSON));

        assertThat(service.getQuoteDetails(filter)).isNotNull()
                .satisfies(list -> {
                    assertThat(list).hasSize(filter.tickers().size() * filter.exchanges().size());
                });
    }

}
