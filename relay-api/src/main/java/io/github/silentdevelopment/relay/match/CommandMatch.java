package io.github.silentdevelopment.relay.match;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class CommandMatch {

    private final Command command;
    private final List<Command> path;
    private final Signature signature;
    private final Map<Argument<?>, Object> values;
    private final Set<CommandOption> options;
    private final Map<ValueCommandOption<?>, Object> optionValues;

    public CommandMatch(Command command, Signature signature, Map<Argument<?>, Object> values, Set<CommandOption> options, Map<ValueCommandOption<?>, Object> optionValues) {
        this(command, List.of(command), signature, values, options, optionValues);
    }

    public CommandMatch(Command command, List<Command> path, Signature signature, Map<Argument<?>, Object> values, Set<CommandOption> options, Map<ValueCommandOption<?>, Object> optionValues) {
        this.command = Objects.requireNonNull(command, "command");
        this.signature = Objects.requireNonNull(signature, "signature");

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path cannot be null or empty.");
        }

        if (path.get(path.size() - 1) != command) {
            throw new IllegalArgumentException("path must end with the matched command.");
        }

        if (values == null) {
            throw new IllegalArgumentException("values cannot be null.");
        }

        if (options == null) {
            throw new IllegalArgumentException("options cannot be null.");
        }

        if (optionValues == null) {
            throw new IllegalArgumentException("optionValues cannot be null.");
        }

        this.path = List.copyOf(path);
        this.values = Map.copyOf(values);
        this.options = Set.copyOf(options);
        this.optionValues = Map.copyOf(optionValues);
    }

    public Command getCommand() {
        return this.command;
    }

    public List<Command> getPath() {
        return this.path;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public boolean has(Argument<?> argument) {
        return this.values.containsKey(argument);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Argument<T> argument) {
        Objects.requireNonNull(argument, "argument");

        if (!this.values.containsKey(argument)) {
            throw new IllegalArgumentException("No parsed value exists for argument: " + argument.name());
        }

        return (T) this.values.get(argument);
    }

    public boolean hasFlag(CommandFlag flag) {
        Objects.requireNonNull(flag, "flag");
        return this.options.contains(flag);
    }

    public boolean hasOption(CommandOption option) {
        Objects.requireNonNull(option, "option");
        return this.options.contains(option);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOptionValue(ValueCommandOption<T> option) {
        Objects.requireNonNull(option, "option");

        if (!this.optionValues.containsKey(option)) {
            throw new IllegalArgumentException("No parsed value exists for option: " + option.getName());
        }

        return (T) this.optionValues.get(option);
    }

    public Map<Argument<?>, Object> getValues() {
        return this.values;
    }

}