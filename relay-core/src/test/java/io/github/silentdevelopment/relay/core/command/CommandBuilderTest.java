package io.github.silentdevelopment.relay.core.command;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.argument.ArgumentTypes;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.command.option.DefaultCommandFlag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandBuilderTest {

    @Test
    void buildsSimpleCommand() {
        Command command = CommandBuilder.<String>literal("ping")
                .description("Ping command.")
                .alias("p")
                .noArgs()
                .build();

        assertEquals("ping", command.name());
        assertEquals("Ping command.", command.description());
        assertEquals(List.of("p"), command.aliases());
        assertEquals(1, command.signatures().size());
    }

    @Test
    void buildsCommandWithSignatureAndFlag() {
        Argument<String> target = Argument.required("target", ArgumentTypes.STRING);

        Command command = CommandBuilder.<String>literal("ban")
                .signature(target)
                .flag(new DefaultCommandFlag("silent", "Silent mode.", List.of("s")))
                .build();

        assertEquals(1, command.signatures().size());
        assertEquals(1, command.options().size());
    }

    @Test
    void nullAliasesArrayIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> CommandBuilder.<String>literal("ping").aliases((String[]) null));
        assertEquals("aliases cannot be null.", ex.getMessage());
    }

}