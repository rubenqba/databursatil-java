package com.github.rubenqba.databursatil.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.BalanceSheetStatement;
import com.github.rubenqba.databursatil.models.CashFlowStatement;
import com.github.rubenqba.databursatil.models.IncomeStatement;
import com.github.rubenqba.databursatil.util.CustomFinancialStatementResponseDeserializer;

@JsonDeserialize(using = CustomFinancialStatementResponseDeserializer.class)
public record FinancialStatementResponse(
        ReportReference<BalanceSheetStatement> balanceSheet,
        ReportReference<IncomeStatement> incomeQuarterly,
        ReportReference<IncomeStatement> incomeYearToDate,
        ReportReference<CashFlowStatement> cashFlow
) {


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ReportReference<BalanceSheetStatement> balanceSheet;
        private ReportReference<IncomeStatement> incomeQuarterly;
        private ReportReference<IncomeStatement> incomeYearToDate;
        private ReportReference<CashFlowStatement> cashFlow;

        public Builder() {
        }

        public Builder balanceSheet(ReportReference<BalanceSheetStatement> val) {
            balanceSheet = val;
            return this;
        }

        public Builder incomeQuarterly(ReportReference<IncomeStatement> val) {
            incomeQuarterly = val;
            return this;
        }

        public Builder incomeYearToDate(ReportReference<IncomeStatement> val) {
            incomeYearToDate = val;
            return this;
        }

        public Builder cashFlow(ReportReference<CashFlowStatement> val) {
            cashFlow = val;
            return this;
        }

        public FinancialStatementResponse build() {
            return new FinancialStatementResponse(this.balanceSheet, this.incomeQuarterly, this.incomeYearToDate, this.cashFlow);
        }
    }
}
