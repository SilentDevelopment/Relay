package io.github.silentdevelopment.relay.requirement;

import io.github.silentdevelopment.relay.command.Command;

public interface CommandAccessResolver<S> {

    RequirementResult evaluate(S source, Command command);

    default boolean canAccess(S source, Command command) {
        return evaluate(source, command).isAllowed();
    }

}