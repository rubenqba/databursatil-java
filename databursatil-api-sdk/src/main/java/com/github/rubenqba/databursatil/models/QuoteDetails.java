package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.util.CustomOffsetDateTimeDeserializer;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record QuoteDetails(
        @JsonProperty("U")
        @JsonAlias({"ultimo"})
        BigDecimal lastPrice,               // Último precio
        @JsonProperty("P")
        @JsonAlias({"ppp"})
        BigDecimal volumeWeightedAveragePrice, // Precio Promedio Ponderado
        @JsonProperty("A")
        BigDecimal previousClose,                       // Cierre anterior
        @JsonProperty("X") BigDecimal dayHigh,                             // Máximo del día
        @JsonProperty("N") BigDecimal dayLow,                              // Mínimo del día
        @JsonProperty("C")
        @JsonAlias({"cambio%"})
        BigDecimal percentageChange,       // Cambio porcentual
        @JsonProperty("M")
        @JsonAlias({"cambio$"})
        BigDecimal currencyChange,         // Cambio en pesos/moneda
        @JsonProperty("V") long volume,                                    // Volumen operado (en títulos)
        @JsonProperty("O") int trades,                                     // Cantidad de operaciones
        @JsonProperty("I") BigDecimal totalValue,                          // Importe acumulado (en dinero)
        @JsonProperty("F")
        @JsonAlias({"tiempo"})
        @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
        OffsetDateTime timestamp                                        // Fecha y hora de la cotización
) {

}