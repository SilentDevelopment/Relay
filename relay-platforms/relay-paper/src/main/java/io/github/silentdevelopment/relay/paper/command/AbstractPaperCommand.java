package io.github.silentdevelopment.relay.paper.command;

import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.core.command.AbstractCommandDefinition;
import io.github.silentdevelopment.relay.paper.command.context.DefaultPaperCommandContext;
import io.github.silentdevelopment.relay.paper.command.context.PaperCommandContext;
import org.bukkit.command.CommandSender;

public abstract class AbstractPaperCommand extends AbstractCommandDefinition<CommandSender> {

    @Override
    protected final void handle(CommandContext<CommandSender> context) {
        handle(new DefaultPaperCommandContext(context));
    }

    protected abstract void handle(PaperCommandContext context);

}