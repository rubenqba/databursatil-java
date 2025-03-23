package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.HistoricalPriceDetail;
import com.github.rubenqba.databursatil.models.api.HistoricalResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoricalResponseDeserializer extends JsonDeserializer<HistoricalResponse> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public HistoricalResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode root = p.getCodec().readTree(p);
        List<HistoricalPriceDetail> prices = new ArrayList<>();

        root.fieldNames().forEachRemaining(dateString -> {
            JsonNode valuesNode = root.get(dateString);

            LocalDate date = LocalDate.parse(dateString, FORMATTER);
            BigDecimal closePrice = valuesNode.get(0).decimalValue();
            BigDecimal totalValue = valuesNode.get(1).decimalValue();

            prices.add(new HistoricalPriceDetail(date, closePrice, totalValue));
        });

        // Opcional: ordenar por fecha si lo deseas
        prices.sort(Comparator.comparing(HistoricalPriceDetail::date));

        return new HistoricalResponse(prices);
    }
}
