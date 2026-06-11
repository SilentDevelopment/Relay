package io.github.silentdevelopment.relay.paper.dispatch;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReaderFactory;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.CommandHandler;
import io.github.silentdevelopment.relay.core.dispatch.DefaultCommandDispatcher;
import io.github.silentdevelopment.relay.dispatch.CommandDispatchResult;
import io.github.silentdevelopment.relay.dispatch.CommandDispatcher;
import io.github.silentdevelopment.relay.match.CommandMatcher;
import io.github.silentdevelopment.relay.paper.command.PaperCommandHandler;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public final class PaperCommandDispatcher implements CommandDispatcher<CommandSender> {

    private final DefaultCommandDispatcher<CommandSender> delegate;

    public PaperCommandDispatcher() {
        this.delegate = new DefaultCommandDispatcher<>();
    }

    public PaperCommandDispatcher(CommandMatcher matcher, ArgumentReaderFactory readerFactory, CommandAccessResolver<CommandSender> accessResolver) {
        this.delegate = new DefaultCommandDispatcher<>(matcher, readerFactory, accessResolver);
    }

    public PaperCommandDispatcher(DefaultCommandDispatcher<CommandSender> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public void registerRoot(Command command) {
        this.delegate.registerRoot(command);
    }

    @Override
    public void bind(Command command, CommandHandler<CommandSender> handler) {
        this.delegate.bind(command, handler);
    }

    public void bind(Command command, PaperCommandHandler handler) {
        this.delegate.bind(command, handler);
    }


    @Override
    public CommandDispatchResult dispatch(CommandSender source, String label, String arguments) {
        return this.delegate.dispatch(source, label, arguments);
    }

    public DefaultCommandDispatcher<CommandSender> unwrap() {
        return this.delegate;
    }

}