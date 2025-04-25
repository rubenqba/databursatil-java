package com.github.rubenqba.databursatil.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Commodity(
        String code, // Código de la commodity (ej: "Te", "Cafe", "Oro")
        String unit, // unidad
        BigDecimal lastPrice, // ultimo
        BigDecimal absoluteChange, // cambio$
        BigDecimal percentChange, // cambio%
        BigDecimal weeklyChangePercent, // semanal
        BigDecimal monthlyChangePercent, // mensual
        BigDecimal annualChangePercent, // anual
        BigDecimal percentChangeYTD, // ytd
        LocalDate date // tiempo
) {
}
