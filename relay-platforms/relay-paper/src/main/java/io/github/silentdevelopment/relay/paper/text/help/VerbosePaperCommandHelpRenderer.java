package io.github.silentdevelopment.relay.paper.text.help;

import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import io.github.silentdevelopment.relay.text.help.CommandHelpOption;
import io.github.silentdevelopment.relay.text.help.CommandHelpRenderer;
import io.github.silentdevelopment.relay.text.help.CommandHelpUsage;
import net.kyori.adventure.text.Component;

import java.util.List;

public final class VerbosePaperCommandHelpRenderer implements CommandHelpRenderer<Component> {

    @Override
    public Component render(CommandHelpEntry entry) {
        Component component = Component.empty()
                .append(Component.text("Path: "))
                .append(Component.text(entry.path()))
                .append(Component.newline())
                .append(Component.text("Description: "))
                .append(Component.text(entry.description()))
                .append(Component.newline())
                .append(Component.text("Usages: "))
                .append(Component.text(entry.usages().stream().map(CommandHelpUsage::usage).toList().toString()))
                .append(Component.newline())
                .append(Component.text("Options:"));

        if (entry.options().isEmpty()) {
            component = component.append(Component.text(" []"));
        } else {
            for (CommandHelpOption option : entry.options()) {
                component = component.append(Component.newline());
                component = component.append(Component.text(" - "));
                component = component.append(Component.text(option.usage()));

                if (!option.aliases().isEmpty()) {
                    component = component.append(Component.text(" (aliases: " + option.aliases() + ")"));
                }

                if (!option.description().isBlank()) {
                    component = component.append(Component.text(" - " + option.description()));
                }
            }
        }

        component = component.append(Component.newline());
        component = component.append(Component.text("Subcommands: "));
        component = component.append(Component.text(entry.subcommands().toString()));
        return component;
    }

    @Override
    public Component renderAll(List<CommandHelpEntry> entries) {
        Component component = Component.empty();

        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) {
                component = component.append(Component.newline()).append(Component.newline());
            }

            component = component.append(render(entries.get(i)));
        }

        return component;
    }

}