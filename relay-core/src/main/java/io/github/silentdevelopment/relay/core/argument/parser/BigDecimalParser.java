package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

import java.math.BigDecimal;

public final class BigDecimalParser implements ArgumentParser<BigDecimal> {

    @Override
    public ParseResult<BigDecimal> parse(ArgumentReader reader) {
        String input = reader.read();
        if (input == null) {
            return ParseResult.failure("Number cannot be null.");
        }

        if (input.isBlank()) {
            return ParseResult.failure("Number cannot be blank.");
        }

        try {
            return ParseResult.success(new BigDecimal(input));
        } catch (NumberFormatException ex) {
            return ParseResult.failure("Invalid decimal number.");
        }
    }

}