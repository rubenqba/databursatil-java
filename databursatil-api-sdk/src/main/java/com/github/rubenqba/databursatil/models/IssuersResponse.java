package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.util.CustomIssuersResponseDeserializer;

import java.util.List;

@JsonDeserialize(using = CustomIssuersResponseDeserializer.class)
public record IssuersResponse(List<Issuer> issuers) {
}
