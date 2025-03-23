package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.IncomeStatement;

import java.io.IOException;
import java.math.BigDecimal;

public class IncomeStatementDeserializer extends JsonDeserializer<IncomeStatement> {

    @Override
    public IncomeStatement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        return new IncomeStatement(
                extract(node, "Revenue"),
                extract(node, "CostOfSales"),
                extract(node, "GrossProfit"),

                extract(node, "AdministrativeExpense"),
                extract(node, "DistributionCosts"),
                extract(node, "OtherExpenseByFunction"),

                extract(node, "FinanceIncome"),
                extract(node, "FinanceCosts"),

                extract(node, "ProfitLossBeforeTax"),
                extract(node, "IncomeTaxExpenseContinuingOperations"),
                extract(node, "ProfitLoss"),

                extract(node, "ProfitLossAttributableToOwnersOfParent"),
                extract(node, "ProfitLossAttributableToNoncontrollingInterests"),

                extract(node, "BasicEarningsLossPerShare"),
                extract(node, "DilutedEarningsLossPerShare"),

                extract(node, "ProfitLossFromContinuingOperations"),
                extract(node, "ProfitLossFromDiscontinuedOperations"),

                extract(node, "ShareOfProfitLossOfAssociatesAndJointVenturesAccountedForUsingEquityMethod")
        );
    }

    private BigDecimal extract(JsonNode node, String field) {
        if (node.has(field) && node.get(field).isArray() && node.get(field).size() > 1) {
            return node.get(field).get(1).decimalValue();
        }
        return null; // en caso de ausencia del campo
    }
}
