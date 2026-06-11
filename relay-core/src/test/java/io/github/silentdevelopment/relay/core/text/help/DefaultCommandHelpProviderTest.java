package io.github.silentdevelopment.relay.core.text.help;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandHelpProviderTest {

    @Test
    void buildsEntriesForRootAndAccessibleChildren() {
        DefaultCommandHelpProvider<String> provider = new DefaultCommandHelpProvider<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<CommandHelpEntry> entries = provider.build("user", root);

        assertFalse(entries.isEmpty());
        assertEquals("/root", entries.getFirst().path());
        assertTrue(entries.stream().anyMatch(entry -> entry.path().equals("/root ping")));
        assertTrue(entries.stream().anyMatch(entry -> entry.path().equals("/root echo")));
    }

    @Test
    void hidesRestrictedSubcommandsFromUnauthorizedUser() {
        DefaultCommandHelpProvider<String> provider = new DefaultCommandHelpProvider<>();
        Command root = CommandFixtures.rootWithSubcommands();

        List<CommandHelpEntry> entries = provider.build("guest", root);

        assertFalse(entries.stream().anyMatch(entry -> entry.path().equals("/root secure")));
    }

}