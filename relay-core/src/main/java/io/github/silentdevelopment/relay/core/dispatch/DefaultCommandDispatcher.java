package io.github.silentdevelopment.relay.core.dispatch;

import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReaderFactory;
import io.github.silentdevelopment.relay.core.command.context.DefaultCommandContext;
import io.github.silentdevelopment.relay.core.match.DefaultCommandMatcher;
import io.github.silentdevelopment.relay.core.requirement.DefaultCommandAccessResolver;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReaderFactory;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.CommandHandler;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatcher;
import io.github.silentdevelopment.relay.match.CommandMatch;
import io.github.silentdevelopment.relay.match.CommandMatchResult;
import io.github.silentdevelopment.relay.match.CommandMatcher;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.requirement.RequirementResult;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.*;

public final class DefaultCommandDispatcher<S> implements CommandDispatcher<S> {

    private final CommandMatcher matcher;
    private final ArgumentReaderFactory readerFactory;
    private final CommandAccessResolver<S> accessResolver;
    private final Map<String, Command> rootCommands;
    private final Map<Command, CommandHandler<S>> handlers;

    public DefaultCommandDispatcher() {
        this(new DefaultCommandMatcher(), new DefaultArgumentReaderFactory(), new DefaultCommandAccessResolver<>());
    }

    public DefaultCommandDispatcher(CommandMatcher matcher, ArgumentReaderFactory readerFactory, CommandAccessResolver<S> accessResolver) {
        this.matcher = Objects.requireNonNull(matcher, "matcher");
        this.readerFactory = Objects.requireNonNull(readerFactory, "readerFactory");
        this.accessResolver = Objects.requireNonNull(accessResolver, "accessResolver");
        this.rootCommands = new HashMap<>();
        this.handlers = new IdentityHashMap<>();
    }

    @Override
    public void registerRoot(Command command) {
        Objects.requireNonNull(command, "command");
        registerLiteral(command.name(), command);

        for (String alias : command.aliases()) {
            registerLiteral(alias, command);
        }
    }

    @Override
    public void bind(Command command, CommandHandler<S> handler) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(handler, "handler");
        this.handlers.put(command, handler);
    }

    @Override
    public CommandDispatchResult dispatch(S source, String label, String arguments) {
        Objects.requireNonNull(source, "source");

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be null or blank.");
        }

        String key = normalize(label);
        Command root = this.rootCommands.get(key);

        if (root == null) {
            return CommandDispatchResult.unknownCommand();
        }

        final ArgumentReader reader;

        try {
            reader = this.readerFactory.create(arguments);
        } catch (IllegalArgumentException ex) {
            return CommandDispatchResult.invalidUsage(root, null, CommandText.of("invalid_usage", ex.getMessage()));
        }

        CommandMatchResult result = this.matcher.match(root, reader);

        if (result.isFailure()) {
            return CommandDispatchResult.invalidUsage(
                    result.getCommand().orElse(root),
                    result.getSignature().orElse(null),
                    CommandText.of("invalid_usage", result.getError().orElse("Invalid usage."))
            );
        }

        CommandMatch match = result.getMatch();
        Command command = match.getCommand();
        RequirementResult accessResult = this.accessResolver.evaluate(source, command);

        if (accessResult.isDenied()) {
            return CommandDispatchResult.requirementFailed(
                    command,
                    match.getSignature(),
                    CommandText.of("requirement_failed", accessResult.getMessage().orElse("You cannot use this command."))
            );
        }

        CommandHandler<S> handler = this.handlers.get(command);

        if (handler == null) {
            return CommandDispatchResult.noHandler(command, match.getSignature());
        }

        DefaultCommandContext<S> context = new DefaultCommandContext<>(source, label, arguments, match);
        handler.execute(context);

        if (context.isAborted()) {
            if (context.abortMessage().isPresent()) {
                return CommandDispatchResult.aborted(command, match.getSignature(), context.abortMessage().orElseThrow());
            }

            return CommandDispatchResult.aborted(command, match.getSignature());
        }

        return CommandDispatchResult.success(command, match.getSignature());
    }

    private void registerLiteral(String literal, Command command) {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException("Command literal cannot be null or blank.");
        }

        String key = normalize(literal);
        Command existing = this.rootCommands.putIfAbsent(key, command);

        if (existing == null || existing == command) {
            return;
        }

        throw new IllegalStateException("Duplicate root command literal: " + literal);
    }

    private String normalize(String input) {
        return input.toLowerCase(Locale.ROOT);
    }

}