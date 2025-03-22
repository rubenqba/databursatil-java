package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.IssuersResponse;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class DataMappingTest {

    @Test
    public void test() {
        final var mapper = new ObjectMapper().findAndRegisterModules();

        try (final var is = new FileInputStream("src/test/resources/data/emisoras_globales_am.json")) {
            final var data = mapper.readValue(is, IssuersResponse.class);
            assertThat(data.issuers()).hasSize(27);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
