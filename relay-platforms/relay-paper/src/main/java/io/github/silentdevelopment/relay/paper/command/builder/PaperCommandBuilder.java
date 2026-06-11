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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

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
        if (permission == null || permission.isBlank()) {
            throw new IllegalArgumentException("permission cannot be null or blank.");
        }

        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> sender.hasPermission(permission),
                "You do not have permission to use this command."
        ));
        return this;
    }

    public PaperCommandBuilder playerOnly() {
        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> sender instanceof Player,
                "Only players can use this command."
        ));
        return this;
    }

    public PaperCommandBuilder consoleOnly() {
        this.delegate.requirement(new PredicateCommandRequirement<>(
                sender -> !(sender instanceof Player),
                "Only the console can use this command."
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

    public Command build() {
        return this.delegate.build();
    }

}