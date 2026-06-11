package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerParserTest {

    @Test
    void parsesInteger() {
        IntegerParser parser = new IntegerParser();
        ParseResult<Integer> result = parser.parse(DefaultArgumentReader.of("42"));

        assertTrue(result.isSuccess());
        assertEquals(42, result.getValue());
    }

    @Test
    void failsForInvalidInteger() {
        IntegerParser parser = new IntegerParser();
        ParseResult<Integer> result = parser.parse(DefaultArgumentReader.of("abc"));

        assertTrue(result.isFailure());
        assertEquals("Invalid Integer: abc", result.getError());
    }

    @Test
    void failsWhenMissing() {
        IntegerParser parser = new IntegerParser();
        ParseResult<Integer> result = parser.parse(DefaultArgumentReader.of(""));

        assertTrue(result.isFailure());
        assertEquals("Expected an Integer.", result.getError());
    }

}