package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

import java.text.NumberFormat;
import java.text.ParseException;

public final class NumberParser implements ArgumentParser<Number> {

    @Override
    public ParseResult<Number> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a Number.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(NumberFormat.getInstance().parse(input));
        } catch (ParseException ex) {
            return ParseResult.failure("Invalid Number: " + input);
        }
    }

}
