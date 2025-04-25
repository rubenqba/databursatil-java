package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.error.IncomeStatementValidationException;
import com.github.rubenqba.databursatil.models.error.ReportError;
import com.github.rubenqba.databursatil.util.IncomeStatementDetailDeserializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = IncomeStatementDetailDeserializer.class)
public record IncomeStatementDetail(
        BigDecimal revenue,
        BigDecimal costOfGoodsSold,
        BigDecimal grossProfit,

        BigDecimal administrativeExpenses,
        BigDecimal distributionExpenses,
        BigDecimal otherOperatingExpenses,

        BigDecimal financeIncome,
        BigDecimal financeCosts,

        BigDecimal profitBeforeTax,
        BigDecimal incomeTaxExpense,
        BigDecimal netProfit,

        BigDecimal profitAttributableToParentOwners,
        BigDecimal profitAttributableToNoncontrollingInterests,

        BigDecimal basicEarningsPerShare,
        BigDecimal dilutedEarningsPerShare,

        BigDecimal profitFromContinuingOperations,
        BigDecimal profitFromDiscontinuedOperations,

        BigDecimal shareOfProfitFromAssociatesAndJointVentures
) {

    public void validate() {
        List<ReportError> errors = new ArrayList<>();

        // Rule 1: Revenue - CostOfGoodsSold = GrossProfit
        BigDecimal calculatedGrossProfit = revenue.subtract(costOfGoodsSold);
        if (grossProfit.compareTo(calculatedGrossProfit) != 0) {
            errors.add(new ReportError(
                    "Gross Profit must equal Revenue minus Cost of Goods Sold",
                    calculatedGrossProfit,
                    grossProfit
            ));
        }

        // Rule 2: Profit Before Tax calculation
        BigDecimal operatingExpenses = administrativeExpenses
                .add(distributionExpenses)
                .add(otherOperatingExpenses);

        BigDecimal calculatedProfitBeforeTax = grossProfit
                .subtract(operatingExpenses)
                .add(financeIncome)
                .subtract(financeCosts)
                .add(shareOfProfitFromAssociatesAndJointVentures);

        if (profitBeforeTax.compareTo(calculatedProfitBeforeTax) != 0) {
            errors.add(new ReportError(
                    "Profit Before Tax calculation mismatch",
                    calculatedProfitBeforeTax,
                    profitBeforeTax
            ));
        }

        // Rule 3: Net Profit = Profit Before Tax - Income Tax Expense
        BigDecimal calculatedNetProfit = profitBeforeTax.subtract(incomeTaxExpense);
        if (netProfit.compareTo(calculatedNetProfit) != 0) {
            errors.add(new ReportError(
                    "Net Profit must equal Profit Before Tax minus Income Tax Expense",
                    calculatedNetProfit,
                    netProfit
            ));
        }

        // Rule 4: Net Profit = Parent Owners + Non-controlling Interests
        BigDecimal profitAttributable = profitAttributableToParentOwners.add(profitAttributableToNoncontrollingInterests);
        if (netProfit.compareTo(profitAttributable) != 0) {
            errors.add(new ReportError(
                    "Net Profit must equal the sum of Parent Owners and Non-controlling Interests profits",
                    profitAttributable,
                    netProfit
            ));
        }

        // Rule 5: Net Profit = Continuing + Discontinued Operations
        BigDecimal profitOperations = profitFromContinuingOperations.add(profitFromDiscontinuedOperations);
        if (netProfit.compareTo(profitOperations) != 0) {
            errors.add(new ReportError(
                    "Net Profit must equal sum of profits from Continuing and Discontinued Operations",
                    profitOperations,
                    netProfit
            ));
        }

        // Lanzar excepción acumulativa si existen errores
        if (!errors.isEmpty()) {
            throw new IncomeStatementValidationException(errors);
        }
    }
}
