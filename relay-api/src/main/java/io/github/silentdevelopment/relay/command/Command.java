package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.command.option.CommandOption;

import java.util.List;

public interface Command {

    String name();

    List<String> aliases();

    default boolean suggestAliases() {
        return false;
    }

    String description();

    List<Command> subCommands();

    List<Signature> signatures();

    List<CommandOption> options();

    default String shortFlagPrefix() {
        return "-";
    }

    default String longFlagPrefix() {
        return "--";
    }

    default String flagLiteral(String literal) {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException("literal cannot be null or blank.");
        }

        return literal.length() == 1 ? shortFlagPrefix() + literal : longFlagPrefix() + literal;
    }

}
