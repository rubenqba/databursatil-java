package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.util.FinancialPeriodsDeserializer;

import java.util.List;

public record Series(
        @JsonProperty("Serie")
        String series,

        @JsonProperty("Razon Social")
        String companyName,

        @JsonProperty("ISIN")
        String isin,

        @JsonProperty("Bolsa")
        String exchange,

        @JsonProperty("Tipo Valor Descripcion")
        String securityTypeDescription,

        @JsonProperty("Tipo Valor ID")
        String securityTypeId,

        @JsonProperty("Estatus")
        String status,

        @JsonProperty("Acciones en Circulacion")
        Long outstandingShares,

        @JsonProperty("Rango Historicos")
        String historicalRange,

        @JsonProperty("Rango Financieros")
        @JsonDeserialize(using = FinancialPeriodsDeserializer.class)
        List<String> financialPeriods
) {
//    public static Series parseFromJson(JsonNode node) {
//        Series series = new ObjectMapper().convertValue(node, Series.class);
//        series.financialPeriods = node.has("Rango Financieros") ?
//                Arrays.asList(node.get("Rango Financieros").asText().split(", ")) : Collections.emptyList();
//        return series;
//    }
}