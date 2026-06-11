package io.github.silentdevelopment.relay.text.help;

import java.util.List;
import java.util.Objects;

public record CommandHelpOption(String literal, List<String> aliases, String description, String valueUsage) {

    public CommandHelpOption {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException("literal cannot be null or blank.");
        }

        aliases = List.copyOf(Objects.requireNonNull(aliases, "aliases"));

        for (String alias : aliases) {
            if (alias == null || alias.isBlank()) {
                throw new IllegalArgumentException("aliases cannot contain null or blank values.");
            }
        }

        description = description == null ? "" : description;
        valueUsage = valueUsage == null ? "" : valueUsage;
    }

    public boolean takesValue() {
        return !this.valueUsage.isBlank();
    }

    public String usage() {
        if (!takesValue()) {
            return "[" + this.literal + "]";
        }

        return "[" + this.literal + " " + this.valueUsage + "]";
    }

}