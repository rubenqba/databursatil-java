package com.github.rubenqba.databursatil.autoconfigure;

import com.github.rubenqba.databursatil.StockMarketService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
@ConditionalOnClass(StockMarketService.class)
@EnableConfigurationProperties(DataBursatilProperties.class)
@ConditionalOnProperty(prefix = "databursatil", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataBursatilServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StockMarketService stockMarketService(DataBursatilProperties properties) {
        // Validate apiToken
        if (properties.getApiToken() == null || properties.getApiToken().isBlank()) {
            throw new IllegalArgumentException("Property 'databursatil.api-token' must be set");
        }
        RestClient client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestInterceptors(list -> list.add(addTokenFilter(properties.getApiToken())))
                .build();
        return new StockMarketService(client);
    }

    private ClientHttpRequestInterceptor addTokenFilter(String token) {
        return (request, body, execution) -> {
            // Modificar la URI para agregar el token
            URI modifiedUri = UriComponentsBuilder.fromUri(request.getURI())
                    .queryParam("token", token)
                    .build()
                    .toUri();

            // Crear una nueva solicitud con la URI modificada
            final var modifiedRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return modifiedUri;
                }
            };

            return execution.execute(modifiedRequest, body);
        };
    }
}