package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class LongParser implements ArgumentParser<Long> {

    @Override
    public ParseResult<Long> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a Long.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(Long.parseLong(input));
        } catch (NumberFormatException ex) {
            return ParseResult.failure("Invalid Long: " + input);
        }
    }

}
