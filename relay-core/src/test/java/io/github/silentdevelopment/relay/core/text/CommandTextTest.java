package io.github.silentdevelopment.relay.core.text;

import io.github.silentdevelopment.relay.text.CommandText;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommandTextTest {

    @Test
    void storesKeyFallbackArgumentsAndPlaceholders() {
        CommandText text = new CommandText(
                "relay.error.test",
                "Test {name}: {0}",
                List.of("argument"),
                Map.of("name", "value")
        );

        assertEquals("relay.error.test", text.key());
        assertEquals("Test {name}: {0}", text.fallback());
        assertEquals(List.of("argument"), text.arguments());
        assertEquals(Map.of("name", "value"), text.placeholders());
    }

    @Test
    void keyedCreatesTextWithNamedPlaceholders() {
        CommandText text = CommandText.keyed(
                "relay.error.no-permission",
                "Missing {permission}.",
                Map.of("permission", "relay.test")
        );

        assertEquals("relay.error.no-permission", text.key());
        assertEquals("Missing {permission}.", text.fallback());
        assertEquals(List.of(), text.arguments());
        assertEquals(Map.of("permission", "relay.test"), text.placeholders());
    }

    @Test
    void withPlaceholderAddsPlaceholderWithoutMutatingOriginal() {
        CommandText original = CommandText.keyed(
                "relay.error.test",
                "Missing {permission} from {source}.",
                Map.of("permission", "relay.old")
        );

        CommandText updated = original.withPlaceholder("source", "tester");

        assertEquals(Map.of("permission", "relay.old"), original.placeholders());
        assertEquals(Map.of(
                "permission", "relay.old",
                "source", "tester"
        ), updated.placeholders());
    }

    @Test
    void withPlaceholderOverridesExistingPlaceholderOnNewInstanceOnly() {
        CommandText original = CommandText.keyed(
                "relay.error.test",
                "Missing {permission}.",
                Map.of("permission", "relay.old")
        );

        CommandText updated = original.withPlaceholder("permission", "relay.new");

        assertEquals(Map.of("permission", "relay.old"), original.placeholders());
        assertEquals(Map.of("permission", "relay.new"), updated.placeholders());
    }

    @Test
    void withPlaceholdersMergesPlaceholdersWithoutMutatingOriginal() {
        CommandText original = CommandText.keyed(
                "relay.error.test",
                "Missing {permission} from {source}.",
                Map.of("permission", "relay.old")
        );

        CommandText updated = original.withPlaceholders(Map.of(
                "permission", "relay.new",
                "source", "tester"
        ));

        assertEquals(Map.of("permission", "relay.old"), original.placeholders());
        assertEquals(Map.of(
                "permission", "relay.new",
                "source", "tester"
        ), updated.placeholders());
    }

    @Test
    void nullArgumentListBecomesEmptyList() {
        CommandText text = new CommandText("relay.error.test", "Fallback.", null, Map.of());

        assertEquals(List.of(), text.arguments());
    }

    @Test
    void nullPlaceholderMapBecomesEmptyMap() {
        CommandText text = new CommandText("relay.error.test", "Fallback.", List.of(), null);

        assertEquals(Map.of(), text.placeholders());
    }

    @Test
    void rejectsNullFallback() {
        assertThrows(NullPointerException.class, () -> CommandText.of("relay.error.test", (String) null));
    }

    @Test
    void rejectsBlankFallback() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandText.of("relay.error.test", " "));

        assertEquals("fallback cannot be blank.", exception.getMessage());
    }

    @Test
    void rejectsBlankKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandText.of(" ", "Fallback."));

        assertEquals("key cannot be blank.", exception.getMessage());
    }

}