package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class BooleanParser implements ArgumentParser<Boolean> {

    @Override
    public ParseResult<Boolean> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a boolean.");
        }

        return ParseResult.success(Boolean.parseBoolean(reader.read()));
    }

}
