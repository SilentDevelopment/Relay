package io.github.silentdevelopment.relay.core.text;

import io.github.silentdevelopment.relay.text.CommandText;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringCommandResponseRendererTest {

    @Test
    void rendersSuggestions() {
        StringCommandResponseRenderer<Object> renderer = new StringCommandResponseRenderer<>();
        String rendered = renderer.renderSuggestions(List.of("one", "two"));

        assertTrue(rendered.contains("one"));
        assertTrue(rendered.contains("two"));
    }

    @Test
    void rendersRequirementFailure() {
        StringCommandResponseRenderer<Object> renderer = new StringCommandResponseRenderer<>();
        String rendered = renderer.renderRequirementFailure(CommandText.of("relay.error.requirement-failed", "Forbidden."));

        assertTrue(rendered.contains("Forbidden."));
    }

    @Test
    void rendersPositionalArguments() {
        StringCommandResponseRenderer<Object> renderer = new StringCommandResponseRenderer<>();
        String rendered = renderer.renderAbort(CommandText.of(
                "relay.error.invalid-value",
                "Invalid value for {0}: {1}",
                "amount",
                "not a number"
        ));

        assertEquals("Invalid value for amount: not a number", rendered);
    }

    @Test
    void rendersNamedPlaceholders() {
        StringCommandResponseRenderer<Object> renderer = new StringCommandResponseRenderer<>();
        String rendered = renderer.renderAbort(CommandText.keyed(
                "relay.error.no-permission",
                "Missing permission: {permission}",
                Map.of("permission", "relay.test")
        ));

        assertEquals("Missing permission: relay.test", rendered);
    }

    @Test
    void rendersNamedPlaceholdersAfterPositionalArguments() {
        StringCommandResponseRenderer<Object> renderer = new StringCommandResponseRenderer<>();
        String rendered = renderer.renderAbort(new CommandText(
                "relay.error.test",
                "Invalid {name}: {0}",
                List.of("bad value"),
                Map.of("name", "amount")
        ));

        assertEquals("Invalid amount: bad value", rendered);
    }

}