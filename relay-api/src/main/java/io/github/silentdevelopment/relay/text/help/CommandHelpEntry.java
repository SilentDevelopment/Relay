package io.github.silentdevelopment.relay.text.help;

import java.util.List;
import java.util.Objects;

public record CommandHelpEntry(String path, String description, List<CommandHelpUsage> usages, List<String> subcommands, List<CommandHelpOption> options) {

    public CommandHelpEntry {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path cannot be null or blank.");
        }

        description = description == null ? "" : description;
        usages = List.copyOf(Objects.requireNonNull(usages, "usages"));
        subcommands = List.copyOf(Objects.requireNonNull(subcommands, "subcommands"));
        options = List.copyOf(Objects.requireNonNull(options, "options"));
    }

}