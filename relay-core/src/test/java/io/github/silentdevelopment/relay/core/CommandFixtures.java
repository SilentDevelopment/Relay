package io.github.silentdevelopment.relay.core;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.core.argument.ArgumentTypes;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.requirement.PredicateCommandRequirement;

import java.util.ArrayList;
import java.util.List;

public final class CommandFixtures {

    private CommandFixtures() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static final Argument<String> NAME = Argument.required("name", ArgumentTypes.STRING);
    public static final Argument<String> MESSAGE = Argument.greedyRequired("message", ArgumentTypes.GREEDY_STRING);

    public static Command pingCommand() {
        return CommandBuilder.<String>literal("ping")
                .description("Ping command.")
                .noArgs()
                .build();
    }

    public static Command echoCommand() {
        return CommandBuilder.<String>literal("echo")
                .description("Echo command.")
                .signature(MESSAGE)
                .build();
    }

    public static Command secureCommand() {
        return CommandBuilder.<String>literal("secure")
                .description("Restricted command.")
                .noArgs()
                .requirement(new PredicateCommandRequirement<>(source -> source.equals("admin"), "Forbidden."))
                .build();
    }

    public static Command rootWithSubcommands() {
        return CommandBuilder.<String>literal("root")
                .description("Root command.")
                .subcommands(
                        pingCommand(),
                        echoCommand(),
                        secureCommand()
                )
                .build();
    }

    public static final class RecordingHandler<S> implements io.github.silentdevelopment.relay.command.CommandHandler<S> {

        private int executions;
        private CommandContext<S> lastContext;

        @Override
        public void execute(CommandContext<S> context) {
            this.executions++;
            this.lastContext = context;
        }

        public int executions() {
            return this.executions;
        }

        public CommandContext<S> lastContext() {
            return this.lastContext;
        }

    }

    public static final class RecordingSuggestionProvider<S> implements io.github.silentdevelopment.relay.suggestion.SuggestionProvider<S> {

        private final List<String> values;

        public RecordingSuggestionProvider(List<String> values) {
            this.values = new ArrayList<>(values);
        }

        @Override
        public List<String> suggest(io.github.silentdevelopment.relay.suggestion.SuggestionContext<S> context) {
            return List.copyOf(this.values);
        }

    }

}