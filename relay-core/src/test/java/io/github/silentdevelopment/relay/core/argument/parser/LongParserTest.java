package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.LongParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongParserTest {

    @Test
    void parsesLong() {
        LongParser parser = new LongParser();
        ParseResult<Long> result = parser.parse(DefaultArgumentReader.of("42"));

        assertTrue(result.isSuccess());
        assertEquals(42L, result.getValue());
    }

    @Test
    void failsForInvalidLong() {
        LongParser parser = new LongParser();
        ParseResult<Long> result = parser.parse(DefaultArgumentReader.of("abc"));

        assertTrue(result.isFailure());
        assertEquals("Invalid Long: abc", result.getError());
    }

}