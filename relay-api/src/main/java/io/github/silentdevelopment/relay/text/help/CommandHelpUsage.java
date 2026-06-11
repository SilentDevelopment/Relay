package io.github.silentdevelopment.relay.text.help;

public record CommandHelpUsage(String usage, String paste, String description) {

    public CommandHelpUsage {
        if (usage == null || usage.isBlank()) {
            throw new IllegalArgumentException("usage cannot be null or blank.");
        }

        if (paste == null || paste.isBlank()) {
            throw new IllegalArgumentException("paste cannot be null or blank.");
        }

        description = description == null ? "" : description;
    }

}