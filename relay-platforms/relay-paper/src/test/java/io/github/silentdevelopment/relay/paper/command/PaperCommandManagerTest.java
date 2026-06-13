package io.github.silentdevelopment.relay.paper.command;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchStatus;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.argument.PaperArgumentTypeRegistry;
import io.github.silentdevelopment.relay.paper.support.CapturingResponseFunction;
import io.github.silentdevelopment.relay.paper.support.PaperCommandFixtures;
import io.github.silentdevelopment.relay.paper.support.PaperProxyFactory;
import io.github.silentdevelopment.relay.paper.text.PaperCommandResponseRenderer;
import io.github.silentdevelopment.relay.text.CommandText;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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

    @Test
    void dispatchAndRespondUsesSourceAwareRequirementRenderer() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        RecordingRenderer renderer = new RecordingRenderer();

        PaperCommandManager manager = new PaperCommandManager(builder -> builder
                .argumentTypeRegistry(new PaperArgumentTypeRegistry())
                .responseRenderer(renderer)
                .responseFunction(responses)
        );

        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.restrictedCommand("secure");

        PaperCommandFixtures.register(manager, root, context -> {});

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "secure", "");

        assertEquals(CommandDispatchStatus.REQUIREMENT_FAILED, result.getStatus());
        assertSame(sender, renderer.requirementSource);
        assertNotNull(renderer.requirementMessage);
        assertEquals("Forbidden.", renderer.requirementMessage.fallback());
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondUsesSourceAwareInvalidUsageRenderer() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        RecordingRenderer renderer = new RecordingRenderer();

        PaperCommandManager manager = new PaperCommandManager(builder -> builder
                .argumentTypeRegistry(new PaperArgumentTypeRegistry())
                .responseRenderer(renderer)
                .responseFunction(responses)
        );

        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.requiredMessageCommand("echo");

        PaperCommandFixtures.register(manager, root, context -> {});

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "echo", "");

        assertEquals(CommandDispatchStatus.INVALID_USAGE, result.getStatus());
        assertSame(sender, renderer.invalidUsageSource);
        assertNotNull(renderer.invalidUsageMessage);
        assertEquals(1, responses.size());
    }

    @Test
    void dispatchAndRespondUsesSourceAwareAbortRenderer() {
        CapturingResponseFunction responses = new CapturingResponseFunction();
        RecordingRenderer renderer = new RecordingRenderer();

        PaperCommandManager manager = new PaperCommandManager(builder -> builder
                .argumentTypeRegistry(new PaperArgumentTypeRegistry())
                .responseRenderer(renderer)
                .responseFunction(responses)
        );

        CommandSender sender = PaperProxyFactory.sender("Tester");
        Command root = PaperCommandFixtures.noArgsCommand("abort");

        PaperCommandFixtures.register(manager, root, context -> context.abort(CommandText.of("relay.test.abort", "Abort message.")));

        CommandDispatchResult result = manager.dispatchAndRespond(sender, root, "abort", "");

        assertEquals(CommandDispatchStatus.ABORTED, result.getStatus());
        assertSame(sender, renderer.abortSource);
        assertNotNull(renderer.abortMessage);
        assertEquals("Abort message.", renderer.abortMessage.fallback());
        assertEquals(1, responses.size());
    }

    @Test
    void renderSuggestionsUsesSourceAwareRenderer() {
        RecordingRenderer renderer = new RecordingRenderer();

        PaperCommandManager manager = new PaperCommandManager(builder -> builder
                .argumentTypeRegistry(new PaperArgumentTypeRegistry())
                .responseRenderer(renderer)
        );

        CommandSender sender = PaperProxyFactory.sender("Tester");

        Component rendered = manager.renderSuggestions(sender, List.of("one", "two"));

        assertSame(sender, renderer.suggestionSource);
        assertEquals(List.of("one", "two"), renderer.suggestions);
        assertNotNull(rendered);
    }

    private static final class RecordingRenderer extends PaperCommandResponseRenderer {

        private CommandSender requirementSource;
        private CommandText requirementMessage;

        private CommandSender invalidUsageSource;
        private CommandText invalidUsageMessage;

        private CommandSender abortSource;
        private CommandText abortMessage;

        private CommandSender suggestionSource;
        private List<String> suggestions;

        @Override
        public Component renderRequirementFailure(CommandSender source, CommandText message) {
            this.requirementSource = source;
            this.requirementMessage = message;
            return Component.text("requirement");
        }

        @Override
        public Component renderInvalidUsage(CommandSender source, CommandText message, String path, Command command) {
            this.invalidUsageSource = source;
            this.invalidUsageMessage = message;
            return Component.text("invalid");
        }

        @Override
        public Component renderAbort(CommandSender source, CommandText message) {
            this.abortSource = source;
            this.abortMessage = message;
            return Component.text("abort");
        }

        @Override
        public Component renderSuggestions(CommandSender source, List<String> suggestions) {
            this.suggestionSource = source;
            this.suggestions = List.copyOf(suggestions);
            return Component.text("suggestions");
        }
    }

}