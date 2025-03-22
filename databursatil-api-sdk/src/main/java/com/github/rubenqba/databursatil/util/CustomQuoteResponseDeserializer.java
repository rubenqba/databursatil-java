package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.Quote;
import com.github.rubenqba.databursatil.models.QuoteDetails;
import com.github.rubenqba.databursatil.models.StockExchange;
import com.github.rubenqba.databursatil.models.api.QuoteResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomQuoteResponseDeserializer extends JsonDeserializer<QuoteResponse> {
    @Override
    public QuoteResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode root = p.getCodec().readTree(p);
        List<Quote> quotes = new ArrayList<>();

        root.fieldNames().forEachRemaining(ticker -> {
            JsonNode tickerContainer = root.get(ticker);
            tickerContainer.fieldNames().forEachRemaining(exchange -> {
               final var exchangeContainer = tickerContainer.get(exchange);
               try {
                   final var detail = p.getCodec().treeToValue(exchangeContainer, QuoteDetails.class);
                   quotes.add(new Quote(ticker, StockExchange.valueOf(exchange), detail));
               } catch (JsonProcessingException e) {
                   throw new RuntimeException("Error parsing quote detail node", e);
               }
            });
        });

        return new QuoteResponse(quotes);
    }
}
