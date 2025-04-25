package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.BalanceSheetStatementDetail;

import java.io.IOException;
import java.math.BigDecimal;

public class BalanceSheetStatementDetailDeserializer extends JsonDeserializer<BalanceSheetStatementDetail> {

    @Override
    public BalanceSheetStatementDetail deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode root = p.getCodec().readTree(p);

        return new BalanceSheetStatementDetail(
            getBigDecimal(root, "Assets"),
            getBigDecimal(root, "CurrentAssets"),
            getBigDecimal(root, "NoncurrentAssets"),
            getBigDecimal(root, "CashAndCashEquivalents"),
            getBigDecimal(root, "TradeAndOtherCurrentReceivables"),
            getBigDecimal(root, "Inventories"),
            getBigDecimal(root, "PropertyPlantAndEquipment"),
            getBigDecimal(root, "InvestmentProperty"),
            getBigDecimal(root, "Goodwill"),
            getBigDecimal(root, "IntangibleAssetsOtherThanGoodwill"),
            getBigDecimal(root, "DeferredTaxAssets"),
            getBigDecimal(root, "RightofuseAssetsThatDoNotMeetDefinitionOfInvestmentProperty"),

            getBigDecimal(root, "Liabilities"),
            getBigDecimal(root, "CurrentLiabilities"),
            getBigDecimal(root, "TradeAndOtherCurrentPayables"),
            getBigDecimal(root, "NoncurrentLiabilities"),
            getBigDecimal(root, "DeferredTaxLiabilities"),
            getBigDecimal(root, "CurrentLeaseLiabilities"),
            getBigDecimal(root, "NoncurrentLeaseLiabilities"),

            getBigDecimal(root, "Equity"),
            getBigDecimal(root, "IssuedCapital"),
            getBigDecimal(root, "SharePremium"),
            getBigDecimal(root, "RetainedEarnings"),
            getBigDecimal(root, "TreasuryShares"),
            getBigDecimal(root, "OtherReserves"),
            getBigDecimal(root, "NoncontrollingInterests"),

            getBigDecimal(root, "EquityAndLiabilities")
        );
    }

    private BigDecimal getBigDecimal(JsonNode node, String field) {
        if (node.has(field) && node.get(field).isArray() && node.get(field).size() > 1) {
            return node.get(field).get(1).decimalValue();
        }
        return null; // Retorna null si no existe el campo o no tiene valor
    }
}
