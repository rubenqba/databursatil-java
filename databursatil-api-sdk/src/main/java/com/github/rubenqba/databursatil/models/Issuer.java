package com.github.rubenqba.databursatil.models;

import java.util.List;

public record Issuer(String ticker, List<Series> series) {
}
