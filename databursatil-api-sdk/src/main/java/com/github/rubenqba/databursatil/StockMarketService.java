package com.github.rubenqba.databursatil;

import com.github.rubenqba.databursatil.models.Issuer;
import com.github.rubenqba.databursatil.models.IssuersResponse;
import com.github.rubenqba.databursatil.models.error.ApiException;
import com.github.rubenqba.databursatil.models.filter.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
}
