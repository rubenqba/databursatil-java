package com.github.rubenqba.databursatil.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ErrorResponse(@JsonAlias("Error") String error) {
}
