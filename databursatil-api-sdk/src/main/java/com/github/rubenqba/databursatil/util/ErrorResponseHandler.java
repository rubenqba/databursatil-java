package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.api.ErrorResponse;
import com.github.rubenqba.databursatil.models.error.ApiException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class ErrorResponseHandler implements ResponseErrorHandler {

    private final ObjectMapper mapper;

    public ErrorResponseHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        // get error message from response
        String message = "Unknown error: %s".formatted(response.getStatusText());
        try {
            if (response.getStatusCode().isError()) {
                final var error = mapper.readValue(response.getBody(), ErrorResponse.class);
                message = error.error();
            }
        } catch (IOException e) {
            message = "Unknown error: %s".formatted(e.getMessage());
        }
        throw new ApiException(response.getStatusCode(), message);
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }
}
