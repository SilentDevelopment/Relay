package io.github.silentdevelopment.relay.core.match;

import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReader;
import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.OptionParseResult;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.match.CommandMatch;
import io.github.silentdevelopment.relay.match.CommandMatchResult;
import io.github.silentdevelopment.relay.match.CommandMatcher;

import java.util.*;

public final class DefaultCommandMatcher implements CommandMatcher {

    @Override
    public CommandMatchResult match(Command command, ArgumentReader reader) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(reader, "reader");

        Command current = command;

        while (reader.hasNext()) {
            Command subcommand = findSubcommand(current, reader.peek());

            if (subcommand == null) {
                break;
            }

            current = subcommand;
            reader.read();
        }

        OptionParseResult optionResult = parseOptions(current, reader);

        if (optionResult.isFailure()) {
            return CommandMatchResult.failure(current, null, optionResult.getError());
        }

        return matchSignatures(current, DefaultArgumentReader.of(optionResult.getPositionalTokens()), optionResult);
    }

    private CommandMatchResult matchSignatures(Command command, ArgumentReader reader, OptionParseResult optionResult) {
        if (command.signatures().isEmpty()) {
            return CommandMatchResult.failure(command, null, "This command is not directly executable.");
        }

        Signature bestSignature = null;
        String bestError = null;
        int bestCursor = -1;

        for (Signature signature : command.signatures()) {
            int signatureCursor = reader.getCursor();
            Map<Argument<?>, Object> values = new LinkedHashMap<>();
            String error = null;
            int failureCursor = signatureCursor;

            for (Argument<?> argument : signature.arguments()) {
                int argumentCursor = reader.getCursor();
                ParseResult<?> result = argument.type().parser().parse(reader);

                if (result.isSuccess()) {
                    values.put(argument, result.getValue());
                    failureCursor = reader.getCursor();
                    continue;
                }

                reader.setCursor(argumentCursor);

                if (!argument.required()) {
                    continue;
                }

                error = buildArgumentFailure(argument, result.getError());
                failureCursor = argumentCursor;
                break;
            }

            if (error == null && reader.hasNext()) {
                error = "Unexpected trailing input: " + remaining(reader);
                failureCursor = reader.getCursor();
            }

            if (error == null) {
                return CommandMatchResult.success(new CommandMatch(command, signature, values, optionResult.getPresentOptions(), optionResult.getOptionValues()));
            }

            reader.setCursor(signatureCursor);

            if (failureCursor > bestCursor) {
                bestCursor = failureCursor;
                bestSignature = signature;
                bestError = error;
            }
        }

        return CommandMatchResult.failure(command, bestSignature, bestError == null ? "Invalid usage." : bestError);
    }

    private OptionParseResult parseOptions(Command command, ArgumentReader reader) {
        List<String> positionalTokens = new ArrayList<>();
        Set<CommandOption> presentOptions = new LinkedHashSet<>();
        Map<ValueCommandOption<?>, Object> optionValues = new LinkedHashMap<>();
        boolean stopOptionParsing = false;

        while (reader.hasNext()) {
            String token = reader.peek();

            if (stopOptionParsing) {
                positionalTokens.add(reader.read());
                continue;
            }

            if (token.equals("--")) {
                reader.read();
                stopOptionParsing = true;
                continue;
            }

            CommandOption option = resolveOption(command, token);

            if (option == null) {
                if (looksLikeOption(command, token)) {
                    return OptionParseResult.failure("Unknown option: " + token);
                }

                positionalTokens.add(reader.read());
                continue;
            }

            reader.read();

            if (!presentOptions.add(option)) {
                return OptionParseResult.failure("Duplicate option: " + token);
            }

            if (!(option instanceof ValueCommandOption<?> valueOption)) {
                continue;
            }

            int valueCursor = reader.getCursor();
            ParseResult<?> result = valueOption.getArgument().type().parser().parse(reader);

            if (!result.isSuccess()) {
                reader.setCursor(valueCursor);
                return OptionParseResult.failure("Invalid value for option '" + command.flagLiteral(option.getName()) + "': " + result.getError());
            }

            optionValues.put(valueOption, result.getValue());
        }

        return OptionParseResult.success(positionalTokens, presentOptions, optionValues);
    }

    private String buildArgumentFailure(Argument<?> argument, String error) {
        return "Invalid value for '" + argument.name() + "': " + error;
    }

    private String remaining(ArgumentReader reader) {
        StringJoiner joiner = new StringJoiner(" ");

        for (int i = reader.getCursor(); i < reader.size(); i++) {
            joiner.add(reader.getTokens().get(i));
        }

        return joiner.toString();
    }

    private Command findSubcommand(Command command, String input) {
        for (Command subcommand : command.subCommands()) {
            if (matches(subcommand, input)) {
                return subcommand;
            }
        }

        return null;
    }

    private CommandOption resolveOption(Command command, String token) {
        FlagKind kind = detectFlagKind(command, token, false);

        if (kind == FlagKind.NONE) {
            return null;
        }

        if (kind == FlagKind.LONG) {
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

    private boolean looksLikeOption(Command command, String token) {
        return detectFlagKind(command, token, false) != FlagKind.NONE;
    }

    private FlagKind detectFlagKind(Command command, String token, boolean allowBarePrefix) {
        if (token == null || token.isBlank()) {
            return FlagKind.NONE;
        }

        String longPrefix = command.longFlagPrefix();
        String shortPrefix = command.shortFlagPrefix();

        if (matchesPrefix(token, longPrefix, allowBarePrefix)) {
            return FlagKind.LONG;
        }

        if (matchesPrefix(token, shortPrefix, allowBarePrefix)) {
            if (!shortPrefix.equals(longPrefix) && token.startsWith(longPrefix)) {
                return FlagKind.NONE;
            }

            if (token.length() > shortPrefix.length() && Character.isDigit(token.charAt(shortPrefix.length()))) {
                return FlagKind.NONE;
            }

            return FlagKind.SHORT;
        }

        return FlagKind.NONE;
    }

    private boolean matchesPrefix(String token, String prefix, boolean allowBarePrefix) {
        if (!token.startsWith(prefix)) {
            return false;
        }

        return allowBarePrefix ? token.length() >= prefix.length() : token.length() > prefix.length();
    }

    private boolean matches(Command command, String input) {
        if (command.name().equalsIgnoreCase(input)) {
            return true;
        }

        for (String alias : command.aliases()) {
            if (alias.equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }

    private enum FlagKind {
        NONE,
        SHORT,
        LONG
    }

}