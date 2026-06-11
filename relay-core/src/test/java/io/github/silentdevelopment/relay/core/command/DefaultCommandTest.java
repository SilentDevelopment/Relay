package io.github.silentdevelopment.relay.core.command;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandTest {

    @Test
    void duplicateAliasIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> CommandBuilder.<String>literal("ping")
                .alias("p")
                .alias("p")
                .noArgs()
                .build());

        assertEquals("duplicate alias: p", ex.getMessage());
    }

    @Test
    void aliasMatchingNameIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> CommandBuilder.<String>literal("ping")
                .alias("ping")
                .noArgs()
                .build());

        assertEquals("alias cannot be the same as the command name: ping", ex.getMessage());
    }

    @Test
    void duplicateSubcommandLiteralIsRejected() {
        Command childOne = CommandBuilder.<String>literal("child").noArgs().build();
        Command childTwo = CommandBuilder.<String>literal("child").noArgs().build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> CommandBuilder.<String>literal("root")
                .subcommand(childOne)
                .subcommand(childTwo)
                .noArgs()
                .build());

        assertEquals("duplicate subcommand name: child", ex.getMessage());
    }

}