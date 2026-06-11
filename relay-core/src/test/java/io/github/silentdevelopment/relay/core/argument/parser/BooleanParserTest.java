package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.BooleanParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanParserTest {

    @Test
    void parsesTrue() {
        BooleanParser parser = new BooleanParser();
        ParseResult<Boolean> result = parser.parse(DefaultArgumentReader.of("true"));

        assertTrue(result.isSuccess());
        assertTrue(result.getValue());
    }

    @Test
    void parsesFalseForNonTrueInput() {
        BooleanParser parser = new BooleanParser();
        ParseResult<Boolean> result = parser.parse(DefaultArgumentReader.of("nope"));

        assertTrue(result.isSuccess());
        assertFalse(result.getValue());
    }

    @Test
    void failsWhenMissing() {
        BooleanParser parser = new BooleanParser();
        ParseResult<Boolean> result = parser.parse(DefaultArgumentReader.of(""));

        assertTrue(result.isFailure());
        assertEquals("Expected a boolean.", result.getError());
    }

}