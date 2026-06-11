package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.requirement.CommandRequirement;

import java.util.List;

public interface RestrictedCommand<S> extends Command {

    List<CommandRequirement<S>> getRequirements();

}