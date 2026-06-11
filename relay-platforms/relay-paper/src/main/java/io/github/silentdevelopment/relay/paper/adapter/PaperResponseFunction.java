package io.github.silentdevelopment.relay.paper.adapter;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface PaperResponseFunction {

    void send(CommandSender sender, Component message);

}