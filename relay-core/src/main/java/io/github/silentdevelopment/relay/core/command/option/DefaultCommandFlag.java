package io.github.silentdevelopment.relay.core.command.option;

import io.github.silentdevelopment.relay.command.option.CommandFlag;

import java.util.List;
import java.util.Objects;

public final class DefaultCommandFlag implements CommandFlag {

    private final String name;
    private final List<String> aliases;
    private final String description;

    public DefaultCommandFlag(String name, String description, List<String> aliases) {
        validateLiteral(name, "name");
        this.name = name;
        this.description = description == null ? "" : description;
        this.aliases = List.copyOf(Objects.requireNonNull(aliases, "aliases"));
        validateAliases(this.aliases);
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
