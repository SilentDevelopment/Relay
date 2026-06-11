package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.dispatch.CommandDispatcher;

import java.util.List;
import java.util.Objects;

public interface CommandDefinition<S> {

    Command command();

    default List<? extends CommandDefinition<S>> children() {
        return List.of();
    }

    default void bind(CommandDispatcher<S> dispatcher) {
        Objects.requireNonNull(dispatcher, "dispatcher");

        bindSelf(dispatcher);

        for (CommandDefinition<S> child : children()) {
            child.bind(dispatcher);
        }
    }

    default void bindSelf(CommandDispatcher<S> dispatcher) {}

}