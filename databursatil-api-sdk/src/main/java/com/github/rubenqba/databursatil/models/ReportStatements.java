package com.github.rubenqba.databursatil.models;

public record ReportStatements(
        String period,
        BalanceSheetStatement balanceSheet,
        IncomeStatement incomeQuarterly,
        IncomeStatement incomeYearToDate,
        CashFlowStatement cashFlow
) {
    public ReportStatements {
        // ensure thar at least one statement is present
        if (balanceSheet == null && incomeQuarterly == null && incomeYearToDate == null && cashFlow == null) {
            throw new IllegalArgumentException("At least one statement must be present");
        }
    }
}
