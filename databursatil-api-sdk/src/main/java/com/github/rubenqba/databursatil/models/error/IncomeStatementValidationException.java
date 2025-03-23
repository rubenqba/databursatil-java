package com.github.rubenqba.databursatil.models.error;

import java.util.List;

public class IncomeStatementValidationException extends ReportValidationException {
    public IncomeStatementValidationException(List<ReportError> errors) {
        super("Income statement", errors);
    }
}

