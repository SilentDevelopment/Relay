package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class DoubleParser implements ArgumentParser<Double> {

    @Override
    public ParseResult<Double> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a Double.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(Double.parseDouble(input));
        } catch (NumberFormatException ex) {
            return ParseResult.failure("Invalid Double: " + input);
        }
    }

}
