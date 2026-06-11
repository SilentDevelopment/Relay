package io.github.silentdevelopment.relay.core.command;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.RestrictedCommand;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.SuggestibleCommand;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class DefaultCommand<S> implements RestrictedCommand<S>, SuggestibleCommand<S> {

    private final String name;
    private final String description;
    private final List<String> aliases;
    private final List<Command> subcommands;
    private final List<Signature> signatures;
    private final List<CommandOption> options;
    private final List<CommandRequirement<S>> requirements;
    private final Map<Argument<?>, SuggestionProvider<S>> suggestionProviders;
    private final String shortFlagPrefix;
    private final String longFlagPrefix;

    public DefaultCommand(
            @NotNull String name,
            @Nullable String description,
            @NotNull List<String> aliases,
            @NotNull List<Command> subcommands,
            @NotNull List<Signature> signatures,
            @NotNull List<CommandOption> options,
            @NotNull List<CommandRequirement<S>> requirements,
            @NotNull Map<Argument<?>, SuggestionProvider<S>> suggestionProviders,
            @Nullable String shortFlagPrefix,
            @Nullable String longFlagPrefix
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank.");
        }

        this.name = name;
        this.description = description == null ? "" : description;
        this.aliases = List.copyOf(Objects.requireNonNull(aliases, "aliases"));
        this.subcommands = List.copyOf(Objects.requireNonNull(subcommands, "subcommands"));
        this.signatures = List.copyOf(Objects.requireNonNull(signatures, "signatures"));
        this.options = List.copyOf(Objects.requireNonNull(options, "options"));
        this.requirements = List.copyOf(Objects.requireNonNull(requirements, "requirements"));
        this.suggestionProviders = Map.copyOf(Objects.requireNonNull(suggestionProviders, "suggestionProviders"));
        this.shortFlagPrefix = validateFlagPrefix(shortFlagPrefix, "shortFlagPrefix");
        this.longFlagPrefix = validateFlagPrefix(longFlagPrefix, "longFlagPrefix");
        validateAliases(this.name, this.aliases);
        validateSubcommands(this.subcommands);
        validateSignatures(this.signatures);
        validateResolvedPrefixes();
        validateStrings(this.aliases, "aliases");
        validateOptions(this.options);
        validateResolvedPrefixes();
    }

    @Override
    public @NotNull String name() {
        return this.name;
    }

    @Override
    public @NotNull List<String> aliases() {
        return this.aliases;
    }

    @Override
    public @Nullable String description() {
        return this.description;
    }

    @Override
    public @NotNull List<Command> subCommands() {
        return this.subcommands;
    }

    @Override
    public @NotNull List<Signature> signatures() {
        return this.signatures;
    }

    @Override
    public List<CommandOption> options() {
        return this.options;
    }

    @Override
    public @NotNull String shortFlagPrefix() {
        return this.shortFlagPrefix == null ? "-" : this.shortFlagPrefix;
    }

    @Override
    public @NotNull String longFlagPrefix() {
        return this.longFlagPrefix == null ? "--" : this.longFlagPrefix;
    }

    @Override
    public List<CommandRequirement<S>> getRequirements() {
        return this.requirements;
    }

    @Override
    public Map<Argument<?>, SuggestionProvider<S>> getSuggestionProviders() {
        return this.suggestionProviders;
    }

    private static void validateAliases(String name, List<String> aliases) {
        Set<String> literals = new HashSet<>();
        String normalizedName = normalizeLiteral(name);

        for (String alias : aliases) {
            if (alias == null || alias.isBlank()) {
                throw new IllegalArgumentException("aliases cannot contain null or blank values.");
            }

            String normalizedAlias = normalizeLiteral(alias);

            if (normalizedAlias.equals(normalizedName)) {
                throw new IllegalArgumentException("alias cannot be the same as the command name: " + alias);
            }

            if (!literals.add(normalizedAlias)) {
                throw new IllegalArgumentException("duplicate alias: " + alias);
            }
        }
    }

    private static void validateSubcommands(List<Command> subcommands) {
        Set<String> literals = new HashSet<>();

        for (Command subcommand : subcommands) {
            if (subcommand == null) {
                throw new IllegalArgumentException("subcommands cannot contain null values.");
            }

            registerCommandLiteral(literals, subcommand.name(), "subcommand name");

            for (String alias : subcommand.aliases()) {
                registerCommandLiteral(literals, alias, "subcommand alias");
            }

            validateSubcommands(subcommand.subCommands());
        }
    }

    private static void registerCommandLiteral(Set<String> literals, String literal, String label) {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be null or blank.");
        }

        String normalized = normalizeLiteral(literal);

        if (!literals.add(normalized)) {
            throw new IllegalArgumentException("duplicate " + label + ": " + literal);
        }
    }

    private static void validateSignatures(List<Signature> signatures) {
        Set<String> seen = new HashSet<>();

        for (Signature signature : signatures) {
            if (signature == null) {
                throw new IllegalArgumentException("signatures cannot contain null values.");
            }

            String key = signatureKey(signature);

            if (!seen.add(key)) {
                throw new IllegalArgumentException("duplicate signature: " + signature.usage());
            }
        }
    }

    private static String signatureKey(Signature signature) {
        StringJoiner joiner = new StringJoiner("|");

        for (Argument<?> argument : signature.arguments()) {
            joiner.add(argument.name());
            joiner.add(argument.type().getClass().getName());
            joiner.add(Boolean.toString(argument.required()));
            joiner.add(Boolean.toString(argument.greedy()));
        }

        return joiner.toString();
    }

    private static String normalizeLiteral(String literal) {
        return literal.toLowerCase(Locale.ROOT);
    }

    private void validateResolvedPrefixes() {
        if (shortFlagPrefix().equals(longFlagPrefix())) {
            throw new IllegalArgumentException("short and long Option prefixes cannot be the same.");
        }
    }

    private static void validateStrings(List<String> values, String label) {
        for (String value : values) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(label + " cannot contain null or blank values.");
            }
        }
    }

    private static String validateFlagPrefix(String flagPrefix, String label) {
        if (flagPrefix == null) {
            return null;
        }

        if (flagPrefix.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be blank.");
        }

        for (int i = 0; i < flagPrefix.length(); i++) {
            if (Character.isWhitespace(flagPrefix.charAt(i))) {
                throw new IllegalArgumentException(label + " cannot contain whitespace.");
            }
        }

        return flagPrefix;
    }

    private static void validateOptions(List<CommandOption> options) {
        Set<String> literals = new HashSet<>();

        for (CommandOption option : options) {
            if (option == null) {
                throw new IllegalArgumentException("options cannot contain null values.");
            }

            registerOptionLiteral(literals, option.getName(), "option name");

            for (String alias : option.getAliases()) {
                registerOptionLiteral(literals, alias, "option alias");
            }
        }
    }

    private static void registerOptionLiteral(Set<String> literals, String literal, String label) {
        if (literal == null || literal.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be null or blank.");
        }

        if (literal.startsWith("-")) {
            throw new IllegalArgumentException(label + " must not include leading dashes: " + literal);
        }

        String normalized = literal.toLowerCase(Locale.ROOT);

        if (!literals.add(normalized)) {
            throw new IllegalArgumentException("Duplicate option literal: " + literal);
        }
    }

}