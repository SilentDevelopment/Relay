package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.DoubleParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleParserTest {

    @Test
    void parsesDouble() {
        DoubleParser parser = new DoubleParser();
        ParseResult<Double> result = parser.parse(DefaultArgumentReader.of("1.5"));

        assertTrue(result.isSuccess());
        assertEquals(1.5d, result.getValue());
    }

    @Test
    void failsForInvalidDouble() {
        DoubleParser parser = new DoubleParser();
        ParseResult<Double> result = parser.parse(DefaultArgumentReader.of("abc"));

        assertTrue(result.isFailure());
        assertEquals("Invalid Double: abc", result.getError());
    }

}