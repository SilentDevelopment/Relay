package io.github.silentdevelopment.relay.paper.text.help;

import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import io.github.silentdevelopment.relay.text.help.CommandHelpRenderer;
import io.github.silentdevelopment.relay.text.help.CommandHelpUsage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public final class PaperCommandHelpRenderer implements CommandHelpRenderer<Component> {

    @Override
    public Component render(CommandHelpEntry entry) {
        Component component = Component.text(entry.path(), NamedTextColor.GOLD);

        if (!entry.description().isBlank()) {
            component = component.append(Component.newline());
            component = component.append(Component.text(entry.description(), NamedTextColor.GRAY));
        }

        for (CommandHelpUsage usage : entry.usages()) {
            component = component.append(Component.newline());
            component = component.append(Component.text(" - ", NamedTextColor.DARK_GRAY));
            component = component.append(renderUsage(usage));
        }

        return component;
    }

    @Override
    public Component renderAll(List<CommandHelpEntry> entries) {
        if (entries.isEmpty()) {
            return Component.empty();
        }

        CommandHelpEntry root = entries.getFirst();
        Component component = Component.text(root.path(), NamedTextColor.GOLD).decorate(TextDecoration.BOLD);

        if (!root.description().isBlank()) {
            component = component.append(Component.text(" — ", NamedTextColor.DARK_GRAY)).append(Component.text(root.description(), NamedTextColor.GRAY));
        }

        for (CommandHelpEntry entry : entries) {
            for (CommandHelpUsage usage : entry.usages()) {
                component = component.append(Component.newline());
                component = component.append(Component.text(" - ", NamedTextColor.DARK_GRAY));
                component = component.append(renderUsage(usage));
            }
        }

        return component;
    }

    private Component renderUsage(CommandHelpUsage usage) {
        return renderUsageText(usage.usage())
                .clickEvent(ClickEvent.suggestCommand(usage.paste()))
                .hoverEvent(HoverEvent.showText(buildHover(usage)));
    }

    private Component renderUsageText(String usage) {
        Component component = Component.empty();
        List<String> segments = splitUsageSegments(usage);

        for (int i = 0; i < segments.size(); i++) {
            if (i > 0) {
                component = component.append(Component.text(" "));
            }

            component = component.append(renderSegment(segments.get(i)));
        }

        return component;
    }

    private Component renderSegment(String segment) {
        if (segment.startsWith("<") && segment.endsWith(">")) {
            return Component.text(segment, NamedTextColor.RED);
        }

        if (segment.startsWith("[") && segment.endsWith("]")) {
            if (segment.contains("--") && !segment.contains("<")) {
                return Component.text(segment, NamedTextColor.GOLD);
            }

            return Component.text(segment, NamedTextColor.BLUE);
        }

        return Component.text(segment, NamedTextColor.GRAY);
    }

    private Component buildHover(CommandHelpUsage usage) {
        Component hover = Component.empty();

        if (!usage.description().isBlank()) {
            hover = hover.append(Component.text(usage.description(), NamedTextColor.GRAY));
            hover = hover.append(Component.newline());
        }

        hover = hover.append(Component.text("Click to paste: " + usage.paste(), NamedTextColor.GRAY));
        return hover;
    }

    private List<String> splitUsageSegments(String usage) {
        List<String> segments = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        int bracketDepth = 0;
        int angleDepth = 0;

        for (int i = 0; i < usage.length(); i++) {
            char c = usage.charAt(i);

            if (Character.isWhitespace(c) && bracketDepth == 0 && angleDepth == 0) {
                addSegment(segments, current);
                continue;
            }

            current.append(c);

            if (c == '[') {
                bracketDepth++;
            } else if (c == ']') {
                bracketDepth--;
            } else if (c == '<') {
                angleDepth++;
            } else if (c == '>') {
                angleDepth--;
            }
        }

        addSegment(segments, current);
        return List.copyOf(segments);
    }

    private void addSegment(List<String> segments, StringBuilder current) {
        if (current.isEmpty()) {
            return;
        }

        segments.add(current.toString());
        current.setLength(0);
    }

}