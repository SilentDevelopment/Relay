package io.github.silentdevelopment.relay.core.match;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import io.github.silentdevelopment.relay.match.CommandMatchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandMatcherTest {

    @Test
    void matchesSubcommand() {
        DefaultCommandMatcher matcher = new DefaultCommandMatcher();
        Command root = CommandFixtures.rootWithSubcommands();

        CommandMatchResult result = matcher.match(root, io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader.of("ping"));

        assertTrue(result.isSuccess());
        assertEquals("ping", result.getMatch().getCommand().name());
    }

    @Test
    void matchesGreedySignature() {
        DefaultCommandMatcher matcher = new DefaultCommandMatcher();
        Command root = CommandFixtures.echoCommand();

        CommandMatchResult result = matcher.match(root, io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader.of("hello world"));

        assertTrue(result.isSuccess());
        assertEquals("hello world", result.getMatch().get(CommandFixtures.MESSAGE));
    }

    @Test
    void failsForUnexpectedTrailingInput() {
        DefaultCommandMatcher matcher = new DefaultCommandMatcher();
        Command root = CommandFixtures.pingCommand();

        CommandMatchResult result = matcher.match(root, io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader.of("extra"));

        assertTrue(result.isFailure());
        assertEquals("Unexpected trailing input: extra", result.getError().orElseThrow());
    }

}