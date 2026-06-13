package io.github.silentdevelopment.relay.core.text;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.text.CommandResponseRenderer;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.List;
import java.util.StringJoiner;

public class StringCommandResponseRenderer<S> implements CommandResponseRenderer<S, String> {

    public String renderUnknownCommand() {
        return renderUnknownCommand(null);
    }

    @Override
    public String renderUnknownCommand(S source) {
        return "Unknown command.";
    }

    public String renderNoHandler() {
        return renderNoHandler(null);
    }

    @Override
    public String renderNoHandler(S source) {
        return "No handler is bound for this command.";
    }

    public String renderInvalidUsage(CommandText message, List<String> usages) {
        return renderInvalidUsage(null, message, usages);
    }

    @Override
    public String renderInvalidUsage(S source, CommandText message, List<String> usages) {
        if (usages.isEmpty()) {
            return "Invalid usage: " + renderText(message);
        }

        StringBuilder builder = new StringBuilder("Invalid usage: ")
                .append(renderText(message))
                .append(System.lineSeparator())
                .append("Valid usages:");

        for (String usage : usages) {
            builder.append(System.lineSeparator())
                    .append(" - ")
                    .append(usage);
        }

        return builder.toString();
    }

    public String renderInvalidUsage(CommandText message, String path, Command command) {
        return renderInvalidUsage(null, message, path, command);
    }

    @Override
    public String renderInvalidUsage(S source, CommandText message, String path, Command command) {
        if (command.signatures().isEmpty()) {
            return "Invalid usage: " + renderText(message);
        }

        StringBuilder builder = new StringBuilder("Invalid usage: ")
                .append(renderText(message))
                .append(System.lineSeparator())
                .append("Valid usages:");

        for (Signature signature : command.signatures()) {
            builder.append(System.lineSeparator())
                    .append(" - ")
                    .append(path);

            for (var argument : signature.arguments()) {
                builder.append(argument.required() ? " <" : " [")
                        .append(argument.name())
                        .append(argument.required() ? ">" : "]");
            }
        }

        return builder.toString();
    }

    public String renderRequirementFailure(CommandText message) {
        return renderRequirementFailure(null, message);
    }

    @Override
    public String renderRequirementFailure(S source, CommandText message) {
        return "Access denied: " + renderText(message);
    }

    public String renderAbort(CommandText message) {
        return renderAbort(null, message);
    }

    @Override
    public String renderAbort(S source, CommandText message) {
        return renderText(message);
    }

    public String renderSuggestions(List<String> suggestions) {
        return renderSuggestions(null, suggestions);
    }

    @Override
    public String renderSuggestions(S source, List<String> suggestions) {
        StringJoiner joiner = new StringJoiner(", ");
        suggestions.forEach(joiner::add);
        return "Suggestions: [" + joiner + "]";
    }

    protected String renderText(CommandText text) {
        String value = text.fallback();

        for (int index = 0; index < text.arguments().size(); index++) {
            value = value.replace("{" + index + "}", String.valueOf(text.arguments().get(index)));
        }

        for (var entry : text.placeholders().entrySet()) {
            value = value.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }

        return value;
    }

}