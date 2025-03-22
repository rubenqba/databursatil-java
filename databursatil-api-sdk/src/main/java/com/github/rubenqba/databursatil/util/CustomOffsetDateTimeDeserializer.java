package com.github.rubenqba.databursatil.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CustomOffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE_ID = ZoneId.of("America/Mexico_City");

    public CustomOffsetDateTimeDeserializer() {
        super(OffsetTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String dateTimeStr = p.getText();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, FORMATTER);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZONE_ID);
        return zonedDateTime.toOffsetDateTime();
    }
}