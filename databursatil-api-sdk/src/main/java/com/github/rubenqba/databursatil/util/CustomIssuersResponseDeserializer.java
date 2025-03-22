package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.Issuer;
import com.github.rubenqba.databursatil.models.Series;
import com.github.rubenqba.databursatil.models.api.IssuersResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomIssuersResponseDeserializer extends JsonDeserializer<IssuersResponse> {

    @Override
    public IssuersResponse deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        JsonNode root = p.getCodec().readTree(p);
        List<Issuer> issuers = new ArrayList<>();

        root.fieldNames().forEachRemaining(index -> {
            JsonNode issuerContainer = root.get(index);
            issuerContainer.fieldNames().forEachRemaining(code -> {
                JsonNode seriesListNode = issuerContainer.get(code);
                List<Series> seriesList = new ArrayList<>();

                seriesListNode.fieldNames().forEachRemaining(seriesIdx -> {
                    JsonNode seriesNode = seriesListNode.get(seriesIdx);
                    try {
                        Series series = p.getCodec().treeToValue(seriesNode, Series.class);
                        seriesList.add(series);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error parsing Series node", e);
                    }
                });

                issuers.add(new Issuer(code, seriesList));
            });
        });

        return new IssuersResponse(issuers);
    }
}