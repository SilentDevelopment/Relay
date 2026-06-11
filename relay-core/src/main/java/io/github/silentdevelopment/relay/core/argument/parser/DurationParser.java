package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.core.util.DurationUtils;

import java.time.Duration;

public final class DurationParser implements ArgumentParser<Duration> {

    @Override
    public ParseResult<Duration> parse(ArgumentReader reader) {
        String input = reader.read();
        if (input == null) {
            return ParseResult.failure("Duration cannot be null.");
        }

        if (input.isBlank()) {
            return ParseResult.failure("Duration cannot be blank.");
        }

        try {
            return ParseResult.success(DurationUtils.parse(input));
        } catch (IllegalArgumentException ex) {
            return ParseResult.failure("Invalid duration.");
        }
    }
}