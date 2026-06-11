package io.github.silentdevelopment.relay.core.requirement;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.RestrictedCommand;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.requirement.RequirementResult;

public final class DefaultCommandAccessResolver<S> implements CommandAccessResolver<S> {

    @Override
    public RequirementResult evaluate(S source, Command command) {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null.");
        }

        if (command == null) {
            throw new IllegalArgumentException("command cannot be null.");
        }

        if (!(command instanceof RestrictedCommand<?> restrictedCommand)) {
            return RequirementResult.allow();
        }

        @SuppressWarnings("unchecked")
        RestrictedCommand<S> typedCommand = (RestrictedCommand<S>) restrictedCommand;

        for (CommandRequirement<S> requirement : typedCommand.getRequirements()) {
            RequirementResult result = requirement.test(source);

            if (result.isDenied()) {
                return result;
            }
        }

        return RequirementResult.allow();
    }

}