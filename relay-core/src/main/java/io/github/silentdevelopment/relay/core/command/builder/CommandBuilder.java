package io.github.silentdevelopment.relay.core.command.builder;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandFlag;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.core.command.DefaultCommand;
import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;

import java.util.*;

public final class CommandBuilder<S> {

    private final String name;
    private String description;
    private final List<String> aliases;
    private boolean suggestAliases;
    private final List<Command> subcommands;
    private final List<Signature> signatures;
    private final List<CommandOption> options;
    private final List<CommandRequirement<S>> requirements;
    private final Map<Argument<?>, SuggestionProvider<S>> suggestionProviders;
    private String shortFlagPrefix;
    private String longFlagPrefix;

    private CommandBuilder(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank.");
        }

        this.name = name;
        this.description = "";
        this.aliases = new ArrayList<>();
        this.suggestAliases = false;
        this.subcommands = new ArrayList<>();
        this.signatures = new ArrayList<>();
        this.options = new ArrayList<>();
        this.requirements = new ArrayList<>();
        this.suggestionProviders = new LinkedHashMap<>();
        this.shortFlagPrefix = null;
        this.longFlagPrefix = null;
    }

    public static <S> CommandBuilder<S> literal(String name) {
        return new CommandBuilder<>(name);
    }

    public CommandBuilder<S> description(String description) {
        this.description = description == null ? "" : description;
        return this;
    }

    public CommandBuilder<S> shortFlagPrefix(String shortFlagPrefix) {
        validateFlagPrefix(shortFlagPrefix, "shortFlagPrefix");
        this.shortFlagPrefix = shortFlagPrefix;
        return this;
    }

    public CommandBuilder<S> longFlagPrefix(String longFlagPrefix) {
        validateFlagPrefix(longFlagPrefix, "longFlagPrefix");
        this.longFlagPrefix = longFlagPrefix;
        return this;
    }

    public CommandBuilder<S> alias(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("alias cannot be null or blank.");
        }

        this.aliases.add(alias);
        return this;
    }

    public CommandBuilder<S> aliases(String... aliases) {
        if (aliases == null) {
            throw new IllegalArgumentException("aliases cannot be null.");
        }

        for (String alias : aliases) {
            alias(alias);
        }

        return this;
    }

    public CommandBuilder<S> aliases(Collection<String> aliases) {
        if (aliases == null) {
            throw new IllegalArgumentException("aliases cannot be null.");
        }

        for (String alias : aliases) {
            alias(alias);
        }

        return this;
    }

    public CommandBuilder<S> suggestAliases(boolean suggestAliases) {
        this.suggestAliases = suggestAliases;
        return this;
    }

    public CommandBuilder<S> subcommand(Command subcommand) {
        this.subcommands.add(Objects.requireNonNull(subcommand, "subcommand"));
        return this;
    }

    public CommandBuilder<S> subcommands(Command... subcommands) {
        if (subcommands == null) {
            throw new IllegalArgumentException("subcommands cannot be null.");
        }

        for (Command subcommand : subcommands) {
            subcommand(subcommand);
        }

        return this;
    }

    public CommandBuilder<S> subcommands(Collection<? extends Command> subcommands) {
        if (subcommands == null) {
            throw new IllegalArgumentException("subcommands cannot be null.");
        }

        for (Command subcommand : subcommands) {
            subcommand(subcommand);
        }

        return this;
    }

    public CommandBuilder<S> signature(Signature signature) {
        this.signatures.add(Objects.requireNonNull(signature, "signature"));
        return this;
    }

    public CommandBuilder<S> signature(Argument<?>... arguments) {
        if (arguments == null) {
            throw new IllegalArgumentException("arguments cannot be null.");
        }

        return signature(Signature.of(arguments));
    }

    public CommandBuilder<S> noArgs() {
        return signature(Signature.of());
    }

    public CommandBuilder<S> requirement(CommandRequirement<S> requirement) {
        this.requirements.add(Objects.requireNonNull(requirement, "requirement"));
        return this;
    }

    public CommandBuilder<S> suggest(Argument<?> argument, SuggestionProvider<S> provider) {
        this.suggestionProviders.put(Objects.requireNonNull(argument, "argument"), Objects.requireNonNull(provider, "provider"));
        return this;
    }

    public CommandBuilder<S> flag(CommandFlag flag) {
        this.options.add(Objects.requireNonNull(flag, "flag"));
        return this;
    }

    public CommandBuilder<S> flags(CommandFlag... flags) {
        if (flags == null) {
            throw new IllegalArgumentException("flags cannot be null.");
        }

        for (CommandFlag flag : flags) {
            flag(flag);
        }

        return this;
    }

    public CommandBuilder<S> flags(Collection<? extends CommandFlag> flags) {
        if (flags == null) {
            throw new IllegalArgumentException("flags cannot be null.");
        }

        for (CommandFlag flag : flags) {
            flag(flag);
        }

        return this;
    }

    public CommandBuilder<S> option(CommandOption option) {
        this.options.add(Objects.requireNonNull(option, "option"));
        return this;
    }

    public CommandBuilder<S> options(CommandOption... options) {
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null.");
        }

        for (CommandOption option : options) {
            option(option);
        }

        return this;
    }

    public CommandBuilder<S> options(Collection<? extends CommandOption> options) {
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null.");
        }

        for (CommandOption option : options) {
            option(option);
        }

        return this;
    }

    public Command build() {
        return new DefaultCommand<>(this.name, this.description, this.aliases, this.suggestAliases, this.subcommands, this.signatures, this.options, this.requirements, this.suggestionProviders, this.shortFlagPrefix, this.longFlagPrefix);
    }

    private void validateFlagPrefix(String flagPrefix, String label) {
        if (flagPrefix == null || flagPrefix.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be null or blank.");
        }

        for (int i = 0; i < flagPrefix.length(); i++) {
            if (Character.isWhitespace(flagPrefix.charAt(i))) {
                throw new IllegalArgumentException(label + " cannot contain whitespace.");
            }
        }
    }

}
