package com.github.rubenqba.databursatil.models;

import java.util.List;

public record Issuer(String code, List<Series> series) {
}
