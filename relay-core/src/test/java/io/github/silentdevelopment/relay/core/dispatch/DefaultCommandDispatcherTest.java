package io.github.silentdevelopment.relay.core.dispatch;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.CommandFixtures;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchStatus;
import org.junit.jupiter.api.Test;

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

}