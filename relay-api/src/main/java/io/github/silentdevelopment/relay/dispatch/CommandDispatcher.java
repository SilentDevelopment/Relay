package io.github.silentdevelopment.relay.dispatch;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.CommandHandler;

public interface CommandDispatcher<S> {

    void registerRoot(Command command);

    void bind(Command command, CommandHandler<S> handler);

    CommandDispatchResult dispatch(S source, String label, String arguments);

}