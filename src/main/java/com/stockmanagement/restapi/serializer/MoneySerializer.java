package com.stockmanagement.restapi.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneySerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(final BigDecimal value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeNumber(value.setScale(2, RoundingMode.HALF_UP));
    }
}