package io.github.silentdevelopment.relay.core.command.context;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.match.CommandMatch;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.Objects;
import java.util.Optional;

public final class DefaultCommandContext<S> implements CommandContext<S> {

    private final S source;
    private final String label;
    private final String rawInput;
    private final CommandMatch match;
    private final ExecutionState executionState;

    public DefaultCommandContext(S source, String label, String rawInput, CommandMatch match) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be null or blank.");
        }

        this.source = Objects.requireNonNull(source, "source");
        this.rawInput = rawInput == null ? "" : rawInput;
        this.match = Objects.requireNonNull(match, "match");
        this.label = label;
        this.executionState = new ExecutionState();
    }

    @Override
    public S source() {
        return this.source;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public String rawInput() {
        return this.rawInput;
    }

    @Override
    public Command command() {
        return this.match.getCommand();
    }

    @Override
    public Signature signature() {
        return this.match.getSignature();
    }

    @Override
    public boolean has(Argument<?> argument) {
        return this.match.has(argument);
    }

    @Override
    public <T> T get(Argument<T> argument) {
        return this.match.get(argument);
    }

    @Override
    public boolean hasFlag(CommandFlag flag) {
        return this.match.hasFlag(flag);
    }

    @Override
    public boolean hasOption(CommandOption option) {
        return this.match.hasOption(option);
    }

    @Override
    public <T> T getOptionValue(ValueCommandOption<T> option) {
        return this.match.getOptionValue(option);
    }

    @Override
    public boolean abort() {
        return this.executionState.abort();
    }

    @Override
    public boolean abortIf(boolean condition) {
        return this.executionState.abortIf(condition);
    }

    @Override
    public boolean abort(CommandText message) {
        return this.executionState.abort(message);
    }

    @Override
    public boolean abortIf(boolean condition, CommandText message) {
        return this.executionState.abortIf(condition, message);
    }

    @Override
    public boolean isAborted() {
        return this.executionState.isAborted();
    }

    public Optional<CommandText> abortMessage() {
        return this.executionState.abortMessage();
    }

    private static final class ExecutionState {

        private boolean aborted;
        private CommandText abortMessage;

        private boolean abort() {
            this.aborted = true;
            return true;
        }

        private boolean abortIf(boolean condition) {
            if (!condition) {
                return false;
            }

            return abort();
        }

        private boolean abort(CommandText message) {
            this.aborted = true;

            if (this.abortMessage == null) {
                this.abortMessage = Objects.requireNonNull(message, "message");
            }

            return true;
        }

        private boolean abortIf(boolean condition, CommandText message) {
            if (!condition) {
                return false;
            }

            return abort(message);
        }

        private boolean isAborted() {
            return this.aborted;
        }

        private Optional<CommandText> abortMessage() {
            return Optional.ofNullable(this.abortMessage);
        }

    }

}