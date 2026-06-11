package io.github.silentdevelopment.relay.paper.adapter;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Objects;

public final class PaperBasicCommandAdapter implements BasicCommand {

    private final Command root;
    private final PaperCommandManager manager;

    public PaperBasicCommandAdapter(Command root, PaperCommandManager manager) {
        this.root = Objects.requireNonNull(root, "root");
        this.manager = Objects.requireNonNull(manager, "manager");
    }

    @Override
    public void execute(CommandSourceStack source, String @NonNull [] args) {
        CommandSender sender = source.getSender();
        String arguments = String.join(" ", args);
        this.manager.dispatchAndRespond(sender, this.root, this.root.name(), arguments);
    }

    @Override
    public @NotNull Collection<String> suggest(CommandSourceStack source, String @NonNull [] args) {
        CommandSender sender = source.getSender();
        String input = String.join(" ", args);
        return this.manager.suggest(sender, this.root, input);
    }

    @Override
    public boolean canUse(@NonNull CommandSender sender) {
        return this.manager.accessResolver().canAccess(sender, this.root);
    }

    @Override
    public String permission() {
        return null;
    }

}