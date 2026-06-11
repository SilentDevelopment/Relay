package io.github.silentdevelopment.relay.paper.bridge;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.adapter.PaperCommandExecutor;
import io.github.silentdevelopment.relay.paper.support.CapturingResponseFunction;
import io.github.silentdevelopment.relay.paper.support.PaperCommandFixtures;
import io.github.silentdevelopment.relay.paper.support.PaperProxyFactory;
import io.github.silentdevelopment.relay.paper.support.StubBukkitCommand;
import io.github.silentdevelopment.relay.text.CommandText;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperCommandExecutorTest {

    @Test
    void onCommandSendsInvalidUsageMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.requiredMessageCommand("echo");
        PaperCommandExecutor executor = new PaperCommandExecutor(root, manager);

        PaperCommandFixtures.register(manager, root, context -> {});

        boolean handled = executor.onCommand(sender, new StubBukkitCommand("echo"), "echo", new String[0]);

        assertTrue(handled);
        assertEquals(1, responses.size());
    }

    @Test
    void onCommandSendsAbortMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("abort");
        PaperCommandExecutor executor = new PaperCommandExecutor(root, manager);

        PaperCommandFixtures.register(manager, root, context -> context.abort(CommandText.of("Aborted.")));

        boolean handled = executor.onCommand(sender, new StubBukkitCommand("abort"), "abort", new String[0]);

        assertTrue(handled);
        assertEquals(1, responses.size());
    }

    @Test
    void onCommandDoesNotSendMessageOnSuccess() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("ping");
        PaperCommandExecutor executor = new PaperCommandExecutor(root, manager);

        PaperCommandFixtures.register(manager, root, context -> {});

        boolean handled = executor.onCommand(sender, new StubBukkitCommand("ping"), "ping", new String[0]);

        assertTrue(handled);
        assertEquals(0, responses.size());
    }

}
