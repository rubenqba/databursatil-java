package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.BenchmarkIndex;
import com.github.rubenqba.databursatil.models.Issuer;
import com.github.rubenqba.databursatil.models.Series;
import com.github.rubenqba.databursatil.models.api.BenchmarkIndexResponse;
import com.github.rubenqba.databursatil.models.api.IssuersResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomBenchmarkIndexResponseDeserializer extends JsonDeserializer<BenchmarkIndexResponse> {
    @Override
    public BenchmarkIndexResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode root = p.getCodec().readTree(p);
        final var indexes = new ArrayList<BenchmarkIndex>();

        root.fieldNames().forEachRemaining(code -> {
            JsonNode indexContainer = root.get(code);
            final var index = new BenchmarkIndex(
                    code,
                    indexContainer.get("nombre").textValue(),
                    indexContainer.get("ultimo").decimalValue(),
                    indexContainer.get("apertura").decimalValue(),
                    indexContainer.get("maximo").decimalValue(),
                    indexContainer.get("minimo").decimalValue(),
                    indexContainer.get("cambio%").decimalValue(),
                    indexContainer.get("cambio$").decimalValue(),
                    indexContainer.get("volumen").asLong(),
                    indexContainer.get("cambioytd%").decimalValue(),
                    CustomOffsetDateTimeDeserializer.parse(indexContainer.get("tiempo").textValue())
            );
            indexes.add(index);
        });

        return new BenchmarkIndexResponse(indexes);
    }
}
