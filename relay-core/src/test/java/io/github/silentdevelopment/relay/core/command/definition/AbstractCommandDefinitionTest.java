package io.github.silentdevelopment.relay.core.command.definition;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.core.command.AbstractCommandDefinition;
import io.github.silentdevelopment.relay.core.dispatch.DefaultCommandDispatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractCommandDefinitionTest {

    @Test
    void bindsHandlerIntoDispatcher() {
        TestDefinition definition = new TestDefinition();
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        definition.bind(dispatcher);
        dispatcher.registerRoot(definition.command());

        var result = dispatcher.dispatch("user", "test", "");

        assertTrue(result.isSuccess());
        assertEquals(1, definition.executions);
    }

    private static final class TestDefinition extends AbstractCommandDefinition<String> {

        private int executions;

        @Override
        protected void handle(CommandContext<String> context) {
            this.executions++;
        }

        @Override
        protected Command buildCommand() {
            return io.github.silentdevelopment.relay.core.command.builder.CommandBuilder.<String>literal("test")
                    .noArgs()
                    .build();
        }

    }

}