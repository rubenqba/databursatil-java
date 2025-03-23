package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.util.ErrorResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

public class BaseStockMarketServiceTest {

    protected MockRestServiceServer server;
    protected StockMarketService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .defaultStatusHandler(new ErrorResponseHandler(new ObjectMapper()));
        server = MockRestServiceServer.bindTo(builder).build();
        service = new StockMarketService(builder.build());
    }
}
