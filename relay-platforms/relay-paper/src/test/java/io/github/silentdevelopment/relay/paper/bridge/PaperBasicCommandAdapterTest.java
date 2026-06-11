package io.github.silentdevelopment.relay.paper.bridge;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.adapter.PaperBasicCommandAdapter;
import io.github.silentdevelopment.relay.paper.support.CapturingResponseFunction;
import io.github.silentdevelopment.relay.paper.support.PaperCommandFixtures;
import io.github.silentdevelopment.relay.paper.support.PaperProxyFactory;
import io.github.silentdevelopment.relay.text.CommandText;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaperBasicCommandAdapterTest {

    @Test
    void executeSendsInvalidUsageMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        CommandSourceStack sourceStack = PaperProxyFactory.sourceStack(sender);
        Command root = PaperCommandFixtures.requiredMessageCommand("echo");
        PaperBasicCommandAdapter adapter = new PaperBasicCommandAdapter(root, manager);

        PaperCommandFixtures.register(manager, root, context -> {});

        adapter.execute(sourceStack, new String[0]);

        assertEquals(1, responses.size());
    }

    @Test
    void executeSendsAbortMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        CommandSourceStack sourceStack = PaperProxyFactory.sourceStack(sender);
        Command root = PaperCommandFixtures.noArgsCommand("abort");
        PaperBasicCommandAdapter adapter = new PaperBasicCommandAdapter(root, manager);

        PaperCommandFixtures.register(manager, root, context -> context.abort(CommandText.of("Aborted.")));

        adapter.execute(sourceStack, new String[0]);

        assertEquals(1, responses.size());
    }

    @Test
    void executeDoesNotSendMessageOnSuccess() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        CommandSourceStack sourceStack = PaperProxyFactory.sourceStack(sender);
        Command root = PaperCommandFixtures.noArgsCommand("ping");
        PaperBasicCommandAdapter adapter = new PaperBasicCommandAdapter(root, manager);

        PaperCommandFixtures.register(manager, root, context -> {});

        adapter.execute(sourceStack, new String[0]);

        assertEquals(0, responses.size());
    }

}
