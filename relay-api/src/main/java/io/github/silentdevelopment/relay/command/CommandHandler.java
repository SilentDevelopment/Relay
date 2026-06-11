package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.command.context.CommandContext;

public interface CommandHandler<S> {

    void execute(CommandContext<S> context);

}