package com.github.rubenqba.databursatil.models;

public enum FinancialStatementType {
    BALANCE_SHEET("posicion", "Balance Sheet (Statement of Financial Position)"),

    CASH_FLOW_STATEMENT("flujos", "Cash Flow Statement"),

    INCOME_STATEMENT_QUARTERLY("resultado_trimestre", "Income Statement (Quarterly Results)"),

    INCOME_STATEMENT_YTD("resultado_acumulado", "Income Statement (YTD Results)");

    private final String code;
    private final String description;

    FinancialStatementType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // Método para buscar el enum por el valor del API
    public static FinancialStatementType fromCode(String code) {
        for (FinancialStatementType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown financial statement type: " + code);
    }

    // Getters
    public String code() { return code; }
    public String description() { return description; }
}