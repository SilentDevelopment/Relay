package io.github.silentdevelopment.relay.paper.command;

import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.core.command.AbstractCommandGroupDefinition;
import io.github.silentdevelopment.relay.paper.command.builder.PaperCommandBuilder;
import io.github.silentdevelopment.relay.paper.command.context.DefaultPaperCommandContext;
import io.github.silentdevelopment.relay.paper.command.context.PaperCommandContext;
import org.bukkit.command.CommandSender;

public abstract class AbstractPaperCommandGroup extends AbstractCommandGroupDefinition<CommandSender> {

    @Override
    protected final void handle(CommandContext<CommandSender> context) {
        handle(new DefaultPaperCommandContext(context));
    }

    protected void handle(PaperCommandContext context) {}

    protected final PaperCommandBuilder root(String name, String description) {
        return PaperCommands.literal(name).description(description).noArgs();
    }

}