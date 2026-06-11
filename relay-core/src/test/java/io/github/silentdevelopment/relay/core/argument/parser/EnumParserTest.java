package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.EnumParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumParserTest {

    private enum Mode {
        ONE,
        TWO
    }

    @Test
    void parsesEnumIgnoringCase() {
        EnumParser<Mode> parser = new EnumParser<>(Mode.class);
        ParseResult<Mode> result = parser.parse(DefaultArgumentReader.of("one"));

        assertTrue(result.isSuccess());
        assertEquals(Mode.ONE, result.getValue());
    }

    @Test
    void parsesDashedEnumName() {
        EnumParser<Mode> parser = new EnumParser<>(Mode.class);
        ParseResult<Mode> result = parser.parse(DefaultArgumentReader.of("two"));

        assertTrue(result.isSuccess());
        assertEquals(Mode.TWO, result.getValue());
    }

    @Test
    void failsForInvalidValue() {
        EnumParser<Mode> parser = new EnumParser<>(Mode.class);
        ParseResult<Mode> result = parser.parse(DefaultArgumentReader.of("invalid"));

        assertTrue(result.isFailure());
        assertEquals("Invalid mode: invalid", result.getError());
    }

}