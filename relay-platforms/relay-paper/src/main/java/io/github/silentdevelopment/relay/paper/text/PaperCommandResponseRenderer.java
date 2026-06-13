package io.github.silentdevelopment.relay.paper.text;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.paper.text.usage.PaperUsageComponentRenderer;
import io.github.silentdevelopment.relay.paper.text.usage.StyledPaperUsageComponentRenderer;
import io.github.silentdevelopment.relay.text.CommandResponseRenderer;
import io.github.silentdevelopment.relay.text.CommandText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PaperCommandResponseRenderer implements CommandResponseRenderer<CommandSender, Component> {

    private final PaperUsageComponentRenderer usageRenderer;

    public PaperCommandResponseRenderer() {
        this(new StyledPaperUsageComponentRenderer(false));
    }

    public PaperCommandResponseRenderer(PaperUsageComponentRenderer usageRenderer) {
        this.usageRenderer = Objects.requireNonNull(usageRenderer, "usageRenderer");
    }

    public Component renderUnknownCommand() {
        return renderUnknownCommand(null);
    }

    @Override
    public Component renderUnknownCommand(CommandSender source) {
        return Component.text("Unknown command.", NamedTextColor.RED);
    }

    public Component renderNoHandler() {
        return renderNoHandler(null);
    }

    @Override
    public Component renderNoHandler(CommandSender source) {
        return Component.text("No handler is bound for this command.", NamedTextColor.RED);
    }

    public Component renderInvalidUsage(CommandText message, List<String> usages) {
        return renderInvalidUsage(null, message, usages);
    }

    @Override
    public Component renderInvalidUsage(CommandSender source, CommandText message, List<String> usages) {
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
        return renderInvalidUsage(null, message, path, command);
    }

    @Override
    public Component renderInvalidUsage(CommandSender source, CommandText message, String path, Command command) {
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

    public Component renderRequirementFailure(CommandText message) {
        return renderRequirementFailure(null, message);
    }

    @Override
    public Component renderRequirementFailure(CommandSender source, CommandText message) {
        return Component.text("Access denied: " + renderText(message), NamedTextColor.RED);
    }

    public Component renderAbort(CommandText message) {
        return renderAbort(null, message);
    }

    @Override
    public Component renderAbort(CommandSender source, CommandText message) {
        return Component.text(renderText(message), NamedTextColor.RED);
    }

    public Component renderSuggestions(List<String> suggestions) {
        return renderSuggestions(null, suggestions);
    }

    @Override
    public Component renderSuggestions(CommandSender source, List<String> suggestions) {
        StringJoiner joiner = new StringJoiner(", ");
        suggestions.forEach(joiner::add);
        return Component.text("Suggestions: [" + joiner + "]", NamedTextColor.GRAY);
    }

    protected String renderText(CommandText text) {
        String value = text.fallback();

        for (int index = 0; index < text.arguments().size(); index++) {
            value = value.replace("{" + index + "}", String.valueOf(text.arguments().get(index)));
        }

        for (var entry : text.placeholders().entrySet()) {
            value = value.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }

        return value;
    }

}