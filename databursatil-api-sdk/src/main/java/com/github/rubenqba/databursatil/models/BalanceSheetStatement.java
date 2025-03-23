package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.error.BalanceSheetValidationException;
import com.github.rubenqba.databursatil.models.error.ReportError;
import com.github.rubenqba.databursatil.util.BalanceSheetStatementDeserializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = BalanceSheetStatementDeserializer.class)
public record BalanceSheetStatement(
        // Activos (Assets)
        BigDecimal totalAssets,
        BigDecimal currentAssets,
        BigDecimal nonCurrentAssets,
        BigDecimal cashAndCashEquivalents,
        BigDecimal tradeAndOtherReceivables,
        BigDecimal inventories,
        BigDecimal propertyPlantAndEquipment,
        BigDecimal investmentProperty,
        BigDecimal goodwill,
        BigDecimal intangibleAssets,
        BigDecimal deferredTaxAssets,
        BigDecimal rightOfUseAssets,

        // Pasivos (Liabilities)
        BigDecimal totalLiabilities,
        BigDecimal currentLiabilities,
        BigDecimal tradeAndOtherPayable,
        BigDecimal nonCurrentLiabilities,
        BigDecimal deferredTaxLiabilities,
        BigDecimal currentLeaseLiabilities,
        BigDecimal nonCurrentLeaseLiabilities,

        // Capital contable (Equity)
        BigDecimal totalEquity,
        BigDecimal issuedCapital,
        BigDecimal sharePremium,
        BigDecimal retainedEarnings,
        BigDecimal treasuryShares,
        BigDecimal otherReserves,
        BigDecimal nonControllingInterests,

        // Otros (Others)
        BigDecimal equityAndLiabilitiesTotal
) {

    public void validate() {
        List<ReportError> errors = new ArrayList<>();

        // Rule 1: Total Assets = Total Liabilities + Total Equity
        BigDecimal liabilitiesAndEquity = totalLiabilities.add(totalEquity);
        if (totalAssets.compareTo(liabilitiesAndEquity) != 0) {
            errors.add(new ReportError(
                    "Total Assets must equal Total Liabilities plus Total Equity",
                    liabilitiesAndEquity,
                    totalAssets
            ));
        }

        // Rule 2: Total Assets = Current Assets + Non-current Assets
        BigDecimal computedTotalAssets = currentAssets.add(nonCurrentAssets);
        if (totalAssets.compareTo(computedTotalAssets) != 0) {
            errors.add(new ReportError(
                    "Total Assets must equal Current Assets plus Non-current Assets",
                    computedTotalAssets,
                    totalAssets
            ));
        }

        // Rule 3: Total Liabilities = Current Liabilities + Non-current Liabilities
        BigDecimal computedTotalLiabilities = currentLiabilities.add(nonCurrentLiabilities);
        if (totalLiabilities.compareTo(computedTotalLiabilities) != 0) {
            errors.add(new ReportError(
                    "Total Liabilities must equal Current Liabilities plus Non-current Liabilities",
                    computedTotalLiabilities,
                    totalLiabilities
            ));
        }

        // Rule 4: Total Equity = Issued Capital + Share Premium + Retained Earnings - Treasury Shares + Other Reserves + Non-controlling Interests
        BigDecimal computedEquity = issuedCapital
                .add(sharePremium)
                .add(retainedEarnings)
                .subtract(treasuryShares)
                .add(otherReserves)
                .add(nonControllingInterests);

        if (totalEquity.compareTo(computedEquity) != 0) {
            errors.add(new ReportError(
                    "Total Equity must equal the sum of its components (Issued Capital + Share Premium + Retained Earnings - Treasury Shares + Other Reserves + Non-controlling Interests)",
                    computedEquity,
                    totalEquity
            ));
        }

        // Rule 5: Equity and Liabilities Total = Total Liabilities + Total Equity
        BigDecimal computedEquityLiabilities = totalLiabilities.add(totalEquity);
        if (equityAndLiabilitiesTotal.compareTo(computedEquityLiabilities) != 0) {
            errors.add(new ReportError(
                    "Equity and Liabilities Total must equal Total Liabilities plus Total Equity",
                    computedEquityLiabilities,
                    equityAndLiabilitiesTotal
            ));
        }

        // Si hay errores, lanza una excepción única
        if (!errors.isEmpty()) {
            throw new BalanceSheetValidationException(errors);
        }
    }

}
