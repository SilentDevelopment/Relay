package io.github.silentdevelopment.relay.paper.command.builder;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.requirement.PredicateCommandRequirement;
import io.github.silentdevelopment.relay.paper.argument.PaperArgumentTypes;
import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;
import io.github.silentdevelopment.relay.text.CommandText;
import io.github.silentdevelopment.relay.text.CommandTexts;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class PaperCommandBuilder {

    private final CommandBuilder<CommandSender> delegate;

    private PaperCommandBuilder(String name) {
        this.delegate = CommandBuilder.literal(name);
    }

    public static PaperCommandBuilder literal(String name) {
        return new PaperCommandBuilder(name);
    }

    public PaperCommandBuilder description(String description) {
        this.delegate.description(description);
        return this;
    }

    public PaperCommandBuilder shortFlagPrefix(String shortFlagPrefix) {
        this.delegate.shortFlagPrefix(shortFlagPrefix);
        return this;
    }

    public PaperCommandBuilder longFlagPrefix(String longFlagPrefix) {
        this.delegate.longFlagPrefix(longFlagPrefix);
        return this;
    }

    public PaperCommandBuilder alias(String alias) {
        this.delegate.alias(alias);
        return this;
    }

    public PaperCommandBuilder aliases(String... aliases) {
        this.delegate.aliases(aliases);
        return this;
    }

    public PaperCommandBuilder aliases(Collection<String> aliases) {
        this.delegate.aliases(aliases);
        return this;
    }

    public PaperCommandBuilder subcommand(Command subcommand) {
        this.delegate.subcommand(subcommand);
        return this;
    }

    public PaperCommandBuilder subcommands(Command... subcommands) {
        this.delegate.subcommands(subcommands);
        return this;
    }

    public PaperCommandBuilder subcommands(Collection<? extends Command> subcommands) {
        this.delegate.subcommands(subcommands);
        return this;
    }

    public PaperCommandBuilder signature(Signature signature) {
        this.delegate.signature(signature);
        return this;
    }

    public PaperCommandBuilder signature(Argument<?>... arguments) {
        this.delegate.signature(arguments);
        return this;
    }

    public PaperCommandBuilder noArgs() {
        this.delegate.noArgs();
        return this;
    }

    public PaperCommandBuilder requirement(CommandRequirement<CommandSender> requirement) {
        this.delegate.requirement(requirement);
        return this;
    }

    public PaperCommandBuilder suggest(Argument<?> argument, SuggestionProvider<CommandSender> provider) {
        this.delegate.suggest(argument, provider);
        return this;
    }

    public PaperCommandBuilder flag(CommandFlag flag) {
        this.delegate.flag(flag);
        return this;
    }

    public PaperCommandBuilder flags(CommandFlag... flags) {
        this.delegate.flags(flags);
        return this;
    }

    public PaperCommandBuilder flags(Collection<? extends CommandFlag> flags) {
        this.delegate.flags(flags);
        return this;
    }

    public PaperCommandBuilder option(CommandOption option) {
        this.delegate.option(option);
        return this;
    }

    public PaperCommandBuilder options(CommandOption... options) {
        this.delegate.options(options);
        return this;
    }

    public PaperCommandBuilder options(Collection<? extends CommandOption> options) {
        this.delegate.options(options);
        return this;
    }

    public PaperCommandBuilder permission(String permission) {
        return permission(permission, CommandTexts.noPermission(permission));
    }

    public PaperCommandBuilder permission(String permission, String message) {
        return permission(permission, CommandTexts.requirementFailed(message));
    }

    public PaperCommandBuilder permission(String permission, CommandText denial) {
        validatePermission(permission);
        Objects.requireNonNull(denial, "denial");

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> sender.hasPermission(permission),
                denial.withPlaceholder("permission", permission)
        ));

        return this;
    }

    public PaperCommandBuilder anyPermission(String... permissions) {
        return anyPermission(CommandTexts.requirementFailed("You do not have permission to use this command."), permissions);
    }

    public PaperCommandBuilder anyPermission(CommandText denial, String... permissions) {
        Set<String> values = permissionSet(permissions);
        Objects.requireNonNull(denial, "denial");

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> values.stream().anyMatch(sender::hasPermission),
                denial.withPlaceholder("permissions", String.join(", ", values))
        ));

        return this;
    }

    public PaperCommandBuilder allPermissions(String... permissions) {
        return allPermissions(CommandTexts.requirementFailed("You do not have permission to use this command."), permissions);
    }

    public PaperCommandBuilder allPermissions(CommandText denial, String... permissions) {
        Set<String> values = permissionSet(permissions);
        Objects.requireNonNull(denial, "denial");

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> values.stream().allMatch(sender::hasPermission),
                denial.withPlaceholder("permissions", String.join(", ", values))
        ));

        return this;
    }

    public PaperCommandBuilder playerOnly() {
        return playerOnly(CommandTexts.playerOnly());
    }

    public PaperCommandBuilder playerOnly(String message) {
        return playerOnly(CommandTexts.requirementFailed(message));
    }

    public PaperCommandBuilder playerOnly(CommandText denial) {
        Objects.requireNonNull(denial, "denial");

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> sender instanceof Player,
                denial
        ));

        return this;
    }

    public PaperCommandBuilder consoleOnly() {
        return consoleOnly(CommandTexts.consoleOnly());
    }

    public PaperCommandBuilder consoleOnly(String message) {
        return consoleOnly(CommandTexts.requirementFailed(message));
    }

    public PaperCommandBuilder consoleOnly(CommandText denial) {
        Objects.requireNonNull(denial, "denial");

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> !(sender instanceof Player),
                denial
        ));

        return this;
    }

    public PaperCommandBuilder suggestPlayers(Argument<Player> argument) {
        Objects.requireNonNull(argument, "argument");

        this.delegate.suggest(argument, ctx -> Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList());

        return this;
    }

    public PaperCommandBuilder suggestWorlds(Argument<World> argument) {
        Objects.requireNonNull(argument, "argument");

        this.delegate.suggest(argument, ctx -> Bukkit.getWorlds().stream()
                .map(World::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList());

        return this;
    }

    public PaperCommandBuilder playerArgument(String name) {
        this.delegate.signature(Argument.required(name, PaperArgumentTypes.PLAYER));
        return this;
    }

    public PaperCommandBuilder worldArgument(String name) {
        this.delegate.signature(Argument.required(name, PaperArgumentTypes.WORLD));
        return this;
    }

    public CommandBuilder<CommandSender> asCommandBuilder() {
        return this.delegate;
    }

    public Command build() {
        return this.delegate.build();
    }

    private static Set<String> permissionSet(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions cannot be null or empty.");
        }

        Set<String> values = new LinkedHashSet<>();

        for (String permission : permissions) {
            validatePermission(permission);
            values.add(permission);
        }

        if (values.isEmpty()) {
            throw new IllegalArgumentException("permissions cannot be empty.");
        }

        return Collections.unmodifiableSet(values);
    }

    private static void validatePermission(String permission) {
        if (permission == null || permission.isBlank()) {
            throw new IllegalArgumentException("permission cannot be null or blank.");
        }
    }

}