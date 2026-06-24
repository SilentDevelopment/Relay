package io.github.silentdevelopment.relay.core.suggestion;

import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import io.github.silentdevelopment.relay.core.requirement.DefaultCommandAccessResolver;
import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.SuggestibleCommand;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.suggestion.CommandSuggester;
import io.github.silentdevelopment.relay.suggestion.SuggestingArgumentType;
import io.github.silentdevelopment.relay.suggestion.SuggestionContext;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class DefaultCommandSuggester<S> implements CommandSuggester<S> {

    private final CommandAccessResolver<S> accessResolver;

    public DefaultCommandSuggester() {
        this(new DefaultCommandAccessResolver<>());
    }

    public DefaultCommandSuggester(CommandAccessResolver<S> accessResolver) {
        this.accessResolver = Objects.requireNonNull(accessResolver, "accessResolver");
    }

    @Override
    public List<String> suggest(S source, Command command, String input) {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null.");
        }

        if (command == null) {
            throw new IllegalArgumentException("command cannot be null.");
        }

        if (!this.accessResolver.canAccess(source, command)) {
            return List.of();
        }

        String safeInput = input == null ? "" : input;
        List<String> tokens = DefaultArgumentReader.ofLenient(safeInput).getTokens();
        boolean endsWithSpace = !safeInput.isEmpty() && Character.isWhitespace(safeInput.charAt(safeInput.length() - 1));
        String partial = endsWithSpace || tokens.isEmpty() ? "" : tokens.get(tokens.size() - 1);
        int completedTokenCount = endsWithSpace || tokens.isEmpty() ? tokens.size() : tokens.size() - 1;

        Command current = command;
        int consumedSubcommands = 0;

        for (int i = 0; i < completedTokenCount; i++) {
            Command subcommand = findAccessibleSubcommand(source, current, tokens.get(i));

            if (subcommand == null) {
                break;
            }

            current = subcommand;
            consumedSubcommands++;
        }

        List<String> completedTokens = tokens.subList(consumedSubcommands, completedTokenCount);
        SuggestionState state = analyzeCompletedTokens(current, completedTokens);

        if (!state.valid()) {
            return List.of();
        }

        LinkedHashSet<String> suggestions = new LinkedHashSet<>();

        if (state.pendingValueOption() != null) {
            collectOptionValueSuggestions(suggestions, source, current, state.pendingValueOption(), safeInput, partial);
            return List.copyOf(suggestions);
        }

        if (looksLikeOptionPrefix(current, partial)) {
            if (!state.stopOptionParsing()) {
                collectOptionSuggestions(suggestions, current, partial, state.usedOptions());
            }

            return List.copyOf(suggestions);
        }

        if (completedTokens.isEmpty()) {
            collectSubcommandSuggestions(suggestions, source, current, partial);
        }

        collectArgumentSuggestions(suggestions, source, current, state.positionalTokens(), safeInput, partial);
        return List.copyOf(suggestions);
    }

    private SuggestionState analyzeCompletedTokens(Command command, List<String> tokens) {
        List<String> positionalTokens = new ArrayList<>();
        LinkedHashSet<CommandOption> usedOptions = new LinkedHashSet<>();
        boolean stopOptionParsing = false;
        ValueCommandOption<?> pendingValueOption = null;

        for (String token : tokens) {
            if (pendingValueOption != null) {
                pendingValueOption = null;
                continue;
            }

            if (stopOptionParsing) {
                positionalTokens.add(token);
                continue;
            }

            if (token.equals("--")) {
                stopOptionParsing = true;
                continue;
            }

            CommandOption option = resolveOption(command, token);

            if (option == null) {
                if (looksLikeCompletedOption(command, token)) {
                    return SuggestionState.invalid();
                }

                positionalTokens.add(token);
                continue;
            }

            if (!usedOptions.add(option)) {
                return SuggestionState.invalid();
            }

            if (option instanceof ValueCommandOption<?> valueOption) {
                pendingValueOption = valueOption;
            }
        }

        return SuggestionState.valid(List.copyOf(positionalTokens), Set.copyOf(usedOptions), pendingValueOption, stopOptionParsing);
    }

    private void collectSubcommandSuggestions(LinkedHashSet<String> suggestions, S source, Command command, String partial) {
        for (Command subcommand : command.subCommands()) {
            if (!this.accessResolver.canAccess(source, subcommand)) {
                continue;
            }

            addIfMatching(suggestions, subcommand.name(), partial);

            if (!subcommand.suggestAliases()) {
                continue;
            }

            for (String alias : subcommand.aliases()) {
                addIfMatching(suggestions, alias, partial);
            }
        }
    }

    private void collectArgumentSuggestions(LinkedHashSet<String> suggestions, S source, Command command, List<String> positionalTokens, String input, String partial) {
        for (Signature signature : command.signatures()) {
            Argument<?> activeArgument = findActiveArgument(signature, positionalTokens);

            if (activeArgument == null) {
                continue;
            }

            SuggestionProvider<S> provider = findSuggestionProvider(command, activeArgument);

            if (provider == null) {
                continue;
            }

            SuggestionContext<S> context = new SuggestionContext<>(source, command, signature, activeArgument, input, partial);

            for (String suggestion : provider.suggest(context)) {
                addIfMatching(suggestions, suggestion, partial);
            }
        }
    }

    private void collectOptionValueSuggestions(LinkedHashSet<String> suggestions, S source, Command command, ValueCommandOption<?> option, String input, String partial) {
        Argument<?> argument = option.getArgument();
        SuggestionProvider<S> provider = findSuggestionProvider(command, argument);

        if (provider == null) {
            return;
        }

        Signature signature = command.signatures().isEmpty() ? Signature.of() : command.signatures().getFirst();
        SuggestionContext<S> context = new SuggestionContext<>(source, command, signature, argument, input, partial);

        for (String suggestion : provider.suggest(context)) {
            addIfMatching(suggestions, suggestion, partial);
        }
    }

    private SuggestionProvider<S> findSuggestionProvider(Command command, Argument<?> argument) {
        if (command instanceof SuggestibleCommand<?> suggestibleCommand) {
            @SuppressWarnings("unchecked")
            SuggestibleCommand<S> typedCommand = (SuggestibleCommand<S>) suggestibleCommand;
            SuggestionProvider<S> provider = typedCommand.getSuggestionProviders().get(argument);

            if (provider != null) {
                return provider;
            }
        }

        if (argument.type() instanceof SuggestingArgumentType<?, ?> suggestingArgumentType) {
            @SuppressWarnings("unchecked")
            SuggestingArgumentType<S, ?> typedArgumentType = (SuggestingArgumentType<S, ?>) suggestingArgumentType;
            return typedArgumentType.getSuggestionProvider();
        }

        return null;
    }

    private Argument<?> findActiveArgument(Signature signature, List<String> tokens) {
        ArgumentReader reader = DefaultArgumentReader.of(tokens);

        for (Argument<?> argument : signature.arguments()) {
            if (!reader.hasNext()) {
                return argument;
            }

            int cursor = reader.getCursor();
            boolean success = argument.type().parser().parse(reader).isSuccess();

            if (!success) {
                reader.setCursor(cursor);
                return argument;
            }
        }

        if (signature.arguments().isEmpty()) {
            return null;
        }

        Argument<?> last = signature.arguments().getLast();

        if (last.greedy()) {
            return last;
        }

        return null;
    }

    private void collectOptionSuggestions(LinkedHashSet<String> suggestions, Command command, String partial, Set<CommandOption> usedOptions) {
        for (CommandOption option : command.options()) {
            if (usedOptions.contains(option)) {
                continue;
            }

            addOptionLiteralIfMatching(suggestions, command, option.getName(), partial);

            for (String alias : option.getAliases()) {
                addOptionLiteralIfMatching(suggestions, command, alias, partial);
            }
        }
    }

    private void addOptionLiteralIfMatching(LinkedHashSet<String> suggestions, Command command, String literal, String partial) {
        String candidate = command.flagLiteral(literal);
        OptionKind partialKind = detectOptionKind(command, partial, true);
        OptionKind candidateKind = detectOptionKind(command, candidate, false);

        if (partialKind != OptionKind.NONE && partialKind != candidateKind) {
            return;
        }

        addIfMatching(suggestions, candidate, partial);
    }

    private boolean looksLikeOptionPrefix(Command command, String token) {
        return detectOptionKind(command, token, true) != OptionKind.NONE;
    }

    private boolean looksLikeCompletedOption(Command command, String token) {
        return detectOptionKind(command, token, false) != OptionKind.NONE;
    }

    private CommandOption resolveOption(Command command, String token) {
        OptionKind kind = detectOptionKind(command, token, false);

        if (kind == OptionKind.NONE) {
            return null;
        }

        if (kind == OptionKind.LONG) {
            String literal = token.substring(command.longFlagPrefix().length());
            return findLongOption(command, literal);
        }

        String literal = token.substring(command.shortFlagPrefix().length());
        return findShortOption(command, literal);
    }

    private CommandOption findLongOption(Command command, String literal) {
        for (CommandOption option : command.options()) {
            if (option.getName().length() > 1 && option.getName().equalsIgnoreCase(literal)) {
                return option;
            }

            for (String alias : option.getAliases()) {
                if (alias.length() > 1 && alias.equalsIgnoreCase(literal)) {
                    return option;
                }
            }
        }

        return null;
    }

    private CommandOption findShortOption(Command command, String literal) {
        for (CommandOption option : command.options()) {
            if (option.getName().length() == 1 && option.getName().equalsIgnoreCase(literal)) {
                return option;
            }

            for (String alias : option.getAliases()) {
                if (alias.length() == 1 && alias.equalsIgnoreCase(literal)) {
                    return option;
                }
            }
        }

        return null;
    }

    private OptionKind detectOptionKind(Command command, String token, boolean allowBarePrefix) {
        if (token == null || token.isBlank()) {
            return OptionKind.NONE;
        }

        String longPrefix = command.longFlagPrefix();
        String shortPrefix = command.shortFlagPrefix();

        if (matchesPrefix(token, longPrefix, allowBarePrefix)) {
            return OptionKind.LONG;
        }

        if (matchesPrefix(token, shortPrefix, allowBarePrefix)) {
            if (!shortPrefix.equals(longPrefix) && token.startsWith(longPrefix)) {
                return OptionKind.NONE;
            }

            if (token.length() > shortPrefix.length() && Character.isDigit(token.charAt(shortPrefix.length()))) {
                return OptionKind.NONE;
            }

            return OptionKind.SHORT;
        }

        return OptionKind.NONE;
    }

    private boolean matchesPrefix(String token, String prefix, boolean allowBarePrefix) {
        if (!token.startsWith(prefix)) {
            return false;
        }

        return allowBarePrefix ? token.length() >= prefix.length() : token.length() > prefix.length();
    }

    private Command findAccessibleSubcommand(S source, Command command, String input) {
        for (Command subcommand : command.subCommands()) {
            if (!this.accessResolver.canAccess(source, subcommand)) {
                continue;
            }

            if (matches(commandLiteral(subcommand), input)) {
                return subcommand;
            }

            for (String alias : subcommand.aliases()) {
                if (matches(alias, input)) {
                    return subcommand;
                }
            }
        }

        return null;
    }

    private String commandLiteral(Command command) {
        return command.name();
    }

    private boolean matches(String literal, String input) {
        return literal.toLowerCase(Locale.ROOT).equals(input.toLowerCase(Locale.ROOT));
    }

    private void addIfMatching(LinkedHashSet<String> suggestions, String candidate, String partial) {
        if (candidate == null || candidate.isBlank()) {
            return;
        }

        if (!partial.isBlank() && !candidate.regionMatches(true, 0, partial, 0, partial.length())) {
            return;
        }

        suggestions.add(candidate);
    }

    private enum OptionKind {
        NONE,
        SHORT,
        LONG
    }

    private record SuggestionState(List<String> positionalTokens, Set<CommandOption> usedOptions, ValueCommandOption<?> pendingValueOption, boolean stopOptionParsing, boolean valid) {

        private static SuggestionState valid(List<String> positionalTokens, Set<CommandOption> usedOptions, ValueCommandOption<?> pendingValueOption, boolean stopOptionParsing) {
            return new SuggestionState(positionalTokens, usedOptions, pendingValueOption, stopOptionParsing, true);
        }

        private static SuggestionState invalid() {
            return new SuggestionState(List.of(), Set.of(), null, false, false);
        }

    }

}
