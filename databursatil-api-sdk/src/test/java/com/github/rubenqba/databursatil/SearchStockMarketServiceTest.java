package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.filter.SearchFilter;
import com.github.rubenqba.databursatil.util.ErrorResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SearchStockMarketServiceTest {

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
    void searchInGlobalMarket() {
        assertThat(service).isNotNull();

        final var filter = SearchFilter.builder()
                .query("AM")
                .onlyGlobalMarket()
                .build();
        server.expect(requestToUriTemplate("/emisoras?letra={query}&mercado={scopes}", "AM", "global"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/emisoras_globales_am.json"), MediaType.APPLICATION_JSON));

        assertThat(service.searchIssuers(filter)).isNotNull()
                .satisfies(list -> {
                    assertThat(list).isNotEmpty();
                    assertThat(list).anySatisfy(issuer -> {
                        assertThat(issuer.code()).isEqualTo("AMZN");
                        assertThat(issuer.series()).hasSize(1);
                        assertThat(issuer.series().getFirst().series()).isEqualTo("*");
                    });
                    assertThat(list).allSatisfy(issuer ->
                            assertThat(issuer.series()
                                    .getFirst()
                                    .securityTypeDescription())
                                    .isNotBlank()
                                    .isIn("ACCIONES DEL SISTEMA INTERNACIONAL DE COTIZACIONES", "CANASTA DE ACCIONES (TRAC'S EXTRANJEROS)", "SIC Acciones"));
                });
    }

    @Test
    void searchInLocalMarket() {
        assertThat(service).isNotNull();

        server.expect(requestToUriTemplate("/emisoras?letra={query}", "AMX"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/emisoras_amx.json"), MediaType.APPLICATION_JSON));

        final var filter = SearchFilter.builder()
                .query("AMX")
                .build();

        assertThat(service.searchIssuers(filter))
                .isNotNull()
                .satisfies(list -> {
                    assertThat(list).hasSize(1);
                    final var issuer = list.getFirst();
                    assertThat(issuer.code()).isEqualTo("AMX");
                    assertThat(issuer.series())
                            .hasSize(4)
                            .satisfiesOnlyOnce(series -> assertThat(series.status()).isEqualTo("ACTIVA"));
                });
    }

    @Test
    void searchAllLocalMarket() {
        server.expect(requestTo("/emisoras"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/emisoras_locales_all.json"), MediaType.APPLICATION_JSON));

        assertThat(service.searchIssuers(null)).isNotEmpty();
    }

    @Test
    void searchAllMarkets() {

        server.expect(requestToUriTemplate("/emisoras?mercado={scopes}", "local,global"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(loadJsonFile("data/emisoras_locales_all.json"), MediaType.APPLICATION_JSON));

        assertThat(service.searchIssuers(SearchFilter.builder().allMarkets().build())).isNotEmpty();
    }

    @Test
    void searchNotFound() {
        server.expect(requestToUriTemplate("/emisoras?letra={query}", "ZZZ"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest()
                        .body(loadJsonFile("data/not_found.json"))
                        .contentType(MediaType.APPLICATION_JSON));

        assertThat(service.searchIssuers(SearchFilter.builder().query("ZZZ").build())).isEmpty();
    }
}