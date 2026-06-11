package io.github.silentdevelopment.relay.paper.text.usage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public final class PlainPaperUsageListRenderer implements PaperUsageListRenderer {

    @Override
    public Component render(List<String> usages) {
        if (usages.isEmpty()) {
            return Component.empty();
        }

        Component component = Component.text("Valid usages:", NamedTextColor.GRAY);

        for (String usage : usages) {
            component = component.append(Component.newline());
            component = component.append(Component.text(" - ", NamedTextColor.GRAY));
            component = component.append(Component.text(usage, NamedTextColor.GREEN));
        }

        return component;
    }

}