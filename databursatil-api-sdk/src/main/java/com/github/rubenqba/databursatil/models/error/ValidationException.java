package com.github.rubenqba.databursatil.models.error;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final String source;
    private final List<String> errors;

    public ValidationException(String source, List<String> errors) {
        super("%s validation failed%s.".formatted(source, errors.size() > 1 ? " with multiple errors" : ""));
        this.source = source;
        this.errors = errors;
    }

    public String getSource() {
        return source;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        return String.join("; ", errors);
    }
}
