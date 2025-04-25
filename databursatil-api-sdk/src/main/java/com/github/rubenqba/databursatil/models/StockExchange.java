package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StockExchange {

    @JsonProperty("BMV") BMV("BMV", "Bolsa Mexicana de Valores"),
    @JsonProperty("BIVA") BIVA("BIVA", "Bolsa Institucional de Valores");

    private  final String code;
    private  final String name;

    StockExchange(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code() {
        return code;
    }

    public String getDescription() {
        return name;
    }
}
