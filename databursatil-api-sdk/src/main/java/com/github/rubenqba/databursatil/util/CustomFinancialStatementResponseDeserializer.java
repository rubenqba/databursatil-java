package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.*;
import com.github.rubenqba.databursatil.models.api.FinancialStatementResponse;
import com.github.rubenqba.databursatil.models.api.ReportReference;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomFinancialStatementResponseDeserializer extends JsonDeserializer<FinancialStatementResponse> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    @Override
    public FinancialStatementResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode root = p.getCodec().readTree(p);

        final var builder = FinancialStatementResponse.builder();

        // deserialize 'posicion' if present
        if (root.has("posicion")) {
            builder.balanceSheet(parseBalanceSheetReports(root.get("posicion"), p));
        }

        // deserialize 'resultado_trimestre' if present
        if (root.has("resultado_trimestre")) {
            builder.incomeQuarterly(parseIncomeReports(root.get("resultado_trimestre"), p));
        }
        // deserialize 'resultado_acumulado' if present
        if (root.has("resultado_acumulado")) {
            builder.incomeYearToDate(parseIncomeReports(root.get("resultado_acumulado"), p));
        }

        // deserialize 'flujos' if present
        if (root.has("flujos")) {
            builder.cashFlow(parseCashFlowReports(root.get("flujos"), p));
        }

        return builder.build();
    }

    private ReportReference<CashFlowStatement> parseCashFlowReports(JsonNode cashFlowContainer, JsonParser p) {
        final var statements = new ArrayList<CashFlowStatement>();
        cashFlowContainer.fieldNames().forEachRemaining(field -> {
            JsonNode statementNode = cashFlowContainer.get(field);
            try {
                var periods = field.split("_");
                var beginPeriod = LocalDate.parse(periods[0], FORMATTER);
                var endPeriod = LocalDate.parse(periods[1], FORMATTER);
                var detail = p.getCodec().treeToValue(statementNode, CashFlowStatementDetail.class);
                statements.add(new CashFlowStatement(beginPeriod, endPeriod, detail));
            } catch (JacksonException e) {
                throw new RuntimeException("Error parsing cash flow report node", e);
            }
        });
        return new ReportReference<>(statements.getFirst(), statements.getLast());
    }

    private ReportReference<BalanceSheetStatement> parseBalanceSheetReports(JsonNode balanceSheetContainer, JsonParser p) {
        List<BalanceSheetStatement> statements = new ArrayList<>();
        balanceSheetContainer.fieldNames().forEachRemaining(field -> {
            JsonNode statementNode = balanceSheetContainer.get(field);
            try {
                var period = LocalDate.parse(field, FORMATTER);
                var detail = p.getCodec().treeToValue(statementNode, BalanceSheetStatementDetail.class);
                statements.add(new BalanceSheetStatement(period, detail));
            } catch (JacksonException e) {
                throw new RuntimeException("Error parsing balance sheet report node", e);
            }
        });
        return new ReportReference<>(statements.getFirst(), statements.getLast());
    }

    private ReportReference<IncomeStatement> parseIncomeReports(JsonNode incomeStatementContainer, JsonParser p) {
        List<IncomeStatement> statements = new ArrayList<>();
        incomeStatementContainer.fieldNames().forEachRemaining(field -> {
            JsonNode statementNode = incomeStatementContainer.get(field);
            try {
                // field has the format 'yyyy-MM-dd_yyyy-MM-dd' and needs to be split into two dates
                var periods = field.split("_");
                var beginPeriod = LocalDate.parse(periods[0], FORMATTER);
                var endPeriod = LocalDate.parse(periods[1], FORMATTER);
                var detail = p.getCodec().treeToValue(statementNode, IncomeStatementDetail.class);
                statements.add(new IncomeStatement(beginPeriod, endPeriod, detail));
            } catch (JacksonException e) {
                throw new RuntimeException("Error parsing quarterly income report node", e);
            }
        });
        return new ReportReference<>(statements.getFirst(), statements.getLast());
    }
}
