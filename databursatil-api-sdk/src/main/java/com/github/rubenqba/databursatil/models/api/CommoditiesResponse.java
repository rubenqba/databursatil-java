package com.github.rubenqba.databursatil.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.CommodityGroup;
import com.github.rubenqba.databursatil.util.CustomCommodityResponseDeserializer;

import java.util.List;

@JsonDeserialize(using = CustomCommodityResponseDeserializer.class)
public record CommoditiesResponse(List<CommodityGroup> groups) {
}
