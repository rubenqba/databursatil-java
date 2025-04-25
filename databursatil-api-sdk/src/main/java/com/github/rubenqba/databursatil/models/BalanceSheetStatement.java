package com.github.rubenqba.databursatil.models;

import java.time.LocalDate;

public record BalanceSheetStatement(LocalDate period, BalanceSheetStatementDetail statement) {
}
