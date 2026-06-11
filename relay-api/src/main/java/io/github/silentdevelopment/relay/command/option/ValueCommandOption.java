package io.github.silentdevelopment.relay.command.option;

import io.github.silentdevelopment.relay.argument.Argument;

public interface ValueCommandOption<T> extends CommandOption {

    Argument<T> getArgument();

}