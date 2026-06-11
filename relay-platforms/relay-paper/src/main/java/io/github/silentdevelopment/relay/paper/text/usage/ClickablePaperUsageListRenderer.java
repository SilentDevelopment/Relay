package io.github.silentdevelopment.relay.paper.text.usage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.StringJoiner;

public final class ClickablePaperUsageListRenderer implements PaperUsageListRenderer {

    @Override
    public Component render(List<String> usages) {
        if (usages.isEmpty()) {
            return Component.empty();
        }

        Component component = Component.text("Valid usages:", NamedTextColor.GRAY);

        for (String usage : usages) {
            String suggested = extractStaticCommand(usage);

            component = component.append(Component.newline());
            component = component.append(Component.text(" - ", NamedTextColor.GRAY));
            component = component.append(Component.text(usage, NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.suggestCommand(suggested))
                    .hoverEvent(HoverEvent.showText(Component.text("Click to paste: " + suggested, NamedTextColor.YELLOW))));
        }

        return component;
    }

    private String extractStaticCommand(String usage) {
        if (usage == null || usage.isBlank()) {
            return "";
        }

        String[] parts = usage.trim().split("\\s+");
        StringJoiner joiner = new StringJoiner(" ");

        for (String part : parts) {
            if (part.startsWith("<") || part.startsWith("[")) {
                break;
            }

            joiner.add(part);
        }

        String result = joiner.toString();

        if (result.isEmpty()) {
            return "";
        }

        return result + " ";
    }

}