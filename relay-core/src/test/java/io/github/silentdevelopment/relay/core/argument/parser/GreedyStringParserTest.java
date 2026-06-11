package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.GreedyStringParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedyStringParserTest {

    @Test
    void readsRemainingInput() {
        GreedyStringParser parser = new GreedyStringParser();
        ParseResult<String> result = parser.parse(DefaultArgumentReader.of("hello world again"));

        assertTrue(result.isSuccess());
        assertEquals("hello world again", result.getValue());
    }

    @Test
    void failsWhenMissing() {
        GreedyStringParser parser = new GreedyStringParser();
        ParseResult<String> result = parser.parse(DefaultArgumentReader.of(""));

        assertTrue(result.isFailure());
        assertEquals("Expected a value.", result.getError());
    }

}
