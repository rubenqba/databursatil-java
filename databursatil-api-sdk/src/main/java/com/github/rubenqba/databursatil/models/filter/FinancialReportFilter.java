package com.github.rubenqba.databursatil.models.filter;

import com.github.rubenqba.databursatil.models.FinancialStatementType;

import java.util.EnumSet;

public record FinancialReportFilter(String issuerCode, EnumSet<FinancialStatementType> include, String period) {
    public FinancialReportFilter {
        if (issuerCode == null || issuerCode.isBlank()) {
            throw new IllegalArgumentException("Issuer code is required");
        }
        if (include == null || include.isEmpty()) {
            include = EnumSet.of(FinancialStatementType.INCOME_STATEMENT_QUARTERLY);
        }
        if (period == null || period.isBlank()) {
            throw new IllegalArgumentException("Period is required");
        }
        if (!period.matches("^[1-4]T_2\\d{3}$")) {
            throw new IllegalArgumentException("Period format is invalid. It should be like 1T_2021");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String issuerCode;
        private EnumSet<FinancialStatementType> include = EnumSet.noneOf(FinancialStatementType.class);
        private String period;

        public Builder() {
        }

        public Builder issuerCode(String val) {
            issuerCode = val;
            return this;
        }

        public Builder includeBalanceSheet() {
            include.add(FinancialStatementType.BALANCE_SHEET);
            return this;
        }

        public Builder includeCashFlowStatement() {
            include.add(FinancialStatementType.CASH_FLOW_STATEMENT);
            return this;
        }

        public Builder includeIncomeStatementQuarterly() {
            include.add(FinancialStatementType.INCOME_STATEMENT_QUARTERLY);
            return this;
        }

        public Builder includeIncomeStatementYTD() {
            include.add(FinancialStatementType.INCOME_STATEMENT_YTD);
            return this;
        }

        public Builder includeAllStatements() {
            include = EnumSet.allOf(FinancialStatementType.class);
            return this;
        }

        public Builder period(String val) {
            period = val;
            return this;
        }

        public FinancialReportFilter build() {
            return new FinancialReportFilter(this.issuerCode, this.include, this.period);
        }
    }
}
