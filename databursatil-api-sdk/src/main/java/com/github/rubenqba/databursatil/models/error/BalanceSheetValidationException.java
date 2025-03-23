package com.github.rubenqba.databursatil.models.error;

import java.util.List;

public class BalanceSheetValidationException extends ReportValidationException {

    public BalanceSheetValidationException(List<ReportError> errors) {
        super("Balance sheet", errors);
    }
}