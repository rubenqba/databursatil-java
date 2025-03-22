package com.github.rubenqba.databursatil.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.Quote;
import com.github.rubenqba.databursatil.util.CustomQuoteResponseDeserializer;

import java.util.List;

@JsonDeserialize(using = CustomQuoteResponseDeserializer.class)
public record QuoteResponse(List<Quote> quotes) {
}
