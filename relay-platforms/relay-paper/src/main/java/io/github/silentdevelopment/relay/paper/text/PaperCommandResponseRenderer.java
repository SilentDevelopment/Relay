package io.github.silentdevelopment.relay.paper.text;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.paper.text.usage.PaperUsageComponentRenderer;
import io.github.silentdevelopment.relay.paper.text.usage.StyledPaperUsageComponentRenderer;
import io.github.silentdevelopment.relay.text.CommandResponseRenderer;
import io.github.silentdevelopment.relay.text.CommandText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PaperCommandResponseRenderer implements CommandResponseRenderer<Component> {

    private final PaperUsageComponentRenderer usageRenderer;

    public PaperCommandResponseRenderer() {
        this(new StyledPaperUsageComponentRenderer(false));
    }

    public PaperCommandResponseRenderer(PaperUsageComponentRenderer usageRenderer) {
        this.usageRenderer = Objects.requireNonNull(usageRenderer, "usageRenderer");
    }

    @Override
    public Component renderUnknownCommand() {
        return Component.text("Unknown command.", NamedTextColor.RED);
    }

    @Override
    public Component renderNoHandler() {
        return Component.text("No handler is bound for this command.", NamedTextColor.RED);
    }

    @Override
    public Component renderInvalidUsage(CommandText message, List<String> usages) {
        Component component = Component.text("Invalid usage: " + renderText(message), NamedTextColor.RED);

        if (usages.isEmpty()) {
            return component;
        }

        component = component.append(Component.newline());
        component = component.append(Component.text("Valid usages:", NamedTextColor.GRAY));

        for (String usage : usages) {
            component = component.append(Component.newline());
            component = component.append(Component.text(" - " + usage, NamedTextColor.GREEN));
        }

        return component;
    }

    public Component renderInvalidUsage(CommandText message, String path, Command command) {
        Component component = Component.text("Invalid usage: " + renderText(message), NamedTextColor.RED);

        if (command.signatures().isEmpty()) {
            return component;
        }

        component = component.append(Component.newline());
        component = component.append(Component.text("Valid usages:", NamedTextColor.GRAY));

        for (Signature signature : command.signatures()) {
            component = component.append(Component.newline());
            component = component.append(Component.text(" - ", NamedTextColor.GRAY));
            component = component.append(this.usageRenderer.render(path, command, signature));
        }

        return component;
    }

    @Override
    public Component renderRequirementFailure(CommandText message) {
        return Component.text("Access denied: " + renderText(message), NamedTextColor.RED);
    }

    @Override
    public Component renderAbort(CommandText message) {
        return Component.text(renderText(message), NamedTextColor.RED);
    }

    @Override
    public Component renderSuggestions(List<String> suggestions) {
        StringJoiner joiner = new StringJoiner(", ");
        suggestions.forEach(joiner::add);
        return Component.text("Suggestions: [" + joiner + "]", NamedTextColor.GRAY);
    }

    private String renderText(CommandText text) {
        if (text.arguments().isEmpty()) {
            return text.fallback();
        }

        return MessageFormat.format(text.fallback(), text.arguments().toArray());
    }

}