package io.github.silentdevelopment.relay.core.command;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.dispatch.CommandDispatcher;

public abstract class AbstractCommandDefinition<S> implements CommandDefinition<S> {

    private Command command;

    @Override
    public final Command command() {
        if (this.command == null) {
            this.command = buildCommand();
        }

        return this.command;
    }

    @Override
    public final void bind(CommandDispatcher<S> dispatcher) {
        dispatcher.bind(command(), this::handle);
    }

    protected abstract void handle(CommandContext<S> context);

    protected abstract Command buildCommand();

}