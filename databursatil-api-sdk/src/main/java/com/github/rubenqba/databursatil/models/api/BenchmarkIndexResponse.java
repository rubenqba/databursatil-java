package com.github.rubenqba.databursatil.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.rubenqba.databursatil.models.BenchmarkIndex;
import com.github.rubenqba.databursatil.util.CustomBenchmarkIndexResponseDeserializer;

import java.util.List;

@JsonDeserialize(using = CustomBenchmarkIndexResponseDeserializer.class)
public record BenchmarkIndexResponse(List<BenchmarkIndex> indexes) {
}
