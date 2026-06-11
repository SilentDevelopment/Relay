package io.github.silentdevelopment.relay.paper.text.usage;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class StyledPaperUsageComponentRenderer implements PaperUsageComponentRenderer {

    private final boolean clickable;

    public StyledPaperUsageComponentRenderer() {
        this(false);
    }

    public StyledPaperUsageComponentRenderer(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public Component render(String path, Command command, Signature signature) {
        Component component = Component.text(path, NamedTextColor.GRAY);

        for (CommandOption option : command.options()) {
            component = component.append(Component.text(" "));
            component = component.append(renderOption(command, option));
        }

        for (Argument<?> argument : signature.arguments()) {
            component = component.append(Component.text(" "));
            component = component.append(renderArgument(argument));
        }

        if (!this.clickable) {
            return component;
        }

        String suggested = path.endsWith(" ") ? path : path + " ";

        return component.clickEvent(ClickEvent.suggestCommand(suggested)).hoverEvent(HoverEvent.showText(buildHover(suggested, command)));
    }

    private Component renderOption(Command command, CommandOption option) {
        String literal = command.flagLiteral(option.getName());

        if (option instanceof CommandFlag) {
            return Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text(literal, NamedTextColor.GOLD))
                    .append(Component.text("]", NamedTextColor.DARK_GRAY));
        }

        if (option instanceof ValueCommandOption<?> valueOption) {
            return Component.text("[", NamedTextColor.DARK_GRAY)
                    .append(Component.text(literal, NamedTextColor.GOLD))
                    .append(Component.text(" ", NamedTextColor.GRAY))
                    .append(Component.text(valueOption.getArgument().usage(), NamedTextColor.BLUE))
                    .append(Component.text("]", NamedTextColor.DARK_GRAY));
        }

        return Component.text("[" + literal + "]", NamedTextColor.BLUE);
    }

    private Component renderArgument(Argument<?> argument) {
        NamedTextColor color = argument.required() ? NamedTextColor.RED : NamedTextColor.BLUE;
        return Component.text(argument.usage(), color);
    }

    private Component buildHover(String suggested, Command command) {
        Component hover = Component.empty();

        if (!command.description().isBlank()) {
            hover = hover.append(Component.text(command.description(), NamedTextColor.GRAY));
            hover = hover.append(Component.newline());
        }

        hover = hover.append(Component.text("Click to paste: " + suggested, NamedTextColor.GRAY));
        return hover;
    }

}