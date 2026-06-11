package io.github.silentdevelopment.relay.core.text;

import io.github.silentdevelopment.relay.text.CommandText;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringCommandResponseRendererTest {

    @Test
    void rendersSuggestions() {
        StringCommandResponseRenderer renderer = new StringCommandResponseRenderer();
        String rendered = renderer.renderSuggestions(List.of("one", "two"));

        assertTrue(rendered.contains("one"));
        assertTrue(rendered.contains("two"));
    }

    @Test
    void rendersRequirementFailure() {
        StringCommandResponseRenderer renderer = new StringCommandResponseRenderer();
        String rendered = renderer.renderRequirementFailure(CommandText.of("requirement_failed", "Forbidden."));

        assertTrue(rendered.contains("Forbidden."));
    }

}