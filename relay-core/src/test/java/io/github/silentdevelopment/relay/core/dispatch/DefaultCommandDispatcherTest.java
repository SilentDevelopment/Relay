package io.github.silentdevelopment.relay.core.dispatch;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.requirement.PredicateCommandRequirement;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchStatus;
import io.github.silentdevelopment.relay.text.CommandText;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandDispatcherTest {

    @Test
    void dispatchesBoundRootCommand() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();
        Command command = CommandFixtures.echoCommand();
        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(command);
        dispatcher.bind(command, handler);

        CommandDispatchResult result = dispatcher.dispatch("user", "echo", "hello world");

        assertEquals(CommandDispatchStatus.SUCCESS, result.getStatus());
        assertEquals(1, handler.executions());
    }

    @Test
    void returnsUnknownCommandWhenRootMissing() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        CommandDispatchResult result = dispatcher.dispatch("user", "missing", "");

        assertEquals(CommandDispatchStatus.UNKNOWN_COMMAND, result.getStatus());
    }

    @Test
    void returnsRequirementFailedWhenAccessDenied() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();
        Command command = CommandFixtures.secureCommand();
        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(command);
        dispatcher.bind(command, handler);

        CommandDispatchResult result = dispatcher.dispatch("guest", "secure", "");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertEquals(0, handler.executions());
    }

    @Test
    void returnsNoHandlerWhenNotBound() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();
        Command command = CommandFixtures.pingCommand();

        dispatcher.registerRoot(command);

        CommandDispatchResult result = dispatcher.dispatch("user", "ping", "");

        assertEquals(CommandDispatchStatus.NO_HANDLER, result.getStatus());
    }

    @Test
    void parentRequirementDeniesChildCommand() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();
        Command child = CommandBuilder.<String>literal("child")
                .description("Child command.")
                .noArgs()
                .build();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.equals("admin"),
                        CommandText.keyed(
                                "relay.test.root-denied",
                                "Root denied for {source}.",
                                Map.of("source", "guest")
                        )
                ))
                .subcommand(child)
                .build();

        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(root);
        dispatcher.bind(child, handler);

        CommandDispatchResult result = dispatcher.dispatch("guest", "root", "child");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertSame(root, result.getCommand().orElseThrow());
        assertEquals("relay.test.root-denied", result.getMessage().orElseThrow().key());
        assertEquals("Root denied for {source}.", result.getMessage().orElseThrow().fallback());
        assertEquals(Map.of("source", "guest"), result.getMessage().orElseThrow().placeholders());
        assertEquals(0, handler.executions());
    }

    @Test
    void parentRequirementIsCheckedBeforeChildRequirement() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        Command child = CommandBuilder.<String>literal("child")
                .description("Child command.")
                .noArgs()
                .requirement(new PredicateCommandRequirement<>(
                        source -> false,
                        CommandText.of("relay.test.child-denied", "Child denied.")
                ))
                .build();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> false,
                        CommandText.of("relay.test.root-denied", "Root denied.")
                ))
                .subcommand(child)
                .build();

        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(root);
        dispatcher.bind(child, handler);

        CommandDispatchResult result = dispatcher.dispatch("user", "root", "child");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertSame(root, result.getCommand().orElseThrow());
        assertEquals("relay.test.root-denied", result.getMessage().orElseThrow().key());
        assertEquals("Root denied.", result.getMessage().orElseThrow().fallback());
        assertEquals(0, handler.executions());
    }

    @Test
    void childRequirementDeniesWhenParentAllows() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        Command child = CommandBuilder.<String>literal("child")
                .description("Child command.")
                .noArgs()
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.equals("admin"),
                        CommandText.of("relay.test.child-denied", "Child denied.")
                ))
                .build();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.startsWith("user"),
                        CommandText.of("relay.test.root-denied", "Root denied.")
                ))
                .subcommand(child)
                .build();

        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(root);
        dispatcher.bind(child, handler);

        CommandDispatchResult result = dispatcher.dispatch("user-1", "root", "child");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertSame(child, result.getCommand().orElseThrow());
        assertEquals("relay.test.child-denied", result.getMessage().orElseThrow().key());
        assertEquals("Child denied.", result.getMessage().orElseThrow().fallback());
        assertEquals(0, handler.executions());
    }

    @Test
    void childCommandDispatchesWhenParentAndChildRequirementsAllow() {
        DefaultCommandDispatcher<String> dispatcher = new DefaultCommandDispatcher<>();

        Command child = CommandBuilder.<String>literal("child")
                .description("Child command.")
                .noArgs()
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.equals("admin"),
                        CommandText.of("relay.test.child-denied", "Child denied.")
                ))
                .build();

        Command root = CommandBuilder.<String>literal("root")
                .description("Root command.")
                .requirement(new PredicateCommandRequirement<>(
                        source -> source.equals("admin"),
                        CommandText.of("relay.test.root-denied", "Root denied.")
                ))
                .subcommand(child)
                .build();

        CommandFixtures.RecordingHandler<String> handler = new CommandFixtures.RecordingHandler<>();

        dispatcher.registerRoot(root);
        dispatcher.bind(child, handler);

        CommandDispatchResult result = dispatcher.dispatch("admin", "root", "child");

        assertEquals(CommandDispatchStatus.SUCCESS, result.getStatus());
        assertSame(child, result.getCommand().orElseThrow());
        assertEquals(1, handler.executions());
    }

}