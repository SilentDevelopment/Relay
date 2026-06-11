package io.github.silentdevelopment.relay.core.text.usage;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.command.option.CommandOption;
import io.github.silentdevelopment.relay.command.option.ValueCommandOption;
import io.github.silentdevelopment.relay.text.usage.CommandUsageFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class DefaultCommandUsageFormatter implements CommandUsageFormatter {

    @Override
    public List<String> format(Command command) {
        Objects.requireNonNull(command, "command");
        return format(command, command.name());
    }

    @Override
    public List<String> format(Command command, String label) {
        Objects.requireNonNull(command, "command");

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be null or blank.");
        }

        List<String> usages = new ArrayList<>();
        collect(usages, command, "/" + label);
        return List.copyOf(usages);
    }

    private void collect(List<String> usages, Command command, String path) {
        for (Signature signature : command.signatures()) {
            usages.add(formatUsage(path, command, signature));
        }

        for (Command subcommand : command.subCommands()) {
            collect(usages, subcommand, path + " " + subcommand.name());
        }
    }

    private String formatUsage(String path, Command command, Signature signature) {
        StringBuilder builder = new StringBuilder(path);
        appendSegment(builder, formatOptions(command));
        appendSegment(builder, signature.usage());
        return builder.toString();
    }

    private String formatOptions(Command command) {
        if (command.options().isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (CommandOption option : command.options()) {
            joiner.add(formatOption(command, option));
        }

        return joiner.toString();
    }

    private String formatOption(Command command, CommandOption option) {
        String literal = command.flagLiteral(option.getName());

        if (option instanceof ValueCommandOption<?> valueOption) {
            return "[" + literal + " " + valueOption.getArgument().usage() + "]";
        }

        return "[" + literal + "]";
    }

    private void appendSegment(StringBuilder builder, String segment) {
        if (segment == null || segment.isBlank()) {
            return;
        }

        builder.append(' ').append(segment);
    }

}