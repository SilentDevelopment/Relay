package io.github.silentdevelopment.relay.paper.bootstrap;

import io.github.silentdevelopment.relay.command.CommandDefinition;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public abstract class RelayBootstrap implements PluginBootstrap {

    @Override
    public final void bootstrap(@NotNull BootstrapContext context) {
        PaperCommandManager manager = createManager();
        PaperLifecycleCommandRegistrar registrar = new PaperLifecycleCommandRegistrar(context, manager);
        registrar.registerAll(commands());
        registrar.install();
        afterBootstrap(context, manager);
    }

    @Override
    public abstract @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context);

    protected PaperCommandManager createManager() {
        return new PaperCommandManager();
    }

    protected abstract Collection<? extends CommandDefinition<CommandSender>> commands();

    protected void afterBootstrap(BootstrapContext context, PaperCommandManager manager) {}

}