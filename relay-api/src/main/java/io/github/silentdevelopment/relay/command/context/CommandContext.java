package io.github.silentdevelopment.relay.command.context;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.Objects;
import java.util.function.Supplier;

public interface CommandContext<S> {

    S source();

    String label();

    String rawInput();

    Command command();

    Signature signature();

    boolean has(Argument<?> argument);

    <T> T get(Argument<T> argument);

    boolean hasFlag(CommandFlag flag);

    boolean hasOption(CommandOption option);

    <T> T getOptionValue(ValueCommandOption<T> option);

    default <T> T getOptionValueOrDefault(ValueCommandOption<T> option, T fallback) {
        return hasOption(option) ? getOptionValue(option) : fallback;
    }

    default <T> T getOptionValueOrElse(ValueCommandOption<T> option, Supplier<? extends T> fallbackSupplier) {
        Objects.requireNonNull(option, "option");
        Objects.requireNonNull(fallbackSupplier, "fallbackSupplier");
        return hasOption(option) ? getOptionValue(option) : fallbackSupplier.get();
    }

    boolean abort();

    boolean abortIf(boolean condition);

    boolean abort(CommandText message);

    boolean abortIf(boolean condition, CommandText message);

    boolean isAborted();

}