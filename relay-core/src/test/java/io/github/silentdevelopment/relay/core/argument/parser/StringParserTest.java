package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.StringParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringParserTest {

    @Test
    void parsesSingleToken() {
        StringParser parser = new StringParser();
        ParseResult<String> result = parser.parse(DefaultArgumentReader.of("hello"));

        assertTrue(result.isSuccess());
        assertEquals("hello", result.getValue());
    }

    @Test
    void failsWhenMissing() {
        StringParser parser = new StringParser();
        ParseResult<String> result = parser.parse(DefaultArgumentReader.of(""));

        assertTrue(result.isFailure());
        assertEquals("Expected a value.", result.getError());
    }

}