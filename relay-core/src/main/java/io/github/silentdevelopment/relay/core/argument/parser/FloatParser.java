package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class FloatParser  implements ArgumentParser<Float> {

    @Override
    public ParseResult<Float> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a Float.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(Float.parseFloat(input));
        } catch (NumberFormatException ex) {
            return ParseResult.failure("Invalid Float: " + input);
        }
    }

}
