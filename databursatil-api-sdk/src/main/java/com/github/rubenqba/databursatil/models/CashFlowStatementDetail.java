package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.error.CashFlowStatementValidationException;
import com.github.rubenqba.databursatil.models.error.ReportError;
import com.github.rubenqba.databursatil.util.CashFlowStatementDetailDeserializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = CashFlowStatementDetailDeserializer.class)
public record CashFlowStatementDetail(

        // General Information
        BigDecimal profitLoss,
        BigDecimal cashAndCashEquivalentsAtBeginning,
        BigDecimal cashAndCashEquivalentsAtEnd,
        BigDecimal increaseDecreaseInCashAndCashEquivalents,
        BigDecimal increaseDecreaseInCashAndCashEquivalentsBeforeExchangeRateEffects,
        BigDecimal effectOfExchangeRateChangesOnCash,

        // Operating Activities
        BigDecimal netCashFromOperatingActivities,
        BigDecimal adjustmentsToReconcileProfitLoss,
        BigDecimal depreciationAndAmortisationExpense,
        BigDecimal financeCosts,
        BigDecimal incomeTaxExpense,
        BigDecimal shareBasedPayments,
        BigDecimal fairValueGainsLosses,
        BigDecimal unrealisedForeignExchangeLossesGains,
        BigDecimal gainsLossesOnDisposalOfNonCurrentAssets,
        BigDecimal adjustmentForImpairmentLoss,
        BigDecimal adjustmentsForProvisions,
        BigDecimal adjustmentsForDecreaseIncreaseInInventories,
        BigDecimal decreaseIncreaseInTradeAccountReceivable,
        BigDecimal increaseDecreaseInTradeAccountPayable,
        BigDecimal decreaseIncreaseInOtherOperatingReceivables,
        BigDecimal increaseDecreaseInOtherOperatingPayables,
        BigDecimal otherOperatingActivities,
        BigDecimal dividendsReceivedOperating,
        BigDecimal interestPaidOperating,
        BigDecimal interestReceivedOperating,
        BigDecimal incomeTaxesPaidOperating,

        // Investing Activities
        BigDecimal netCashFromInvestingActivities,
        BigDecimal purchaseOfPropertyPlantAndEquipment,
        BigDecimal purchaseOfIntangibleAssets,
        BigDecimal purchaseOfOtherLongTermAssets,
        BigDecimal proceedsFromSalesOfOtherLongTermAssets,
        BigDecimal proceedsFromSalesOfPropertyPlantAndEquipment,
        BigDecimal proceedsFromSalesOfIntangibleAssets,
        BigDecimal interestPaidInvesting,
        BigDecimal interestReceivedInvesting,
        BigDecimal dividendsReceivedInvesting,
        BigDecimal cashFlowsFromLosingControlOfSubsidiaries,
        BigDecimal cashFlowsUsedInObtainingControlOfSubsidiaries,
        BigDecimal otherInvestingActivities,

        // Financing Activities
        BigDecimal netCashFromFinancingActivities,
        BigDecimal proceedsFromIssuingShares,
        BigDecimal proceedsFromBorrowings,
        BigDecimal repaymentsOfBorrowings,
        BigDecimal dividendsPaid,
        BigDecimal paymentsToAcquireOrRedeemEntityShares,
        BigDecimal interestPaidFinancing,
        BigDecimal paymentsOfLeaseLiabilities,
        BigDecimal paymentsOfFinanceLeaseLiabilities,
        BigDecimal proceedsFromGovernmentGrants,
        BigDecimal proceedsFromChangesInOwnershipInterestsInSubsidiaries,
        BigDecimal paymentsFromChangesInOwnershipInterestsInSubsidiaries,
        BigDecimal otherFinancingActivities
) {

    public void validate() {
        List<ReportError> errors = new ArrayList<>();

        // Rule 1: Cash reconciliation
        BigDecimal totalNetCashFlow = netCashFromOperatingActivities
                .add(netCashFromInvestingActivities)
                .add(netCashFromFinancingActivities);

        BigDecimal expectedEndingCash = cashAndCashEquivalentsAtBeginning
                .add(totalNetCashFlow)
                .add(effectOfExchangeRateChangesOnCash);

        if (!expectedEndingCash.setScale(2, RoundingMode.HALF_UP)
                .equals(cashAndCashEquivalentsAtEnd.setScale(2, RoundingMode.HALF_UP))) {
            errors.add(new ReportError("Cash reconciliation failed.", expectedEndingCash, cashAndCashEquivalentsAtEnd));
        }

        // Rule 2: Validate increase/decrease before exchange rate effect
        if (!totalNetCashFlow.setScale(2, RoundingMode.HALF_UP)
                .equals(increaseDecreaseInCashAndCashEquivalentsBeforeExchangeRateEffects.setScale(2, RoundingMode.HALF_UP))) {
            errors.add(new ReportError("Increase/decrease before exchange rate effects mismatch.", totalNetCashFlow, increaseDecreaseInCashAndCashEquivalentsBeforeExchangeRateEffects));
        }

        // Rule 3: Validate Operating Cash Flow
        BigDecimal operatingCalculated = profitLoss.add(adjustmentsToReconcileProfitLoss).subtract(incomeTaxesPaidOperating);
        if (!operatingCalculated.setScale(2, RoundingMode.HALF_UP)
                .equals(netCashFromOperatingActivities.setScale(2, RoundingMode.HALF_UP))) {
            errors.add(new ReportError("Operating cash flow reconciliation failed.", operatingCalculated, netCashFromOperatingActivities));
        }

        if (!errors.isEmpty()) {
            throw new CashFlowStatementValidationException(errors);
        }
    }
}