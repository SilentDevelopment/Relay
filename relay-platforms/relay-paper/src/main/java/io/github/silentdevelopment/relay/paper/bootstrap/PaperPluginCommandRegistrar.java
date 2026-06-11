package io.github.silentdevelopment.relay.paper.bootstrap;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.adapter.PaperCommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * @deprecated Use {@link RelayBootstrap}.
 * @see RelayBootstrap
 * @see PaperLifecycleCommandRegistrar
 */
@Deprecated
public final class PaperPluginCommandRegistrar {

    private final JavaPlugin plugin;
    private final PaperCommandManager manager;

    public PaperPluginCommandRegistrar(JavaPlugin plugin, PaperCommandManager manager) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.manager = Objects.requireNonNull(manager, "manager");
    }

    public void register(Command command) {
        Objects.requireNonNull(command, "command");

        PluginCommand pluginCommand = plugin.getCommand(command.name());

        if (pluginCommand == null) {
            throw new IllegalStateException("Command '" + command.name() + "' is missing from plugin.yml.");
        }

        manager.registerRoot(command);

        PaperCommandExecutor executor = new PaperCommandExecutor(command, manager);
        pluginCommand.setExecutor(executor);
        pluginCommand.setTabCompleter(executor);
    }

    public void registerAll(Command... commands) {
        Objects.requireNonNull(commands, "commands");

        for (Command command : commands) {
            if (command == null) {
                throw new IllegalArgumentException("commands cannot contain null values.");
            }

            register(command);
        }
    }

    public void register(CommandDefinition<CommandSender> command) {
        Objects.requireNonNull(command, "command");
        command.bind(manager.dispatcher());
        register(command.command());
    }

    public void registerAll(CommandDefinition<CommandSender>... commands) {
        Objects.requireNonNull(commands, "commands");

        for (CommandDefinition<CommandSender> command : commands) {
            if (command == null) {
                throw new IllegalArgumentException("commands cannot contain null values.");
            }

            register(command);
        }
    }

}