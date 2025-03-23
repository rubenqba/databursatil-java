package com.github.rubenqba.databursatil.models.error;

import java.util.List;

public class ReportValidationException extends ValidationException {
    public ReportValidationException(String source, List<ReportError> errors) {
        super(source, errors.stream()
                .map(e -> "%s: Expected %s, current %s".formatted(e.description(), e.expectedValue(), e.actualValue()))
                .toList());
    }
}
