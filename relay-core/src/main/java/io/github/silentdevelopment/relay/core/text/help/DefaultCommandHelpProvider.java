package io.github.silentdevelopment.relay.core.text.help;

import io.github.silentdevelopment.relay.core.requirement.DefaultCommandAccessResolver;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import io.github.silentdevelopment.relay.text.help.CommandHelpOption;
import io.github.silentdevelopment.relay.text.help.CommandHelpProvider;
import io.github.silentdevelopment.relay.text.help.CommandHelpUsage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class DefaultCommandHelpProvider<S> implements CommandHelpProvider<S> {

    private final CommandAccessResolver<S> accessResolver;

    public DefaultCommandHelpProvider() {
        this(new DefaultCommandAccessResolver<>());
    }

    public DefaultCommandHelpProvider(CommandAccessResolver<S> accessResolver) {
        this.accessResolver = Objects.requireNonNull(accessResolver, "accessResolver");
    }

    @Override
    public List<CommandHelpEntry> build(S source, Command command) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(command, "command");
        return build(source, command, command.name());
    }

    @Override
    public List<CommandHelpEntry> build(S source, Command command, String label) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(command, "command");

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be null or blank.");
        }

        List<CommandHelpEntry> entries = new ArrayList<>();
        collect(entries, source, command, "/" + label);
        return List.copyOf(entries);
    }

    private void collect(List<CommandHelpEntry> entries, S source, Command command, String path) {
        if (!this.accessResolver.canAccess(source, command)) {
            return;
        }

        List<String> subcommands = new ArrayList<>();

        for (Command subcommand : command.subCommands()) {
            if (this.accessResolver.canAccess(source, subcommand)) {
                subcommands.add(subcommand.name());
            }
        }

        entries.add(new CommandHelpEntry(path, command.description(), buildOwnUsages(command, path), subcommands, buildOptions(command)));

        for (Command subcommand : command.subCommands()) {
            collect(entries, source, subcommand, path + " " + subcommand.name());
        }
    }

    private List<CommandHelpUsage> buildOwnUsages(Command command, String path) {
        List<CommandHelpUsage> usages = new ArrayList<>();

        for (Signature signature : command.signatures()) {
            String usage = formatUsage(path, command, signature);
            usages.add(new CommandHelpUsage(usage, path + " ", command.description()));
        }

        return List.copyOf(usages);
    }

    private String formatUsage(String path, Command command, Signature signature) {
        StringBuilder builder = new StringBuilder(path);
        appendSegment(builder, formatOptionsForUsage(command));
        appendSegment(builder, signature.usage());
        return builder.toString();
    }

    private String formatOptionsForUsage(Command command) {
        if (command.options().isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (CommandOption option : command.options()) {
            joiner.add(formatOptionUsage(command, option));
        }

        return joiner.toString();
    }

    private String formatOptionUsage(Command command, CommandOption option) {
        String literal = command.flagLiteral(option.getName());

        if (option instanceof ValueCommandOption<?> valueOption) {
            return "[" + literal + " " + valueOption.getArgument().usage() + "]";
        }

        return "[" + literal + "]";
    }

    private List<CommandHelpOption> buildOptions(Command command) {
        List<CommandHelpOption> options = new ArrayList<>();

        for (CommandOption option : command.options()) {
            String valueUsage = "";

            if (option instanceof ValueCommandOption<?> valueOption) {
                valueUsage = valueOption.getArgument().usage();
            }

            String literal = command.flagLiteral(option.getName());
            List<String> aliases = formatOptionAliases(command, option);
            options.add(new CommandHelpOption(literal, aliases, option.getDescription(), valueUsage));
        }

        return List.copyOf(options);
    }

    private List<String> formatOptionAliases(Command command, CommandOption option) {
        List<String> aliases = new ArrayList<>();

        for (String alias : option.getAliases()) {
            aliases.add(command.flagLiteral(alias));
        }

        return List.copyOf(aliases);
    }

    private void appendSegment(StringBuilder builder, String segment) {
        if (segment == null || segment.isBlank()) {
            return;
        }

        builder.append(' ').append(segment);
    }

}