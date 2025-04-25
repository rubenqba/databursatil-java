package com.github.rubenqba.databursatil.models;

import java.time.LocalDate;

public record IncomeStatement(LocalDate beginPeriod, LocalDate endPeriod, IncomeStatementDetail statement) {
}
