package io.github.silentdevelopment.relay.core.command.option;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;

import java.util.List;
import java.util.Objects;

public final class DefaultCommandOption<T> implements ValueCommandOption<T> {

    private final String name;
    private final List<String> aliases;
    private final String description;
    private final Argument<T> argument;

    public DefaultCommandOption(String name, String description, List<String> aliases, Argument<T> argument) {
        validateLiteral(name, "name");
        this.name = name;
        this.description = description == null ? "" : description;
        this.aliases = List.copyOf(Objects.requireNonNull(aliases, "aliases"));
        this.argument = Objects.requireNonNull(argument, "argument");
        validateAliases(this.aliases);

        if (!this.argument.required()) {
            throw new IllegalArgumentException("Option arguments must be required.");
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Argument<T> getArgument() {
        return this.argument;
    }

    private static void validateAliases(List<String> aliases) {
        for (String alias : aliases) {
            validateLiteral(alias, "alias");
        }
    }

    private static void validateLiteral(String literal, String label) {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be null or blank.");
        }

        if (literal.startsWith("-")) {
            throw new IllegalArgumentException(label + " must not include leading dashes: " + literal);
        }
    }

}