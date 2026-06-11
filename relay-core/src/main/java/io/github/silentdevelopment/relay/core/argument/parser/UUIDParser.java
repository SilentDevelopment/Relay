package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

import java.util.UUID;

public final class UUIDParser implements ArgumentParser<UUID> {

    @Override
    public ParseResult<UUID> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a UUID.");
        }

        String input = reader.read();

        try {
            return ParseResult.success(UUID.fromString(input));
        } catch (IllegalArgumentException ex) {
            return ParseResult.failure("Invalid UUID: " + input);
        }
    }

}
