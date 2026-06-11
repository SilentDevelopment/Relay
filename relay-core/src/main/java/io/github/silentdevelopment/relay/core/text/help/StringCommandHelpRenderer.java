package io.github.silentdevelopment.relay.core.text.help;

import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import io.github.silentdevelopment.relay.text.help.CommandHelpOption;
import io.github.silentdevelopment.relay.text.help.CommandHelpRenderer;
import io.github.silentdevelopment.relay.text.help.CommandHelpUsage;

import java.util.List;
import java.util.StringJoiner;

public final class StringCommandHelpRenderer implements CommandHelpRenderer<String> {

    @Override
    public String render(CommandHelpEntry entry) {
        StringBuilder builder = new StringBuilder();
        builder.append("Path: ").append(entry.path()).append('\n');
        builder.append("Description: ").append(entry.description()).append('\n');
        builder.append("Usages: ").append(entry.usages().stream().map(CommandHelpUsage::usage).toList()).append('\n');
        builder.append("Options:");

        if (entry.options().isEmpty()) {
            builder.append(" []");
        } else {
            for (CommandHelpOption option : entry.options()) {
                builder.append('\n').append(" - ").append(option.usage());

                if (!option.aliases().isEmpty()) {
                    builder.append(" (aliases: ").append(option.aliases()).append(')');
                }

                if (!option.description().isBlank()) {
                    builder.append(" - ").append(option.description());
                }
            }
        }

        builder.append('\n');
        builder.append("Subcommands: ").append(entry.subcommands());
        return builder.toString();
    }

    @Override
    public String renderAll(List<CommandHelpEntry> entries) {
        StringJoiner joiner = new StringJoiner("\n\n");

        for (CommandHelpEntry entry : entries) {
            joiner.add(render(entry));
        }

        return joiner.toString();
    }

}