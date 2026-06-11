package io.github.silentdevelopment.relay.paper.command.context;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.text.CommandText;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public final class DefaultPaperCommandContext implements PaperCommandContext {

    private final CommandContext<CommandSender> delegate;

    public DefaultPaperCommandContext(CommandContext<CommandSender> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public CommandSender source() {
        return this.delegate.source();
    }

    @Override
    public String label() {
        return this.delegate.label();
    }

    @Override
    public String rawInput() {
        return this.delegate.rawInput();
    }

    @Override
    public Command command() {
        return this.delegate.command();
    }

    @Override
    public Signature signature() {
        return this.delegate.signature();
    }

    @Override
    public boolean has(Argument<?> argument) {
        return this.delegate.has(argument);
    }

    @Override
    public <T> T get(Argument<T> argument) {
        return this.delegate.get(argument);
    }

    @Override
    public boolean hasFlag(CommandFlag flag) {
        return this.delegate.hasFlag(flag);
    }

    @Override
    public boolean hasOption(CommandOption option) {
        return this.delegate.hasOption(option);
    }

    @Override
    public <T> T getOptionValue(ValueCommandOption<T> option) {
        return this.delegate.getOptionValue(option);
    }

    @Override
    public boolean abort() {
        return this.delegate.abort();
    }

    @Override
    public boolean abort(CommandText message) {
        return this.delegate.abort(message);
    }

    @Override
    public boolean abortIf(boolean condition, CommandText message) {
        return this.delegate.abortIf(condition, message);
    }

    @Override
    public boolean abortIf(boolean condition) {
        return this.delegate.abortIf(condition);
    }

    @Override
    public boolean isAborted() {
        return this.delegate.isAborted();
    }

}