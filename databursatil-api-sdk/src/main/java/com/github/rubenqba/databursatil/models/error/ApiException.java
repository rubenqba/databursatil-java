package com.github.rubenqba.databursatil.models.error;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatusCode status;
    public ApiException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }
}
