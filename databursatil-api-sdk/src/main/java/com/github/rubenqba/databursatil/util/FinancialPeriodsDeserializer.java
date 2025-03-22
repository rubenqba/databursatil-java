package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FinancialPeriodsDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        String text = jp.getText();
        return (text == null || text.trim().isEmpty() || "null".equals(text))
                ? Collections.emptyList()
                : Arrays.asList(text.split(",\\s*"));
    }
}