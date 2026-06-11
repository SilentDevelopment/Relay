package io.github.silentdevelopment.relay.paper;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchStatus;
import io.github.silentdevelopment.relay.paper.support.CapturingResponseFunction;
import io.github.silentdevelopment.relay.paper.support.PaperCommandFixtures;
import io.github.silentdevelopment.relay.paper.support.PaperProxyFactory;
import io.github.silentdevelopment.relay.text.CommandText;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaperCommandManagerTest {

    @Test
    void dispatchAndRespondReturnsInvalidUsageAndSendsMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.requiredMessageCommand("echo");

        PaperCommandFixtures.register(manager, root, context -> {});

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "echo", "");

        assertEquals(CommandDispatchStatus.INVALID_USAGE, result.getStatus());
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondReturnsRequirementFailedAndSendsMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.restrictedCommand("secure");

        PaperCommandFixtures.register(manager, root, context -> {});

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "secure", "");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondReturnsAbortedAndSendsMessageWhenAbortHasMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("abort");

        PaperCommandFixtures.register(manager, root, context -> context.abort(CommandText.of("Aborted.")));

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "abort", "");

        assertEquals(CommandDispatchStatus.ABORTED, result.getStatus());
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondReturnsAbortedAndDoesNotSendMessageWhenAbortIsSilent() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("abort");

        PaperCommandFixtures.register(manager, root, context -> context.abort());

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "abort", "");

        assertEquals(CommandDispatchStatus.ABORTED, result.getStatus());
        assertEquals(0, responses.size());
    }

    @Test
    void dispatchAndRespondReturnsNoHandlerAndSendsMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("ping");

        manager.registerRoot(root);

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "ping", "");

        assertEquals(CommandDispatchStatus.NO_HANDLER, result.getStatus());
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondReturnsSuccessAndDoesNotSendMessage() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        PaperCommandManager manager = PaperCommandFixtures.manager(responses);
        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("ping");

        PaperCommandFixtures.register(manager, root, context -> {});

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "ping", "");

        assertEquals(CommandDispatchStatus.SUCCESS, result.getStatus());
        assertEquals(0, responses.size());
    }

}
