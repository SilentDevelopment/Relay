package io.github.silentdevelopment.relay.paper.adapter;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.Objects;

public final class PaperCommandExecutor implements TabExecutor {

    private final Command root;
    private final PaperCommandManager manager;

    public PaperCommandExecutor(Command root, PaperCommandManager manager) {
        this.root = Objects.requireNonNull(root, "root");
        this.manager = Objects.requireNonNull(manager, "manager");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Objects.requireNonNull(sender, "sender");
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(label, "label");
        Objects.requireNonNull(args, "args");

        this.manager.dispatchAndRespond(sender, this.root, label, joinArguments(args));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Objects.requireNonNull(sender, "sender");
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(alias, "alias");
        Objects.requireNonNull(args, "args");

        return this.manager.suggest(sender, this.root, joinArguments(args));
    }

    private String joinArguments(String[] args) {
        if (args.length == 0) {
            return "";
        }

        return String.join(" ", args);
    }

}