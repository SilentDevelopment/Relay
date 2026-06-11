package io.github.silentdevelopment.relay.paper.requirement;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.requirement.DefaultCommandAccessResolver;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.requirement.RequirementResult;
import org.bukkit.command.CommandSender;

public class PaperCommandAccessResolver implements CommandAccessResolver<CommandSender> {

    private final CommandAccessResolver<CommandSender> delegate;

    public PaperCommandAccessResolver(CommandAccessResolver<CommandSender> accessResolver) {
        this.delegate = accessResolver;
    }

    public PaperCommandAccessResolver() {
        this(new DefaultCommandAccessResolver<>());
    }

    @Override
    public RequirementResult evaluate(CommandSender source, Command command) {
        return this.delegate.evaluate(source, command);
    }

}