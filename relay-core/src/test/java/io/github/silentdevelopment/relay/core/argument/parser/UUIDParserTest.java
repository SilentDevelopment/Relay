package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.core.argument.parser.UUIDParser;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UUIDParserTest {

    @Test
    void parsesUuid() {
        UUID uuid = UUID.randomUUID();
        UUIDParser parser = new UUIDParser();
        ParseResult<UUID> result = parser.parse(DefaultArgumentReader.of(uuid.toString()));

        assertTrue(result.isSuccess());
        assertEquals(uuid, result.getValue());
    }

    @Test
    void failsForInvalidUuid() {
        UUIDParser parser = new UUIDParser();
        ParseResult<UUID> result = parser.parse(DefaultArgumentReader.of("abc"));

        assertTrue(result.isFailure());
        assertEquals("Invalid UUID: abc", result.getError());
    }

}