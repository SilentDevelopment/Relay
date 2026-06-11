package io.github.silentdevelopment.relay.paper.command;

import io.github.silentdevelopment.relay.command.context.CommandContext;
import io.github.silentdevelopment.relay.command.CommandHandler;
import io.github.silentdevelopment.relay.paper.command.context.DefaultPaperCommandContext;
import io.github.silentdevelopment.relay.paper.command.context.PaperCommandContext;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface PaperCommandHandler extends CommandHandler<CommandSender> {

    void execute(PaperCommandContext context);

    @Override
    default void execute(CommandContext<CommandSender> context) {
        execute(new DefaultPaperCommandContext(context));
    }

}