package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.CashFlowStatementDetail;

import java.io.IOException;
import java.math.BigDecimal;

public class CashFlowStatementDetailDeserializer extends JsonDeserializer<CashFlowStatementDetail> {

    @Override
    public CashFlowStatementDetail deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        return new CashFlowStatementDetail(
                extract(node, "ProfitLoss"),
                extract(node, "CashAndCashEquivalents"),
                extract(node, "CashAndCashEquivalents_Ending"),
                extract(node, "IncreaseDecreaseInCashAndCashEquivalents"),
                extract(node, "IncreaseDecreaseInCashAndCashEquivalentsBeforeEffectOfExchangeRateChanges"),
                extract(node, "EffectOfExchangeRateChangesOnCashAndCashEquivalents"),

                // Operating Activities
                extract(node, "CashFlowsFromUsedInOperatingActivities"),
                extract(node, "AdjustmentsForReconcileProfitLoss"),
                extract(node, "AdjustmentsForDepreciationAndAmortisationExpense"),
                extract(node, "AdjustmentsForFinanceCosts"),
                extract(node, "AdjustmentsForIncomeTaxExpense"),
                extract(node, "AdjustmentsForSharebasedPayments"),
                extract(node, "AdjustmentsForFairValueGainsLosses"),
                extract(node, "AdjustmentsForUnrealisedForeignExchangeLossesGains"),
                extract(node, "AdjustmentsForLossesGainsOnDisposalOfNoncurrentAssets"),
                extract(node, "AdjustmentsForImpairmentLossReversalOfImpairmentLossRecognisedInProfitOrLoss"),
                extract(node, "AdjustmentsForProvisions"),
                extract(node, "AdjustmentsForDecreaseIncreaseInInventories"),
                extract(node, "AdjustmentsForDecreaseIncreaseInTradeAccountReceivable"),
                extract(node, "AdjustmentsForIncreaseDecreaseInTradeAccountPayable"),
                extract(node, "AdjustmentsForDecreaseIncreaseInOtherOperatingReceivables"),
                extract(node, "AdjustmentsForIncreaseDecreaseInOtherOperatingPayables"),
                extract(node, "OtherInflowsOutflowsOfCashClassifiedAsOperatingActivities"),
                extract(node, "DividendsReceivedClassifiedAsOperatingActivities"),
                extract(node, "InterestPaidClassifiedAsOperatingActivities"),
                extract(node, "InterestReceivedClassifiedAsOperatingActivities"),
                extract(node, "IncomeTaxesPaidRefundClassifiedAsOperatingActivities"),

                // Investing Activities
                extract(node, "CashFlowsFromUsedInInvestingActivities"),
                extract(node, "PurchaseOfPropertyPlantAndEquipmentClassifiedAsInvestingActivities"),
                extract(node, "PurchaseOfIntangibleAssetsClassifiedAsInvestingActivities"),
                extract(node, "PurchaseOfOtherLongtermAssetsClassifiedAsInvestingActivities"),
                extract(node, "ProceedsFromOtherLongtermAssetsClassifiedAsInvestingActivities"),
                extract(node, "ProceedsFromSalesOfPropertyPlantAndEquipmentClassifiedAsInvestingActivities"),
                extract(node, "ProceedsFromSalesOfIntangibleAssetsClassifiedAsInvestingActivities"),
                extract(node, "InterestPaidClassifiedAsInvestingActivities"),
                extract(node, "InterestReceivedClassifiedAsInvestingActivities"),
                extract(node, "DividendsReceivedClassifiedAsInvestingActivities"),
                extract(node, "CashFlowsFromLosingControlOfSubsidiariesOrOtherBusinessesClassifiedAsInvestingActivities"),
                extract(node, "CashFlowsUsedInObtainingControlOfSubsidiariesOrOtherBusinessesClassifiedAsInvestingActivities"),
                extract(node, "OtherInflowsOutflowsOfCashClassifiedAsInvestingActivities"),

                // Financing Activities
                extract(node, "CashFlowsFromUsedInFinancingActivities"),
                extract(node, "ProceedsFromIssuingShares"),
                extract(node, "ProceedsFromBorrowingsClassifiedAsFinancingActivities"),
                extract(node, "RepaymentsOfBorrowingsClassifiedAsFinancingActivities"),
                extract(node, "DividendsPaidClassifiedAsFinancingActivities"),
                extract(node, "PaymentsToAcquireOrRedeemEntitysShares"),
                extract(node, "InterestPaidClassifiedAsFinancingActivities"),
                extract(node, "PaymentsOfLeaseLiabilitiesClassifiedAsFinancingActivities"),
                extract(node, "PaymentsOfFinanceLeaseLiabilitiesClassifiedAsFinancingActivities"),
                extract(node, "ProceedsFromGovernmentGrantsClassifiedAsFinancingActivities"),
                extract(node, "ProceedsFromChangesInOwnershipInterestsInSubsidiaries"),
                extract(node, "PaymentsFromChangesInOwnershipInterestsInSubsidiaries"),
                extract(node, "OtherInflowsOutflowsOfCashClassifiedAsFinancingActivities")
        );
    }

    private BigDecimal extract(JsonNode node, String field) {
        if (node.has(field) && node.get(field).isArray() && node.get(field).size() > 1) {
            return node.get(field).get(1).decimalValue();
        }
        return BigDecimal.ZERO;
    }
}