package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rubenqba.databursatil.models.Commodity;
import com.github.rubenqba.databursatil.models.CommodityGroup;
import com.github.rubenqba.databursatil.models.api.CommoditiesResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class CustomCommodityResponseDeserializer extends JsonDeserializer<CommoditiesResponse> {
    @Override
    public CommoditiesResponse deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode root = p.getCodec().readTree(p);
        final var groups = new ArrayList<CommodityGroup>();

        root.fieldNames().forEachRemaining(group -> {
            JsonNode groupNode = root.get(group);
            if (!groupNode.isContainerNode()) {
                return;
            }
            final var commodities = new ArrayList<Commodity>();

            groupNode.fieldNames().forEachRemaining(code -> {
                JsonNode commodityNode = groupNode.get(code);
                final var commodity = new Commodity(
                        code,
                        commodityNode.get("unidad").textValue(),
                        commodityNode.get("ultimo").decimalValue(),
                        commodityNode.get("cambio$").decimalValue(),
                        commodityNode.get("cambio%").decimalValue(),
                        commodityNode.get("semanal").decimalValue(),
                        commodityNode.get("mensual").decimalValue(),
                        commodityNode.get("anual").decimalValue(),
                        commodityNode.get("ytd").decimalValue(),
                        LocalDate.parse(commodityNode.get("tiempo").textValue(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                );
                commodities.add(commodity);
            });
            commodities.sort(Comparator.comparing(Commodity::code));
            groups.add(new CommodityGroup(group, commodities));
        });
        groups.sort(Comparator.comparing(CommodityGroup::name));
        return new CommoditiesResponse(groups);
    }
}
