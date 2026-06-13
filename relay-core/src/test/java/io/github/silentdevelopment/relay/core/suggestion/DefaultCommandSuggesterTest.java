package io.github.silentdevelopment.relay.core.suggestion;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import io.github.silentdevelopment.relay.core.argument.ArgumentTypes;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.requirement.PredicateCommandRequirement;
import io.github.silentdevelopment.relay.text.CommandText;
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

    @Test
    void returnsNoSuggestionsWhenRootIsInaccessible() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> false,
                        CommandText.of("relay.test.root-denied", "Root denied.")
                ))
                .subcommand(CommandFixtures.pingCommand())
                .build();

        List<String> suggestions = suggester.suggest("guest", root, "");

        assertEquals(List.of(), suggestions);
    }

    @Test
    void hidesInaccessibleSubcommands() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<String> suggestions = suggester.suggest("guest", root, "");

        assertTrue(suggestions.contains("ping"));
        assertTrue(suggestions.contains("echo"));
        assertFalse(suggestions.contains("secure"));
    }

    @Test
    void suggestsAccessibleRestrictedSubcommands() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<String> suggestions = suggester.suggest("admin", root, "");

        assertTrue(suggestions.contains("secure"));
    }

    @Test
    void doesNotEnterInaccessibleSubcommandPath() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<String> suggestions = suggester.suggest("guest", root, "secure ");

        assertEquals(List.of(), suggestions);
    }

    @Test
    void suggestsNestedAccessibleSubcommandsOnlyWhenParentIsAccessible() {
        DefaultCommandSuggester<String> suggester = new DefaultCommandSuggester<>();

        Command nested = CommandBuilder.<String>literal("nested")
                .description("Nested command.")
                .noArgs()
                .build();

        Command secure = CommandBuilder.<String>literal("secure")
                .description("Secure command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.equals("admin"),
                        CommandText.of("relay.test.secure-denied", "Secure denied.")
                ))
                .subcommand(nested)
                .build();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .subcommand(secure)
                .build();

        assertEquals(List.of(), suggester.suggest("guest", root, "secure "));
        assertEquals(List.of("nested"), suggester.suggest("admin", root, "secure "));
    }

}