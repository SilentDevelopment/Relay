package io.github.silentdevelopment.relay.core.command.definition;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.core.command.AbstractCommandDefinition;
import io.github.silentdevelopment.relay.core.command.AbstractCommandGroupDefinition;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.dispatch.DefaultCommandDispatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractCommandGroupDefinitionTest {

    @Test
    void bindsSelfAndChildren() {
        TestGroup group = new TestGroup();
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        group.bind(dispatcher);
        dispatcher.registerRoot(group.command());

        var rootResult = dispatcher.dispatch("user", "root", "");
        var childResult = dispatcher.dispatch("user", "root", "child");

        assertTrue(rootResult.isSuccess());
        assertTrue(childResult.isSuccess());
        assertEquals(1, group.executions);
        assertEquals(1, group.child.executions);
    }

    private static final class TestGroup extends AbstractCommandGroupDefinition<String> {

        private final TestChild child = new TestChild();
        private int executions;

        @Override
        public List<? extends CommandDefinition<String>> children() {
            return List.of(this.child);
        }

        @Override
        protected void handle(CommandContext<String> context) {
            this.executions++;
        }

        @Override
        protected CommandBuilder<String> buildCommand() {
            return CommandBuilder.<String>literal("root").noArgs();
        }

    }

    private static final class TestChild extends AbstractCommandDefinition<String> {

        private int executions;

        @Override
        protected void handle(CommandContext<String> context) {
            this.executions++;
        }

        @Override
        protected Command buildCommand() {
            return CommandBuilder.<String>literal("child").noArgs().build();
        }

    }

}