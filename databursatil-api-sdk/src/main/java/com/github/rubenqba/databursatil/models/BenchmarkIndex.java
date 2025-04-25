package com.github.rubenqba.databursatil.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BenchmarkIndex(
        String code,           // Código de pizarra (ej: "IPC", "DJIA")
        String name,
        BigDecimal lastPrice,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal percentChange,
        BigDecimal absoluteChange,
        Long volume,
        BigDecimal percentChangeYTD,
        OffsetDateTime timestamp  // Usamos OffsetDateTime para zona horaria
) {
}
