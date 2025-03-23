package com.github.rubenqba.databursatil.models.error;

import java.util.List;

public class CashFlowStatementValidationException extends ReportValidationException {
    public CashFlowStatementValidationException(List<ReportError> errors) {
        super("Cash flow statement", errors);
    }
}
