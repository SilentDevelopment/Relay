package io.github.silentdevelopment.relay.paper.bootstrap;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.adapter.PaperBasicCommandAdapter;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class PaperLifecycleCommandRegistrar {

    private final PluginMeta pluginMeta;
    private final Installer installer;
    private final PaperCommandManager manager;
    private final List<Command> roots;
    private boolean installed;

    public PaperLifecycleCommandRegistrar(BootstrapContext context, PaperCommandManager manager) {
        Objects.requireNonNull(context, "context");
        this.pluginMeta = context.getPluginMeta();
        this.installer = registrar -> context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> registrar.register(event.registrar(), this.pluginMeta)));
        this.manager = Objects.requireNonNull(manager, "manager");
        this.roots = new ArrayList<>();
        this.installed = false;
    }

    public PaperLifecycleCommandRegistrar(JavaPlugin plugin, PaperCommandManager manager) {
        Objects.requireNonNull(plugin, "plugin");
        this.pluginMeta = plugin.getPluginMeta();
        this.installer = registrar -> plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> registrar.register(event.registrar(), this.pluginMeta)));
        this.manager = Objects.requireNonNull(manager, "manager");
        this.roots = new ArrayList<>();
        this.installed = false;
    }

    public void register(CommandDefinition<CommandSender> definition) {
        Objects.requireNonNull(definition, "definition");

        if (this.installed) {
            throw new IllegalStateException("Cannot register commands after install() has been called.");
        }

        definition.bind(this.manager.dispatcher());

        Command root = definition.command();
        this.manager.registerRoot(root);
        this.roots.add(root);
    }

    public void registerAll(CommandDefinition<CommandSender>... definitions) {
        Objects.requireNonNull(definitions, "definitions");

        for (CommandDefinition<CommandSender> definition : definitions) {
            if (definition == null) {
                throw new IllegalArgumentException("definitions cannot contain null values.");
            }

            register(definition);
        }
    }

    public void registerAll(Collection<? extends CommandDefinition<CommandSender>> definitions) {
        Objects.requireNonNull(definitions, "definitions");

        for (CommandDefinition<CommandSender> definition : definitions) {
            if (definition == null) {
                throw new IllegalArgumentException("definitions cannot contain null values.");
            }

            register(definition);
        }
    }

    public void install() {
        if (this.installed) {
            throw new IllegalStateException("install() has already been called.");
        }

        this.installed = true;
        this.installer.install(this::registerCommands);
    }

    private void registerCommands(Commands commands, PluginMeta pluginMeta) {
        for (Command root : this.roots) {
            commands.register(
                    pluginMeta,
                    root.name(),
                    root.description().isBlank() ? null : root.description(),
                    List.copyOf(root.aliases()),
                    new PaperBasicCommandAdapter(root, this.manager)
            );
        }
    }

    @FunctionalInterface
    private interface Installer {

        void install(CommandInstaller installer);

    }

    @FunctionalInterface
    private interface CommandInstaller {

        void register(Commands commands, PluginMeta pluginMeta);

    }

}