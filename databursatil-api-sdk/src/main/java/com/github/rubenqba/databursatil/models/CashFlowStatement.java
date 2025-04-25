package com.github.rubenqba.databursatil.models;

import java.time.LocalDate;

public record CashFlowStatement(LocalDate beginPeriod, LocalDate endPeriod, CashFlowStatementDetail statement) {
}
