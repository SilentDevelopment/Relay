package io.github.silentdevelopment.relay.paper;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReaderFactory;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReaderFactory;
import io.github.silentdevelopment.relay.core.match.DefaultCommandMatcher;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchStatus;
import io.github.silentdevelopment.relay.match.CommandMatcher;
import io.github.silentdevelopment.relay.paper.argument.PaperArgumentTypeRegistry;
import io.github.silentdevelopment.relay.paper.adapter.PaperResponseFunction;
import io.github.silentdevelopment.relay.paper.dispatch.PaperCommandDispatcher;
import io.github.silentdevelopment.relay.paper.command.PaperCommandHandler;
import io.github.silentdevelopment.relay.paper.text.help.PaperCommandHelpProvider;
import io.github.silentdevelopment.relay.paper.requirement.PaperCommandAccessResolver;
import io.github.silentdevelopment.relay.paper.suggestion.PaperCommandSuggester;
import io.github.silentdevelopment.relay.paper.text.PaperCommandResponseRenderer;
import io.github.silentdevelopment.relay.paper.text.help.PaperCommandHelpRenderer;
import io.github.silentdevelopment.relay.paper.text.usage.StyledPaperUsageComponentRenderer;
import io.github.silentdevelopment.relay.text.CommandTexts;
import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class PaperCommandManager {

    private final PaperCommandDispatcher dispatcher;
    private final PaperArgumentTypeRegistry argumentTypeRegistry;
    private final PaperCommandAccessResolver accessResolver;
    private final PaperCommandHelpProvider helpProvider;
    private final PaperCommandSuggester suggester;
    private final PaperCommandHelpRenderer helpRenderer;
    private final PaperCommandResponseRenderer responseRenderer;
    private final PaperResponseFunction responseFunction;

    public PaperCommandManager() {
        this(builder -> {});
    }

    public PaperCommandManager(Consumer<Builder> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("consumer cannot be null.");
        }

        Builder builder = new Builder();
        consumer.accept(builder);

        PaperCommandAccessResolver resolvedAccessResolver = builder.accessResolverOverride != null ? builder.accessResolverOverride : new PaperCommandAccessResolver();
        PaperArgumentTypeRegistry resolvedArgumentTypeRegistry = builder.argumentTypeRegistryOverride != null ? builder.argumentTypeRegistryOverride : PaperArgumentTypeRegistry.createDefault();

        this.accessResolver = resolvedAccessResolver;
        this.argumentTypeRegistry = resolvedArgumentTypeRegistry;
        this.dispatcher = builder.dispatcherOverride != null ? builder.dispatcherOverride : new PaperCommandDispatcher(builder.matcher, builder.readerFactory, resolvedAccessResolver);
        this.helpProvider = builder.helpProviderOverride != null ? builder.helpProviderOverride : new PaperCommandHelpProvider(resolvedAccessResolver);
        this.suggester = builder.suggesterOverride != null ? builder.suggesterOverride : new PaperCommandSuggester(resolvedAccessResolver);
        this.helpRenderer = builder.helpRenderer;
        this.responseRenderer = builder.responseRenderer;
        this.responseFunction = builder.responseFunction;
    }

    public void registerRoot(Command command) {
        this.dispatcher.registerRoot(command);
    }

    public void bind(Command command, PaperCommandHandler handler) {
        this.dispatcher.bind(command, handler);
    }

    public CommandDispatchResult dispatch(CommandSender source, String label, String arguments) {
        return this.dispatcher.dispatch(source, label, arguments);
    }

    public List<String> suggest(CommandSender source, Command command, String input) {
        return this.suggester.suggest(source, command, input);
    }

    public List<CommandHelpEntry> buildHelp(CommandSender source, Command command) {
        return this.helpProvider.build(source, command);
    }

    public Component renderHelp(CommandSender source, Command command) {
        return this.helpRenderer.renderAll(buildHelp(source, command));
    }

    public Component renderSuggestions(List<String> suggestions) {
        return this.responseRenderer.renderSuggestions(suggestions);
    }

    public Component renderSuggestions(CommandSender source, List<String> suggestions) {
        return this.responseRenderer.renderSuggestions(source, suggestions);
    }

    public CommandDispatchResult dispatchAndRespond(CommandSender source, Command root, String label, String arguments) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(root, "root");

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be null or blank.");
        }

        if (arguments == null) {
            throw new IllegalArgumentException("arguments cannot be null.");
        }

        CommandDispatchResult result = this.dispatch(source, label, arguments);
        this.renderDispatchMessage(source, root, result).ifPresent(component -> this.responseFunction.send(source, component));
        return result;
    }

    public Optional<Component> renderDispatchMessage(CommandSender source, Command root, CommandDispatchResult result) {
        Objects.requireNonNull(source, "source");

        if (result.getStatus() == CommandDispatchStatus.SUCCESS) {
            return Optional.empty();
        }

        if (result.getStatus() == CommandDispatchStatus.UNKNOWN_COMMAND) {
            return Optional.of(this.responseRenderer.renderUnknownCommand(source));
        }

        if (result.getStatus() == CommandDispatchStatus.NO_HANDLER) {
            return Optional.of(this.responseRenderer.renderNoHandler(source));
        }

        if (result.getStatus() == CommandDispatchStatus.REQUIREMENT_FAILED) {
            return Optional.of(this.responseRenderer.renderRequirementFailure(
                    source,
                    result.getMessage().orElse(CommandTexts.requirementFailed("You cannot use this command."))
            ));
        }

        if (result.getStatus() == CommandDispatchStatus.ABORTED) {
            if (result.getMessage().isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(this.responseRenderer.renderAbort(
                    source,
                    result.getMessage().orElse(CommandTexts.aborted("You cannot use this command."))
            ));
        }

        if (result.getStatus() == CommandDispatchStatus.INVALID_USAGE) {
            Command target = result.getCommand().orElse(root);
            String path = findPath(root, target, "/" + root.name());

            if (path == null) {
                path = "/" + root.name();
            }

            return Optional.of(this.responseRenderer.renderInvalidUsage(
                    source,
                    result.getMessage().orElse(CommandTexts.invalidUsage("Invalid usage.")),
                    path,
                    target
            ));
        }

        return Optional.empty();
    }

    public PaperCommandDispatcher dispatcher() {
        return this.dispatcher;
    }

    public PaperArgumentTypeRegistry argumentRegistry() {
        return argumentTypeRegistry;
    }

    public PaperCommandAccessResolver accessResolver() {
        return this.accessResolver;
    }

    public PaperCommandHelpProvider helpProvider() {
        return this.helpProvider;
    }

    public PaperCommandSuggester suggester() {
        return this.suggester;
    }

    public PaperCommandHelpRenderer helpRenderer() {
        return this.helpRenderer;
    }

    public PaperCommandResponseRenderer responseRenderer() {
        return this.responseRenderer;
    }

    public PaperResponseFunction responseFunction() {
        return this.responseFunction;
    }

    private String findPath(Command current, Command target, String path) {
        if (current == target) {
            return path;
        }

        for (Command subcommand : current.subCommands()) {
            String resolved = findPath(subcommand, target, path + " " + subcommand.name());

            if (resolved != null) {
                return resolved;
            }
        }

        return null;
    }

    public static final class Builder {

        private CommandMatcher matcher = new DefaultCommandMatcher();
        private ArgumentReaderFactory readerFactory = new DefaultArgumentReaderFactory();

        private PaperCommandDispatcher dispatcherOverride;
        private PaperArgumentTypeRegistry argumentTypeRegistryOverride;
        private PaperCommandAccessResolver accessResolverOverride;
        private PaperCommandHelpProvider helpProviderOverride;
        private PaperCommandSuggester suggesterOverride;

        private PaperCommandHelpRenderer helpRenderer = new PaperCommandHelpRenderer();
        private PaperCommandResponseRenderer responseRenderer = new PaperCommandResponseRenderer(new StyledPaperUsageComponentRenderer(false));
        private PaperResponseFunction responseFunction = Audience::sendMessage;

        public Builder matcher(CommandMatcher matcher) {
            this.matcher = Objects.requireNonNull(matcher, "matcher");
            return this;
        }

        public Builder readerFactory(ArgumentReaderFactory readerFactory) {
            this.readerFactory = Objects.requireNonNull(readerFactory, "readerFactory");
            return this;
        }

        public Builder dispatcher(PaperCommandDispatcher dispatcher) {
            this.dispatcherOverride = Objects.requireNonNull(dispatcher, "dispatcher");
            return this;
        }

        public Builder argumentTypeRegistry(PaperArgumentTypeRegistry argumentTypeRegistry) {
            this.argumentTypeRegistryOverride = argumentTypeRegistry;
            return this;
        }

        public Builder accessResolver(PaperCommandAccessResolver accessResolver) {
            this.accessResolverOverride = Objects.requireNonNull(accessResolver, "accessResolver");
            return this;
        }

        public Builder helpProvider(PaperCommandHelpProvider helpProvider) {
            this.helpProviderOverride = Objects.requireNonNull(helpProvider, "helpProvider");
            return this;
        }

        public Builder suggester(PaperCommandSuggester suggester) {
            this.suggesterOverride = Objects.requireNonNull(suggester, "suggester");
            return this;
        }

        public Builder helpRenderer(PaperCommandHelpRenderer helpRenderer) {
            this.helpRenderer = Objects.requireNonNull(helpRenderer, "helpRenderer");
            return this;
        }

        public Builder responseRenderer(PaperCommandResponseRenderer responseRenderer) {
            this.responseRenderer = Objects.requireNonNull(responseRenderer, "responseRenderer");
            return this;
        }

        public Builder responseFunction(PaperResponseFunction responseFunction) {
            this.responseFunction = Objects.requireNonNull(responseFunction, "responseFunction");
            return this;
        }

    }

}