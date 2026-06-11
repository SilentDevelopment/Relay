package io.github.silentdevelopment.relay.paper.support;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class StubBukkitCommand extends Command {

    public StubBukkitCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }

}
