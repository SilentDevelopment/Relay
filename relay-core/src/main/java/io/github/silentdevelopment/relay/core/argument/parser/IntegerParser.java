package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class IntegerParser implements ArgumentParser<Integer> {

    @Override
    public ParseResult<Integer> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected an Integer.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(Integer.parseInt(input));
        } catch (NumberFormatException ex) {
            return ParseResult.failure("Invalid Integer: " + input);
        }
    }

}