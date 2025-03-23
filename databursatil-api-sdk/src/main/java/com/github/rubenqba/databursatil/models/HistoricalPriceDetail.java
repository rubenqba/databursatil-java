package com.github.rubenqba.databursatil.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoricalPriceDetail(LocalDate date, BigDecimal closePrice, BigDecimal totalValue) {
}
