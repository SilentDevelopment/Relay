package io.github.silentdevelopment.relay.paper.command.context;

import io.github.silentdevelopment.relay.command.context.CommandContext;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PaperCommandContext extends CommandContext<CommandSender> {

    default boolean isPlayer() {
        return sender() instanceof Player;
    }

    default boolean isConsole() {
        return sender() instanceof ConsoleCommandSender || sender() instanceof RemoteConsoleCommandSender;
    }

    default boolean isEntity() {
        return sender() instanceof Entity;
    }

    default boolean isLivingEntity() {
        return sender() instanceof LivingEntity;
    }

    default CommandSender sender() {
        return source();
    }

    default Player player() {
        if (!(sender() instanceof Player player)) {
            throw new IllegalStateException("Command sender is not a player.");
        }

        return player;
    }

    default @Nullable Player playerNullable() {
        return isPlayer() ? (Player) sender() : null;
    }

    default UUID uuid() {
        return player().getUniqueId();
    }

    default @Nullable LivingEntity livingEntityNullable() {
        return sender() instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    default LivingEntity livingEntity() {
        if (!(sender() instanceof LivingEntity livingEntity)) {
            throw new IllegalStateException("Command sender is not a living entity.");
        }

        return livingEntity;
    }

    default @Nullable Entity entityNullable() {
        return sender() instanceof Entity entity ? entity : null;
    }

    default Entity entity() {
        if (!(sender() instanceof Entity entity)) {
            throw new IllegalStateException("Command sender is not an entity.");
        }

        return entity;
    }

    default @Nullable World worldNullable() {
        if (sender() instanceof Entity entity) {
            return entity.getWorld();
        }

        if (sender() instanceof BlockCommandSender commandBlock) {
            return commandBlock.getBlock().getWorld();
        }

        return null;
    }

    default World world() {
        World world = worldNullable();

        if (world == null) {
            throw new IllegalStateException("Command sender is not bound to a world.");
        }

        return world;
    }

    default boolean hasPermission(String permission) {
        if (permission == null || permission.isBlank()) {
            throw new IllegalArgumentException("Permission cannot be null or blank.");
        }

        return sender().hasPermission(permission);
    }

    default void reply(Component message) {
        sender().sendMessage(message);
    }

    default void reply(String message) {
        reply(Component.text(message));
    }

}