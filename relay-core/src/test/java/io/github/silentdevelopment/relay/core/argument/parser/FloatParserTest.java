package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.FloatParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FloatParserTest {

    @Test
    void parsesFloat() {
        FloatParser parser = new FloatParser();
        ParseResult<Float> result = parser.parse(DefaultArgumentReader.of("1.5"));

        assertTrue(result.isSuccess());
        assertEquals(1.5f, result.getValue());
    }

    @Test
    void failsForInvalidFloat() {
        FloatParser parser = new FloatParser();
        ParseResult<Float> result = parser.parse(DefaultArgumentReader.of("abc"));

        assertTrue(result.isFailure());
        assertEquals("Invalid Float: abc", result.getError());
    }

}