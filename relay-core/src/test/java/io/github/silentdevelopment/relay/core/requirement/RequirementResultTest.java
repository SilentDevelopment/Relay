package io.github.silentdevelopment.relay.core.requirement;

import io.github.silentdevelopment.relay.requirement.RequirementResult;
import io.github.silentdevelopment.relay.text.CommandText;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequirementResultTest {

    @Test
    void allowCreatesAllowedResultWithoutMessage() {
        RequirementResult result = RequirementResult.allow();

        assertTrue(result.isAllowed());
        assertFalse(result.isDenied());
        assertTrue(result.getText().isEmpty());
        assertTrue(result.getMessage().isEmpty());
    }

    @Test
    void denyStringCreatesDeniedResultWithFallbackOnlyText() {
        RequirementResult result = RequirementResult.deny("Forbidden.");

        assertFalse(result.isAllowed());
        assertTrue(result.isDenied());
        assertTrue(result.getText().isPresent());
        assertNull(result.getText().orElseThrow().key());
        assertEquals("Forbidden.", result.getText().orElseThrow().fallback());
        assertEquals("Forbidden.", result.getMessage().orElseThrow());
    }

    @Test
    void denyCommandTextPreservesKeyFallbackAndPlaceholders() {
        CommandText text = CommandText.keyed(
                "relay.error.no-permission",
                "Missing {permission}.",
                Map.of("permission", "relay.test")
        );

        RequirementResult result = RequirementResult.deny(text);

        assertFalse(result.isAllowed());
        assertTrue(result.isDenied());
        assertSame(text, result.getText().orElseThrow());
        assertEquals("relay.error.no-permission", result.getText().orElseThrow().key());
        assertEquals("Missing {permission}.", result.getText().orElseThrow().fallback());
        assertEquals(Map.of("permission", "relay.test"), result.getText().orElseThrow().placeholders());
    }

    @Test
    void legacyGetMessageReturnsFallback() {
        RequirementResult result = RequirementResult.deny(CommandText.keyed(
                "relay.error.test",
                "Fallback message.",
                Map.of("name", "value")
        ));

        assertEquals("Fallback message.", result.getMessage().orElseThrow());
    }

    @Test
    void denyRejectsNullCommandText() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> RequirementResult.deny((CommandText) null));

        assertEquals("message cannot be null.", exception.getMessage());
    }

}