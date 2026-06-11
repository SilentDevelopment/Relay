package io.github.silentdevelopment.relay.core.command;

import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.dispatch.CommandDispatcher;

import java.util.List;
import java.util.Objects;

public abstract class AbstractCommandGroupDefinition<S> implements CommandDefinition<S> {

    private Command command;

    @Override
    public final Command command() {
        if (this.command == null) {
            this.command = buildCommand().subcommands(children().stream().map(CommandDefinition::command).toList()).build();
        }

        return this.command;
    }

    @Override
    public final void bind(CommandDispatcher<S> dispatcher) {
        Objects.requireNonNull(dispatcher, "dispatcher");

        dispatcher.bind(command(), this::handle);

        for (CommandDefinition<S> child : children()) {
            child.bind(dispatcher);
        }
    }

    @Override
    public List<? extends CommandDefinition<S>> children() {
        return List.of();
    }

    protected void handle(CommandContext<S> context) {}

    protected abstract CommandBuilder<S> buildCommand();

}