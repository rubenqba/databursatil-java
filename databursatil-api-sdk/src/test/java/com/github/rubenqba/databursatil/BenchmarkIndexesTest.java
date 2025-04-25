package com.github.rubenqba.databursatil;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class BenchmarkIndexesTest extends BaseStockMarketServiceTest {

    @Test
    public void getBenchmarkIndexes() {
        server.expect(requestTo("/indices"))
                .andRespond(withSuccess(loadJsonFile("data/benchmark_indexes.json"), MediaType.APPLICATION_JSON));

        assertThat(service.fetchBenchmarkIndexes()).isNotEmpty();
    }
}
