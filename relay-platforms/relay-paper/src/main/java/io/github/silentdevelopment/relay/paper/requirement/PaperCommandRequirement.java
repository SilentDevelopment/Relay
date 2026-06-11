package io.github.silentdevelopment.relay.paper.requirement;

import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface PaperCommandRequirement extends CommandRequirement<CommandSender> {}