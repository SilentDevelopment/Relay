package io.github.silentdevelopment.relay.core.text;

import io.github.silentdevelopment.relay.text.CommandResponseRenderer;
import io.github.silentdevelopment.relay.text.CommandText;

import java.text.MessageFormat;
import java.util.List;
import java.util.StringJoiner;

public final class StringCommandResponseRenderer implements CommandResponseRenderer<String> {

    @Override
    public String renderUnknownCommand() {
        return "Unknown command.";
    }

    @Override
    public String renderNoHandler() {
        return "No handler is bound for this command.";
    }

    @Override
    public String renderInvalidUsage(CommandText message, List<String> usages) {
        StringBuilder builder = new StringBuilder();
        builder.append("Invalid usage: ").append(renderText(message));

        if (!usages.isEmpty()) {
            builder.append('\n');
            builder.append("Valid usages:");
            builder.append('\n');

            for (String usage : usages) {
                builder.append(" - ").append(usage).append('\n');
            }
        }

        return builder.toString().trim();
    }

    @Override
    public String renderRequirementFailure(CommandText message) {
        return "Access denied: " + renderText(message);
    }

    @Override
    public String renderAbort(CommandText message) {
        return renderText(message);
    }

    @Override
    public String renderSuggestions(List<String> suggestions) {
        StringJoiner joiner = new StringJoiner(", ");
        suggestions.forEach(joiner::add);
        return "Suggestions: [" + joiner + "]";
    }

    private String renderText(CommandText text) {
        if (text.arguments().isEmpty()) {
            return text.fallback();
        }

        return MessageFormat.format(text.fallback(), text.arguments().toArray());
    }

}