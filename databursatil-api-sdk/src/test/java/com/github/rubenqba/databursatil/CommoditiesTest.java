package com.github.rubenqba.databursatil;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class CommoditiesTest extends BaseStockMarketServiceTest {

    @Test
    void fetchCommodities() {
        server.expect(requestTo("/commodities"))
                .andRespond(withSuccess(loadJsonFile("data/commodities.json"), MediaType.APPLICATION_JSON));

        assertThat(service.fetchCommodities()).isNotEmpty();
    }
}
