package io.github.silentdevelopment.relay.core.suggestion;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.argument.ArgumentTypes;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandSuggesterTest {

    @Test
    void suggestsSubcommandsAtRoot() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<String> suggestions = suggester.suggest("user", root, "");

        assertTrue(suggestions.contains("ping"));
        assertTrue(suggestions.contains("echo"));
    }

    @Test
    void suggestsArgumentValuesFromProvider() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Argument<String> mode = Argument.required("mode", ArgumentTypes.STRING);

        Command command = CommandBuilder.<String>literal("mode")
                .signature(mode)
                .suggest(mode, new CommandFixtures.RecordingSuggestionProvider<>(List.of("survival", "creative")))
                .build();

        List<String> suggestions = suggester.suggest("user", command, "");

        assertEquals(List.of("survival", "creative"), suggestions);
    }

    @Test
    void filtersSuggestionsByPartial() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Argument<String> mode = Argument.required("mode", ArgumentTypes.STRING);

        Command command = CommandBuilder.<String>literal("mode")
                .signature(mode)
                .suggest(mode, new CommandFixtures.RecordingSuggestionProvider<>(List.of("survival", "creative")))
                .build();

        List<String> suggestions = suggester.suggest("user", command, "s");

        assertEquals(List.of("survival"), suggestions);
    }

}