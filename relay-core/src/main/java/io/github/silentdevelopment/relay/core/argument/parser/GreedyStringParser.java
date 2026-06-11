package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public final class GreedyStringParser implements ArgumentParser<String> {

    @Override
    public ParseResult<String> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a value.");
        }

        return ParseResult.success(reader.readRemaining());
    }

}