package com.github.rubenqba.databursatil.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.HistoricalPriceDetail;
import com.github.rubenqba.databursatil.util.HistoricalResponseDeserializer;

import java.util.List;

@JsonDeserialize(using = HistoricalResponseDeserializer.class)
public record HistoricalResponse(List<HistoricalPriceDetail> prices) {
}