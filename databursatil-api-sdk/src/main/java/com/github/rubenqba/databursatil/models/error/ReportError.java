package com.github.rubenqba.databursatil.models.error;

public record ReportError(String description, Number expectedValue, Number actualValue) {
    public ReportError(String description, Number expectedValue) {
        this(description, expectedValue, null);
    }
}
