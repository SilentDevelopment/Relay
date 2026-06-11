package io.github.silentdevelopment.relay.core.text.usage;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandUsageFormatterTest {

    @Test
    void formatsRootAndSubcommandUsages() {
        DefaultCommandUsageFormatter formatter = new DefaultCommandUsageFormatter();
        Command root = CommandFixtures.rootWithSubcommands();

        List<String> usages = formatter.format(root);

        assertTrue(usages.contains("/root ping"));
        assertTrue(usages.contains("/root echo <message...>"));
    }

}